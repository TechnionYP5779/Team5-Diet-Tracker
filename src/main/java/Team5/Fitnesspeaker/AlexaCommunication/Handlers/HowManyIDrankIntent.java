package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HowManyIDrankIntent implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("HowMuchIDrankIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText = "";

		// added
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		final List<Integer> DrinkCount = new LinkedList();
		CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Integer count = dataSnapshot.getValue(Integer.class);
				if(count!=null) DrinkCount.add(count);
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

		if (DrinkCount.size() == 0) {
			speechText = String.format("you didn't drink today");
		} else {
			Integer count = DrinkCount.get(0);
			if (count.intValue() == 1)
				speechText = String.format("you drank one glass of water");
			else
				speechText = String.format("you drank %d glasses of water", count);

		}

		/*
		 * Map<String, Object> items = new TreeMap<String, Object>(
		 * i.getAttributesManager().getSessionAttributes()); Integer count
		 * =items.get("Drink")==null? Integer.valueOf(0):(Integer)items.get("Drink"); if
		 * (count.intValue()>0) if (count.intValue() == 1) speechText =
		 * String.format("you drank one glass of water"); else speechText =
		 * String.format("you drank %d glasses of water", count); else speechText =
		 * "you did not drink so far";
		 */

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
