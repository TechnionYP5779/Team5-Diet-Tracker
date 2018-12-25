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
import Utils.Portion;



public class CanIDriveIntentHandler {
	

	public class DrinkAlchoholHandler implements RequestHandler {


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
			final String UserMail = "shalev@gmail";
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
			final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(UserMail).child("Dates").child(getDate()).child("Alchohol");

			final List<Portion> todaysAlchohol = new LinkedList<>();
			final CountDownLatch done = new CountDownLatch(1);
			dbRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(final DataSnapshot s) {
					for (final DataSnapshot portionSnapshot : s.getChildren())
						todaysAlchohol.add(portionSnapshot.getValue(Portion.class));
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

			String speechText="";
			
			if(new AlcoholBloodCalc().CalcForNow(todaysAlchohol)>=0.05)
				speechText="you can't drive right now";
			else
				speechText="you are allowed to drive";
			
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withShouldEndSession(Boolean.FALSE).build();

		}
	}
}

