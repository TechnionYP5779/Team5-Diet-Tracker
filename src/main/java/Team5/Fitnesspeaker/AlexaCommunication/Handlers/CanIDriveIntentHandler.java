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

import Utils.AlcoholBloodCalc;
import Utils.DailyInfo;
import Utils.Portion;
import Utils.UserInfo;

public class CanIDriveIntentHandler implements RequestHandler {

	private String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("CanIDriveIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		try {
			FileInputStream serviceAccount;
			FirebaseOptions options = null;
			try {
				serviceAccount = new FileInputStream("db_credentials.json");
				options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://fitnesspeaker-6eee9.firebaseio.com/").build();
				FirebaseApp.initializeApp(options);
			} catch (final Exception e1) {
				// empty block

			}
		} catch (final Exception e) {
			// empty block

		}
		
		String speechText = "";

		final DatabaseReference UIRef = FirebaseDatabase.getInstance().getReference().child(UserMail).child("User-Info");
		final List<UserInfo> UserList = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		UIRef.addValueEventListener(new ValueEventListener() {
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

		if (UserList.isEmpty()) {
			speechText = String.format("you didn't tell me what is your gender");
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withShouldEndSession(Boolean.FALSE).build();
		}
		
		final DatabaseReference DIRef = FirebaseDatabase.getInstance().getReference().child(UserMail).child("Dates").child(getDate()).child("Daily-Info");
		final List<DailyInfo> DailyList = new LinkedList<>();
		final CountDownLatch donee = new CountDownLatch(1);
		DIRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot userSnapshot : s.getChildren())
					DailyList.add(userSnapshot.getValue(DailyInfo.class));
				donee.countDown();
			}

			@Override
			public void onCancelled(final DatabaseError e) {
				System.out.println("The read failed: " + e.getCode());
			}
		});
		try {
			donee.await();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
		}

		if (DailyList.isEmpty()||DailyList.get(0).weight==-1) {
			speechText = String.format("you didn't tell me what is your weight");
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withShouldEndSession(Boolean.FALSE).build();
		}
			
		final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(UserMail).child("Dates")
				.child(getDate()).child("Alcohol");

		final List<Portion> todaysAlchohol = new LinkedList<>();
		final CountDownLatch done2 = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot portionSnapshot : s.getChildren())
					todaysAlchohol.add(portionSnapshot.getValue(Portion.class));
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

		double alcInblood=0;
		if(UserList.get(0).gender==UserInfo.Gender.MALE)
			alcInblood=new AlcoholBloodCalc().setForMale().setWeight(DailyList.get(0).getWeight()).CalcForNow(todaysAlchohol);
		else
			alcInblood=new AlcoholBloodCalc().setForFemale().setWeight(DailyList.get(0).getWeight()).CalcForNow(todaysAlchohol);
		if ( alcInblood>= 0.05)
			speechText = "you can't drive right now, you drank too much";
		else
			speechText = "you are allowed to drive, go safely";

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
