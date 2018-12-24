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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import Utils.EmailSender;
import Utils.Portion;

public class SendEmailHandler implements RequestHandler {

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("SendMailIntent"));

	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		final String UserMail = "shalev@gmail";
		String mailToSend = "";
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
		// Get Drink
		final DatabaseReference dbRefDrink = FirebaseDatabase.getInstance().getReference().child(UserMail)
				.child("Drink");
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
			// TODO Auto-generated catch block
		}

		if (DrinkCount.isEmpty())
			mailToSend += String.format("You didn't drink today\n");
		else {
			final Integer count = DrinkCount.get(0);
			if (count.intValue() == 1)
				mailToSend += String.format("You drank one glass of water\n");
			else
				mailToSend += String.format("You drank %d glasses of water\n", count);
		}

		// Get Food
		final DatabaseReference dbRefFood = FirebaseDatabase.getInstance().getReference().child(UserMail).child("Food");
		final List<Portion> FoodList = new LinkedList<>();
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
			// TODO Auto-generated catch block
		}
		String foods_eaten = "";
		for (final Portion p : FoodList)
			foods_eaten += p.getName() + " " + Integer.valueOf((int) p.getAmount()) + " grams \n";
		if (!foods_eaten.isEmpty())
			foods_eaten = String.format("You ate: %s.\n", foods_eaten);
		else
			foods_eaten = "You ate nothing.\n";

		mailToSend += foods_eaten;

		try {
			(new EmailSender()).sendMail(mailToSend, "FitnessSpeaker - status", "igor731996@gmail.com");
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", "Mail Sent").withSpeech("Mail Sent")
				.withShouldEndSession(Boolean.FALSE).build();
	}

}