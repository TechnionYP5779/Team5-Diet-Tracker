package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
import java.util.ArrayList;
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
import Utils.EmailSender;
@SuppressWarnings("static-method")
public class WeightProgressIntentHandler implements RequestHandler {

	String UserMail;
	String UserName;
	private final int MAX_DAYS=14;

	private String[] getDates() {
		String[] dates = new String[MAX_DAYS];
		Calendar weekDay = Calendar.getInstance();
		weekDay.add(Calendar.DATE, -MAX_DAYS);
		for (int j = 0; j < MAX_DAYS; ++j) {
			weekDay.add(Calendar.DATE, 1);
			String[] date = weekDay.getTime().toString().split("\\s+");
			dates[j] = date[2] + "-" + date[1] + "-" + date[5];
		}
		return dates;
	}

	private void getUserInfo(HandlerInput i) {
		this.UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		this.UserName = i.getServiceClientFactory().getUpsService().getProfileGivenName();
	}

	
	private void openDatabase() {
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
	}

	private int getWeightByDate(String date) {
		final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(UserMail)
				.child("Dates").child(date).child("Daily-Info");
		
		
		final List<DailyInfo> UserList = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot userSnapshot : s.getChildren())
					UserList.add(userSnapshot.getValue(DailyInfo.class));
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
		
		if(UserList.isEmpty())
			return -1;
		return (int) UserList.get(0).getWeight();
	}
		

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("weightProgressIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		ArrayList<Calendar> dts=new ArrayList<>();
		ArrayList<Integer> wts=new ArrayList<>();
		String s="";
		try {
		getUserInfo(i);
		openDatabase();
		String[] dates = getDates();
		
		int w=-1;
		for (int day = 0; day < MAX_DAYS; ++day) {
			w=getWeightByDate(dates[day]);
			if(w!=-1) {
				wts.add(Integer.valueOf(w));
				Calendar c=Calendar.getInstance();
				c.add(Calendar.DAY_OF_YEAR, -MAX_DAYS+day);
				dts.add(c);
			}
			
		}
		try {
			if( wts.size()>=5) 
				(new EmailSender()).sendWeightStatistics("weight progess", this.UserMail.replace("_dot_", "."), UserName, dts, wts);
		} catch (Exception e) {
			//s+=e.toString();
		}
		} catch(Exception e) {
			//s+=" "+e.toString();
		}
		//s+=" "+String.valueOf(dts.size())+" "+String.valueOf(wts.size());
		if( wts.size()>=5)
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", "Mail with weight progess Sent"+s)
				.withSpeech("Mail with weight progess Sent").withShouldEndSession(Boolean.FALSE).build();
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", "I don't have enough mesurements"+s)
				.withSpeech("I don't have enough mesurements"+s).withShouldEndSession(Boolean.FALSE).build();
	}

}
