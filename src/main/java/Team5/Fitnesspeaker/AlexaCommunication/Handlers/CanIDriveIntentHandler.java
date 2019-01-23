package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.Pair;
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
import Utils.UserInfo;
import Utils.DBUtils;
import Utils.DailyInfo;
import Utils.DBUtils.DBException;

public class CanIDriveIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("CanIDriveIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);
		String speechText = "";
		UserInfo ui=null;
		try {
		    ui=db.DBGetUserInfo();
		}catch(Exception e) {
			speechText = String.format("you didn't tell me what is your gender");
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withShouldEndSession(Boolean.FALSE).build();
		} catch (DBException e1) {}
			
		int weight=-1;

		// ################# need to refactor ##################### 
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		final String user_mail=i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
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
						dbRef = database.getReference().child(user_mail).child("Dates").child(DBUtils.getDate()).child("Daily-Info");
					} catch (final Exception e) {
						speechText += e.getMessage() + " ";// its ok
					}
					// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

					final List<DailyInfo> DailyInfoList = new LinkedList<>();
					final CountDownLatch done = new CountDownLatch(1);
					dbRef.addValueEventListener(new ValueEventListener() {
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

					if (DailyInfoList.isEmpty())
						speechText = String.format("you didn't tell me what is your weight");
					else {
						  weight = (int)(DailyInfoList.get(0).getWeight());
						if (weight == -1)
							speechText = String.format("you didn't tell me what is your weight");
					}
					
					// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
					// ################## need to refactor ###################### 
		
		if (weight==-1) {
			speechText = String.format("you didn't tell me what is your weight");
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withShouldEndSession(Boolean.FALSE).build();
		}
			
		List<Portion> todaysAlchohol = new LinkedList<>();
		try {
			for(Pair<String, Portion> p : db.DBGetTodayAlcoholList()) {
				todaysAlchohol.add(p.getValue());
			}
		} catch (DBException e) {}
		
		double alcInblood=0;
		if(ui.gender==UserInfo.Gender.MALE)
			alcInblood=new AlcoholBloodCalc().setForMale().setWeight(weight).CalcForNow(todaysAlchohol);
		else
			alcInblood=new AlcoholBloodCalc().setForFemale().setWeight(weight).CalcForNow(todaysAlchohol);
		if ( alcInblood>= 0.05)
			speechText = "you can't drive right now, you drank too much";
		else
			speechText = "you are allowed to drive, go safely";

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
