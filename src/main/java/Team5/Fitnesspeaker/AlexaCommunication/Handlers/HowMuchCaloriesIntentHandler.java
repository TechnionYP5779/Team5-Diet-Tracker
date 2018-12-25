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

	@SuppressWarnings({ "boxing" })
	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText = "";
		final Slot measureSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(MEASURE_SLOT);

		// added
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		final String UserMail = "shalev@gmail";
		// DatabaseReference dbRef = null, dbRef2 = null;
		DatabaseReference dbRef2 = null;
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
				// dbRef =
				// database.getReference().child(UserMail).child("Dates").child(getDate()).child("Daily-Info");
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
					if (measure_str.equals("fats"))
						measure = portionSnapshot.getValue(Portion.class).getFats_per_100_grams();
					else if (measure_str.equals("carbs"))
						measure = portionSnapshot.getValue(Portion.class).getCarbs_per_100_grams();
					else if (measure_str.equals("proteins"))
						measure = portionSnapshot.getValue(Portion.class).getProteins_per_100_grams();
					else if (measure_str.equals("calories"))
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

//		// update the calories
//		dbRef.addValueEventListener(new ValueEventListener() {
//			@Override
//			public void onDataChange(final DataSnapshot s) {
//				for (final DataSnapshot userSnapshot : s.getChildren())
//					DailyInfoList.add(userSnapshot.getValue(DailyInfo.class));
//				done.countDown();
//			}
//
//			@Override
//			public void onCancelled(final DatabaseError e) {
//				System.out.println("The read failed: " + e.getCode());
//			}
//		});
		try {
			done.await();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
		}

		if (total_measure.get(0) == 0)
			speechText = String.format("you didn't eat anything today");
		else if (measure_str.equals("calories")) {
			speechText = String.format("you ate %d %s today", total_measure.get(0), measure_str);
		} else {
			speechText = String.format("you ate %d grams of %s today", total_measure.get(0), measure_str);
		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
