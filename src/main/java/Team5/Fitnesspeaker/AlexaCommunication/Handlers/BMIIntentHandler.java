package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

import Utils.DailyInfo;
import Utils.UserInfo;

public class BMIIntentHandler implements RequestHandler{
	
	public static String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}
	
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("BMIIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText = "";

		// added
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		DatabaseReference dbRef = null, dbRef2 = null;
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
			if (database != null) {
				dbRef = database.getReference().child(UserMail).child("User-Info");
				dbRef2 = database.getReference().child(UserMail).child("Dates").child(getDate()).child("Daily-Info");
			}
		} catch (final Exception e) {
			speechText += e.getMessage() + " ";// its ok
		}
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		// Get the Weight
		
		final List<DailyInfo> DailyInfoList = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef2.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot userSnapshot : s.getChildren())
					DailyInfoList.add(userSnapshot.getValue(DailyInfo.class));
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
		
		// get the height
		
		final List<UserInfo> UserList = new LinkedList<>();
		final CountDownLatch done2 = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				UserList.add(s.getValue(UserInfo.class));
				done2.countDown();
			}

			@Override
			public void onCancelled(final DatabaseError e) {
				System.out.println("The read failed: " + e.getCode());
			}
		});
		try {
			done2.await();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
		}


		if (DailyInfoList.isEmpty() || ((int)(DailyInfoList.get(0).getWeight()))==-1) {
			if(UserList.isEmpty() || UserList.get(0).getHeight()==-1) {
				speechText = String.format("Please Tell me your weight and height, So i can calculate your BMI");
			}
			else {
				speechText = String.format("Please Tell me your weight, So i can calculate your BMI");
			}
		}
		else {
			final int weight = (int)(DailyInfoList.get(0).getWeight());
			if (weight == -1)
				speechText = String.format("Please Tell me your weight, So i can calculate your BMI");
			else {
				// Get the height

				if (UserList.isEmpty())
					speechText = String.format("Please Tell me your height, So i can calculate your BMI");
				else {
					final int height = UserList.get(0).getHeight();
					if (height == -1)
						speechText = String.format("Please Tell me your height, So i can calculate your BMI");
					else {
						double heightInMeters = ((double) height) / 100.0;
						double bmi = ((double) weight) / (heightInMeters*heightInMeters);
						speechText = String.format("your BMI is %.2f", Double.valueOf(bmi));
					}

				}
			}
		}
		
		
		

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
