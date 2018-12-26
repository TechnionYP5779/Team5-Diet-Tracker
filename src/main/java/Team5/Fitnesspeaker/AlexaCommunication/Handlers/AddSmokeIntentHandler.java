package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

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

public class AddSmokeIntentHandler implements RequestHandler{
	public static final String NUMBER_SLOT = "Number";
	
	public static String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("AddSmokeIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(NUMBER_SLOT);
		String speechText = "", repromptText;

		// added database
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
				dbRef = database.getReference().child(UserMail).child("Dates").child(getDate()).child("Cigarettes");
		} catch (final Exception e) {
			speechText += e.getMessage() + " ";// its ok
		}
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		if (NumberSlot == null) {
			speechText = "I'm not sure how many cigarettes you smoked. Please tell me again";
			repromptText = "I will repeat, I'm not sure how many cigarettes you smoked. Please tell me again";
		} else {
			final int added_num_of_cigarettes = Integer.parseInt(NumberSlot.getValue());

			final List<Long> SmokeCount = new LinkedList<>();
			SmokeCount.add(Long.valueOf(added_num_of_cigarettes));
			final CountDownLatch done = new CountDownLatch(1);
			dbRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(final DataSnapshot s) {
					final Long count = s.getValue(Long.class);
					if (count != null)
						SmokeCount.set(0, Long.valueOf(count.longValue() + SmokeCount.get(0).longValue()));
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

			if (dbRef != null)
				try {
					dbRef.setValueAsync(SmokeCount.get(0)).get();
				} catch (final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			if (added_num_of_cigarettes == 1)
				speechText = String.format(
						"you added one cigarette. You can ask me how many cigarettes you have smoked so far saying, "
								+ "how many cigarettes i smoked so far?");
			else
				speechText = String.format(
						"you added %d cigarettes. You can ask me how many cigarettes you have smoked so far saying, "
								+ "how many cigarettes i smoked so far?",
						Integer.valueOf(added_num_of_cigarettes));
			repromptText = "I will repeat, You can ask me how many you have smoked so far saying, how many cigarettes i smoked so far?";

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}

}
