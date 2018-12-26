package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
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

import Utils.UserInfo;

public class AddAgeIntentHandler implements RequestHandler {
	public static final String NUMBER_SLOT = "Number";

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("AddAgeIntent"));
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
				dbRef = database.getReference().child(UserMail).child("User-Info");
		} catch (final Exception e) {
			speechText += e.getMessage() + " ";// its ok
		}
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		if (NumberSlot == null) {
			speechText = "I'm not sure how old are you. Please tell me again";
			repromptText = "I will repeat, I'm not sure how old are you. Please tell me again";
		} else {
			final int age = Integer.parseInt(NumberSlot.getValue());

			final List<UserInfo> UserList = new LinkedList<>();
			final List<String> UserId = new LinkedList<>();
			UserInfo u = new UserInfo();
			u.setAge(age);
			// UserList.add(u);
			final CountDownLatch done = new CountDownLatch(1);
			dbRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(final DataSnapshot s) {
					for (final DataSnapshot userSnapshot : s.getChildren()) {
						UserList.add(userSnapshot.getValue(UserInfo.class));
						UserId.add(userSnapshot.getKey());
					}
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
				try {
					if (dbRef != null)
						dbRef.push().setValueAsync(u).get();
				} catch (InterruptedException | ExecutionException e) {
					speechText += e.getMessage() + " ";
				}
			else
				try {
					FirebaseDatabase.getInstance().getReference().child(UserMail).child("User-Info").child(UserId.get(0))
							.setValueAsync(new UserInfo(UserList.get(0).getGender(), age,
									UserList.get(0).getHeight(),
									UserList.get(0).getDailyCaloriesGoal(), UserList.get(0).getDailyLitresOfWaterGoal(),
									UserList.get(0).getDailyProteinGramsGoal(), UserList.get(0).getDailyCarbsGoal(),
									UserList.get(0).getDailyFatsGoal(), UserList.get(0).getDailyLimitCigarettes()))
							.get();
				} catch (final InterruptedException e) {
					e.printStackTrace();
				} catch (final ExecutionException e) {
					e.printStackTrace();
				}

			speechText = String.format("logged succesfully", Integer.valueOf(age));
			repromptText = "I will repeat, You can ask me how old are you saying, how older am i?";

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}
}
