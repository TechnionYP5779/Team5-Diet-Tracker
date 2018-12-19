package Team5.Fitnesspeaker.AlexaCommunication.Handlers;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Utils.Portion;
import Utils.PortionRequestGen;
import Utils.Portion.Type;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import static com.amazon.ask.request.Predicates.intentName;

public class AddDrinkIntent implements RequestHandler {
	public static final String NUMBER_SLOT = "Number";
    @Override
    public boolean canHandle(HandlerInput i) {
        return i.matches(intentName("addDrinkIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput i) {
        Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().get(NUMBER_SLOT);
        String speechText="", repromptText;
        
        //added database
      //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        String UserMail = "shalev@gmail";
		DatabaseReference dbRef = null;
		try {
			FileInputStream serviceAccount;
			FirebaseOptions options = null;
			try {
				serviceAccount = new FileInputStream("db_credentials.json");
				options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://fitnesspeaker.firebaseio.com/").build();
				FirebaseApp.initializeApp(options);
			} catch (Exception e1) {
				speechText += e1.getMessage() + " ";// its ok
			}
			FirebaseDatabase database = FirebaseDatabase.getInstance();
			if (database != null)
				dbRef = database.getReference().child(UserMail).child("Drink");
		} catch (Exception e) {
			speechText += e.getMessage() + " ";// its ok
		}
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        if (NumberSlot == null ) {
			speechText = "I'm not sure how many glasses you drank. Please tell me again";
			repromptText = "I will repeat, I'm not sure how many glasses you drank. Please tell me again";
		} else {
			int added_num_of_glasses = Integer.parseInt(NumberSlot.getValue());
			
			final List<Long> DrinkCount = new LinkedList();
			DrinkCount.add(Long.valueOf(added_num_of_glasses));
			CountDownLatch done = new CountDownLatch(1);
			dbRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					Long count = dataSnapshot.getValue(Long.class);
					if(count!=null) DrinkCount.set(0, Long.valueOf(count.longValue()+DrinkCount.get(0).longValue()));
					done.countDown();
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {
					System.out.println("The read failed: " + databaseError.getCode());
				}
			});
			try {
				done.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
			
			if (dbRef != null)
				try {
					dbRef.setValueAsync(DrinkCount.get(0)).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if(added_num_of_glasses==1) speechText = String.format("you added one glass of water. You can ask me how many glasses you drank so far saying, "
					+ "how many glasses of water i drank so far?");
			else
			speechText = String.format("you added %d glasses of water. You can ask me how many glasses you drank so far saying, "
					+ "how many glasses of water i drank so far?",
					Integer.valueOf(added_num_of_glasses));
			repromptText = "I will repeat, You can ask me how many you drank so far saying, how many glasses of water i drank so far?";
		
		}

        return i.getResponseBuilder()
                .withSimpleCard("FitnessSpeakerSession", speechText)
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .withShouldEndSession(Boolean.FALSE)
                .build();
    }

}