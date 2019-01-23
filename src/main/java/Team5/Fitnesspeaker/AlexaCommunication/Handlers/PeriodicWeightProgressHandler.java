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
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.DailyInfo;
import Utils.UserInfo;

public class PeriodicWeightProgressHandler implements RequestHandler{
	public static final String NUMBER_SLOT = "Number";

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("PeriodicWeightProgress"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		    String speechText = "", repromptText="";
			final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail();
			DBUtils db=new DBUtils(UserMail);
			UserInfo ui=null;
			//DailyInfo di=null;
			String c="";
			try {
				ui=db.DBGetUserInfo();
				//di=db.DBGetTodayDailyInfo();
			} catch (DBException e) {}
			if(ui ==null) {
				c+="ui";
				speechText="you didn't tell me your weight goal yet";
				repromptText="you didn't tell me your weight goal yet";
				ui=new UserInfo();
			}
				
			int wg=ui.getWeightGoal();
			int w=-1; // db.getTodayWeight();
			
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
				  w = (int)(DailyInfoList.get(0).getWeight());
				if (w == -1)
					speechText = String.format("you didn't tell me what is your weight");
			}
			
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// ################## need to refactor ###################### 
			
			if(wg==-1) {
				speechText="you didn't tell me your goal yet";
				repromptText="you didn't tell me your goal yet";
			}
				
			if(w==-1) {
				speechText="you didn't tell me your weight yet";
				repromptText="you didn't tell me your weight yet";
			}
				
			if(wg != -1 && w!= -1) {
				int diff = wg-w;
				if(diff<0)
					diff= diff*-1;
				if(diff<3) {
					speechText="you are doing great, keep going";
					repromptText="you are doing great, keep going";
				}
				else {
					speechText="you are stil far from your goal";
					repromptText="you are stil far from your goal";
				}
			}
			
			
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText+c).withSpeech(speechText+c)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}
}