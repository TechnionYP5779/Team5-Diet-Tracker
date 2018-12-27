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

import Utils.DailyStatistics;
import Utils.EmailSender;
import Utils.Portion;
import Utils.WeeklyStatistics;

public class WeeklyMailReportHandler implements RequestHandler {

	String UserMail;
	String UserName;
	WeeklyStatistics weeklyStatistics = new WeeklyStatistics();

	private String[] getDates() {
		String[] dates = new String[7];
		Calendar weekDay = Calendar.getInstance();
		weekDay.add(Calendar.DATE, -7);
		for (int j = 0; j < 7; ++j) {
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

	private String getDrinkInfo(String date) {
		final DatabaseReference dbRefDrink = FirebaseDatabase.getInstance().getReference().child(UserMail)
				.child("Dates").child(date).child("Drink");
		final List<Integer> DrinkCount = new LinkedList<>();
		final CountDownLatch doneDrink = new CountDownLatch(1);
		dbRefDrink.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				final Integer count = s.getValue(Integer.class);
				if (count != null)
					DrinkCount.add(count);
				doneDrink.countDown();
			}

			@Override
			public void onCancelled(final DatabaseError e) {
				System.out.println("The read failed: " + e.getCode());
			}
		});
		try {
			doneDrink.await();
		} catch (final InterruptedException e) {
			// empty block
		}

		if (DrinkCount.isEmpty())
			return "0";
		else
			return DrinkCount.get(0).toString();
	}

	private List<Portion> getFoodInfo(String date) {
		final DatabaseReference dbRefFood = FirebaseDatabase.getInstance().getReference().child(UserMail).child("Dates")
				.child(date).child("Food");
		final List<Portion> FoodList = new ArrayList<>();
		final CountDownLatch doneFood = new CountDownLatch(1);
		dbRefFood.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot portionSnapshot : s.getChildren())
					FoodList.add(portionSnapshot.getValue(Portion.class));
				doneFood.countDown();
			}

			@Override
			public void onCancelled(final DatabaseError e) {
				System.out.println("The read failed: " + e.getCode());
			}
		});
		try {
			doneFood.await();
		} catch (final InterruptedException e) {
			// empty block
		}
		return FoodList;
	}

	private String getCiggaretsInfo(String date) {
		final DatabaseReference dbRefDrink = FirebaseDatabase.getInstance().getReference().child(UserMail)
				.child("Dates").child(date).child("Cigarettes");
		final List<Integer> ciggaretesCount = new LinkedList<>();
		final CountDownLatch doneCiggaretes = new CountDownLatch(1);
		dbRefDrink.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				final Integer count = s.getValue(Integer.class);
				if (count != null)
					ciggaretesCount.add(count);
				doneCiggaretes.countDown();
			}

			@Override
			public void onCancelled(final DatabaseError e) {
				System.out.println("The read failed: " + e.getCode());
			}
		});
		try {
			doneCiggaretes.await();
		} catch (final InterruptedException e) {
			// empty block
		}

		if (ciggaretesCount.isEmpty())
			return "0";
		else
			return ciggaretesCount.get(0).toString();
	}

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("WeeklyMailReport"));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		getUserInfo(i);
		openDatabase();
		String[] dates = getDates();
		for (int day = 0; day < 7; ++day) {
			DailyStatistics dailyStatistics = new DailyStatistics();
			dailyStatistics.cupsOfWater = getDrinkInfo(dates[day]);
			dailyStatistics.foodPortions = getFoodInfo(dates[day]);
			dailyStatistics.ciggaretesSmoked = getCiggaretsInfo(dates[day]);
			dailyStatistics.calculateDailyNutritions();
			this.weeklyStatistics.dailyStatistics.add(dailyStatistics);
		}
		this.weeklyStatistics.calculateWeeklyData();
		try {
			(new EmailSender()).designedWeeklyStatisticsEmail("Weekly Statistics", this.UserMail.replace("_dot_", "."),
					UserName, this.weeklyStatistics);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		weeklyStatistics = new WeeklyStatistics();
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", "Mail with weekly report Sent")
				.withSpeech("Mail with weekly report Sent ").withShouldEndSession(Boolean.FALSE).build();
	}

}
