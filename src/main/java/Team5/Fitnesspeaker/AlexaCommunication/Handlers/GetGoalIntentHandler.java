package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

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

import Utils.UserInfo;

public class GetGoalIntentHandler implements RequestHandler{
	public static final String MEASURE_SLOT = "Measure";
	
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("GetGoalIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		
		final Slot MeasureSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(MEASURE_SLOT);
		String speechText = "", repromptText="";

		// added
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		DatabaseReference dbRef = null;
		try {
			FileInputStream serviceAccount;
			FirebaseOptions options = null;
			try {
				serviceAccount = new FileInputStream("db_credentials.json");
				options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://fitnesspeaker-6eee9.firebaseio.com/").build();
				FirebaseApp.initializeApp(options);
			} catch (final Exception e1) {
				speechText += e1.getMessage() + " ";// its ok
			}
			final FirebaseDatabase database = FirebaseDatabase.getInstance();
			if (database != null)
				dbRef = database.getReference().child(UserMail).child("User-Info");
		} catch (final Exception e) {
			speechText += e.getMessage() + " ";// its ok
		}
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		if (MeasureSlot == null) {
			speechText = "I'm not sure which goal do you want. Please tell me again";
			repromptText = "I will repeat, I'm not sure which goal do you want. Please tell me again";
		} else {
			final String measure_str = MeasureSlot.getValue();
			
			final List<UserInfo> UserList = new LinkedList<>();
			final CountDownLatch done = new CountDownLatch(1);
			dbRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(final DataSnapshot s) {
					for (final DataSnapshot userSnapshot : s.getChildren())
						UserList.add(userSnapshot.getValue(UserInfo.class));
					done.countDown();
				}

				@Override
				public void onCancelled(final DatabaseError e) {
					System.out.println("The read failed: " + e.getCode());
				}
			});
			try {
				done.await();
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
			}

			if (UserList.isEmpty())
				speechText = String.format("you didn't tell me what is your goal");
			else {
				int amount=0;
				if ("fats".equals(measure_str))
					amount = (int)UserList.get(0).getDailyFatsGoal();
				else if ("carbs".equals(measure_str))
					amount = (int)UserList.get(0).getDailyCarbsGoal();
				else if ("proteins".equals(measure_str))
					amount = (int)UserList.get(0).getDailyProteinGramsGoal();
				else if ("calories".equals(measure_str))
					amount = (int)UserList.get(0).getDailyCaloriesGoal();
				if (amount == -1)
					speechText = String.format("you didn't tell me what is your " + measure_str + " goal");
				else if ("carbs".equals(measure_str))
					speechText = String.format("your " + measure_str + " goal is %d calories", Integer.valueOf(amount));
				else
					speechText = String.format("your " + measure_str + " goal is %d grams", Integer.valueOf(amount));
				

			}
		}
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();

		

	}
}
