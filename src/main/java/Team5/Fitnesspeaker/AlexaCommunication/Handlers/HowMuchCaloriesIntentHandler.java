package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
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

//import Utils.DailyInfo;
import Utils.Portion;
import Utils.UserInfo;

public class HowMuchCaloriesIntentHandler implements RequestHandler {
	public static final String MEASURE_SLOT = "Measure";

	public static String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("HowMuchMeasureIntent"));
	}

	@SuppressWarnings("boxing")
	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText = "";
		final Slot measureSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(MEASURE_SLOT);

		// added
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		DatabaseReference dbRef = null, dbRef2 = null;
		// DatabaseReference dbRef2 = null;
		try {
			FileInputStream serviceAccount;
			FirebaseOptions options = null;
			try {
				serviceAccount = new FileInputStream("db_credentials.json");
				options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://fitnesspeaker-6eee9.firebaseio.com/").build();
				FirebaseApp.initializeApp(options);
			} catch (final Exception e1) {
				speechText += e1.getMessage() + " "; // its ok
			}
			final FirebaseDatabase database = FirebaseDatabase.getInstance();
			if (database != null) {
				dbRef = database.getReference().child(UserMail).child("User-Info");
				dbRef2 = database.getReference().child(UserMail).child("Dates").child(getDate()).child("Food");
			}
		} catch (final Exception e) {
			speechText += e.getMessage() + " ";// its ok
		}
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		// final List<DailyInfo> DailyInfoList = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		final String measure_str = measureSlot.getValue();
		final List<Integer> total_measure = new LinkedList<>();
		total_measure.add(Integer.valueOf(0));
		// count the calories
		dbRef2.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot portionSnapshot : s.getChildren()) {
					double measure = 0;
					double amount = portionSnapshot.getValue(Portion.class).getAmount();
					if ("fats".equals(measure_str))
						measure = portionSnapshot.getValue(Portion.class).getFats_per_100_grams();
					else if ("carbs".equals(measure_str))
						measure = portionSnapshot.getValue(Portion.class).getCarbs_per_100_grams();
					else if ("proteins".equals(measure_str))
						measure = portionSnapshot.getValue(Portion.class).getProteins_per_100_grams();
					else if ("calories".equals(measure_str))
						measure = portionSnapshot.getValue(Portion.class).getCalories_per_100_grams();
					total_measure.set(0, (int) (total_measure.get(0) + measure * ((double) amount / 100)));
				}
				done.countDown();
			}

			@Override
			public void onCancelled(final DatabaseError e) {
				System.out.println("The read failed: " + e.getCode());
			}
		});
		final List<UserInfo> userInfo = new LinkedList<>();
		// get the goal
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot userSnapshot : s.getChildren())
					userInfo.add(userSnapshot.getValue(UserInfo.class));
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

		// TODO: say the goal even if i didn't eat anything today.
		// meaning the second else will be executed.
		if (total_measure.get(0) == 0)
			speechText = String.format("you didn't eat anything today");
		else if ("calories".equals(measure_str))
			speechText = String.format("You ate %d %s today. There are %d %s left for your goal! Keep going!",
					total_measure.get(0), measure_str,
					(int) userInfo.get(0).getDailyCaloriesGoal() - total_measure.get(0), measure_str);
		else {
			int fats = (int) userInfo.get(0).getDailyFatsGoal();
			int carbs = (int) userInfo.get(0).getDailyCarbsGoal();
			int proteins = (int) userInfo.get(0).getDailyProteinGramsGoal();
			if ("proteins".equals(measure_str) && proteins != -1)
				speechText = String.format(
						"you ate %d grams of %s today. There are %d %s left for your goal! Keep going!",
						total_measure.get(0), measure_str, proteins - total_measure.get(0), measure_str);
			else if ("carbs".equals(measure_str) && carbs != -1)
				speechText = String.format(
						"you ate %d grams of %s today. There are %d %s left for your goal! Keep going!",
						total_measure.get(0), measure_str, carbs - total_measure.get(0), measure_str);
			else if ("fats".equals(measure_str) && fats != -1)
				speechText = String.format(
						"you ate %d grams of %s today. There are %d %s left for your goal! Keep going!",
						total_measure.get(0), measure_str, fats - total_measure.get(0), measure_str);
			else
				speechText = String.format(
						"you ate %d grams of %s today. Please notice you didn't tell me your %s goal.",
						total_measure.get(0), measure_str, measure_str);

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
