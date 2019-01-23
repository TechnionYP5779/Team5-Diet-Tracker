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

public class DrinkAlchoholHandler implements RequestHandler {

	private double getAlcoholPrecentage(final String alcoholDrink) {
		switch (alcoholDrink) {
		case "beer":
			return 5.0;
		case "wine":
			return 12.0;
		case "vodka":
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
		case "whiskey":
			return 30.0;
		default:
			return 0.0;
		}
	}

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName(Utils.Strings.IntentsNames.DRINK_ALCOHOL_INTENT));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		final String alcoholName = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get("alchohol").getValue();
		final double alcoholPrecentage = getAlcoholPrecentage(alcoholName),
				alcoholAmount = getAlcoholAmount(alcoholName);
		if (alcoholPrecentage == 0)
			return i.getResponseBuilder()
					.withSimpleCard("FitnessSpeakerSession", Utils.Strings.AlcoholStrings.NOT_ALCOHOL)
					.withSpeech(Utils.Strings.AlcoholStrings.NOT_ALCOHOL).withShouldEndSession(Boolean.FALSE).build();

		String speechText = "", repromptText;
		final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail();
		Portion p = new Portion(Portion.Type.DRINK, alcoholName, alcoholAmount, 0, 0, 0, 0, alcoholPrecentage);
		try {
			new DBUtils(UserMail).DBPushAlcohol(p);
		} catch (DBException e) {
			// e.printStackTrace();
		}
		speechText = String.format(Utils.Strings.AlcoholStrings.CHEERS, alcoholName);
		repromptText = Utils.Strings.AlcoholStrings.DRINK_ALCOHOL;

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}

}
