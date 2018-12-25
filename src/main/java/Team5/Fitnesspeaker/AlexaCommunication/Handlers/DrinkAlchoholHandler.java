package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Utils.Portion;

public class DrinkAlchoholHandler implements RequestHandler {

	private double getAlcoholPrecentage(final String alcoholDrink) {
		switch (alcoholDrink) {
		case "beer":
			return 5.0;
		case "wine":
			return 12.0;
		case "vodka":
			return 40.0;
		case "whiskey":
			return 40.0;
		default:
			return 0.0;
		}
	}

	private double getAlcoholAmount(final String alcoholDrink) {
		switch (alcoholDrink) {
		case "beer":
			return 500.0;
		case "wine":
			return 175.0;
		case "vodka":
			return 30.0;
		case "whiskey":
			return 30.0;
		default:
			return 0.0;
		}
	}

	private static String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("DrinkAlchohol"));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		final String alcoholName = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get("alchohol").getValue();
		final double alcoholPrecentage = getAlcoholPrecentage(alcoholName);
		final double alcoholAmount = getAlcoholAmount(alcoholName);
		if (alcoholPrecentage == 0)
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", "This is not an alchohol drink")
					.withSpeech("This is not an alchohol drink").withShouldEndSession(Boolean.FALSE).build();

		String speechText = "", repromptText;
		final String UserMail = "shalev@gmail";
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
				dbRef = database.getReference().child(UserMail).child("Dates").child(getDate()).child("Alcohol");
		} catch (final Exception e) {
			speechText += e.getMessage() + " ";// its ok
		}

		try {
			if (dbRef != null)
				dbRef.push().setValueAsync(
						new Portion(Portion.Type.DRINK, alcoholName, alcoholAmount, 0, 0, 0, 0, alcoholPrecentage))
						.get();
		} catch (InterruptedException | ExecutionException e) {
			speechText += e.getMessage() + " ";
		}

		speechText = String.format("Cheers! you drank %s", alcoholName);
		repromptText = "You can drink again with saying cheers and the alchohol drink name";

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}

}
