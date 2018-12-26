package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

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
import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import Utils.EmailSender;
import Utils.Portion;

public class SendDailyEmailHandler implements RequestHandler {

	String UserMail;
	String UserName;
	String cupsOfWater;
	List<Portion> foodPortions;
	String ciggaretesSmoked;
	String dailyCalories;
	String dailyProteins;
	String dailyCarbs;
	String dailyFats;

	private String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
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

	private void getDrinkInfo() {
		final DatabaseReference dbRefDrink = FirebaseDatabase.getInstance().getReference().child(UserMail)
				.child("Dates").child(getDate()).child("Drink");
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
			this.cupsOfWater = "0";
		else
			this.cupsOfWater = DrinkCount.get(0).toString();
	}

	private void getFoodInfo() {
		final DatabaseReference dbRefFood = FirebaseDatabase.getInstance().getReference().child(UserMail).child("Dates")
				.child(getDate()).child("Food");
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
		this.foodPortions = FoodList;
	}

	private void getCiggaretsInfo() {
		final DatabaseReference dbRefDrink = FirebaseDatabase.getInstance().getReference().child(UserMail)
				.child("Dates").child(getDate()).child("Cigarettes");
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
			this.ciggaretesSmoked = "0";
		else
			this.ciggaretesSmoked = ciggaretesCount.get(0).toString();
	}

	private void getCurrentDayTotalInfo() {
		// strings
		Double calories = 0.0, proteins = 0.0, carbs = 0.0, fats = 0.0;
		for (Portion portion : foodPortions) {
			calories += portion.amount * portion.calories_per_100_grams / 100;
			proteins += portion.amount * portion.proteins_per_100_grams / 100;
			carbs += portion.amount * portion.carbs_per_100_grams / 100;
			fats += portion.amount * portion.fats_per_100_grams / 100;
		}
		this.dailyCalories = calories.toString();
		this.dailyProteins = proteins.toString();
		this.dailyCarbs = carbs.toString();
		this.dailyFats = fats.toString();
	}

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("SendDailyMailIntent"));

	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		getUserInfo(i);
		openDatabase();
		getDrinkInfo();
		getFoodInfo();
		getCiggaretsInfo();
		getCurrentDayTotalInfo();

		try {
			(new EmailSender()).designedEmail("Daily Statistics", this.UserMail.replace("_dot_", "."), UserName,
					cupsOfWater, foodPortions, dailyCalories, dailyProteins, dailyCarbs, dailyFats, ciggaretesSmoked);
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", "Mail Sent").withSpeech("Mail Sent")
				.withShouldEndSession(Boolean.FALSE).build();
	}

}
