package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;
import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Portion;

@SuppressWarnings("static-method")
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
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", "This is not an alcoholic drink")
					.withSpeech("This is not an alcoholic drink").withShouldEndSession(Boolean.FALSE).build();

		String speechText = "", repromptText;
		final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail();
		Portion p = new Portion(Portion.Type.DRINK, alcoholName, alcoholAmount, 0, 0, 0, 0, alcoholPrecentage);
		try {
			new DBUtils(UserMail).DBPushAlcohol(p);
		} catch (DBException e) {
			// e.printStackTrace();
		}
		speechText = String.format("Cheers! you drank %s", alcoholName);
		repromptText = "You can drink again saying cheers and the alcoholic drink name";

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}

}
