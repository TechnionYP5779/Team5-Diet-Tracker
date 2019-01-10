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

public class SetGoalIntentHandler implements RequestHandler{
	public static final String MEASURE_SLOT = "Measure";
	public static final String NUMBER_SLOT = "Number";
	
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("SetGoalIntent"));
	}
	
	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(NUMBER_SLOT);
		
		final Slot MeasureSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(MEASURE_SLOT);
		
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

		if (NumberSlot == null || MeasureSlot == null) {
			speechText = "I'm not sure what is your goal. Please tell me again";
			repromptText = "I will repeat, I'm not sure what is your goal. Please tell me again";
		} else {
			final String measure_str = MeasureSlot.getValue();
			final int amount = Integer.parseInt(NumberSlot.getValue());

			final List<UserInfo> UserList = new LinkedList<>();
			final List<String> UserId = new LinkedList<>();
			UserInfo u = new UserInfo();
			if ("fats".contains(measure_str))
				u.setDailyFatsGoal(amount);
			else if ("carbs".contains(measure_str))
				u.setDailyCarbsGoal(amount);
			else if ("proteins".contains(measure_str))
				u.setDailyProteinGramsGoal(amount);
			else if ("calories".contains(measure_str))
				u.setDailyCaloriesGoal(amount);
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
					if ("fats".contains(measure_str))
						FirebaseDatabase.getInstance().getReference().child(UserMail).child("User-Info").child(UserId.get(0))
						.setValueAsync(new UserInfo(UserList.get(0).getGender(), UserList.get(0).getAge(),
								UserList.get(0).getHeight(),
								UserList.get(0).getDailyCaloriesGoal(),
								UserList.get(0).getDailyProteinGramsGoal(), UserList.get(0).getDailyCarbsGoal(),
								amount, UserList.get(0).getDailyLimitCigarettes()))
						.get();
					else if ("carbs".contains(measure_str))
						FirebaseDatabase.getInstance().getReference().child(UserMail).child("User-Info").child(UserId.get(0))
						.setValueAsync(new UserInfo(UserList.get(0).getGender(), UserList.get(0).getAge(),
								UserList.get(0).getHeight(),
								UserList.get(0).getDailyCaloriesGoal(),
								UserList.get(0).getDailyProteinGramsGoal(), amount,
								UserList.get(0).getDailyFatsGoal(), UserList.get(0).getDailyLimitCigarettes()))
						.get();
					else if ("proteins".contains(measure_str))
						FirebaseDatabase.getInstance().getReference().child(UserMail).child("User-Info").child(UserId.get(0))
						.setValueAsync(new UserInfo(UserList.get(0).getGender(), UserList.get(0).getAge(),
								UserList.get(0).getHeight(),
								UserList.get(0).getDailyCaloriesGoal(),
								amount, UserList.get(0).getDailyCarbsGoal(),
								UserList.get(0).getDailyFatsGoal(), UserList.get(0).getDailyLimitCigarettes()))
						.get();
					else if ("calories".contains(measure_str))
						FirebaseDatabase.getInstance().getReference().child(UserMail).child("User-Info").child(UserId.get(0))
						.setValueAsync(new UserInfo(UserList.get(0).getGender(), UserList.get(0).getAge(),
								UserList.get(0).getHeight(),amount,
								UserList.get(0).getDailyProteinGramsGoal(), UserList.get(0).getDailyCarbsGoal(),
								UserList.get(0).getDailyFatsGoal(), UserList.get(0).getDailyLimitCigarettes()))
						.get();
				} catch (final InterruptedException e) {
					e.printStackTrace();
				} catch (final ExecutionException e) {
					e.printStackTrace();
				}

			speechText = String.format("logged successfully");
			repromptText = "I will repeat, logged successfully";

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}
}
