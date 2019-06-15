package Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Strings.CigarettesStrings;
import Utils.Strings.IntentsNames;

public class HowMuchSmokedIntentHandler implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.HOW_MUCH_SMOKE_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText = "";

		final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);

		Optional<Integer> smoked = Optional.empty();
		try {
			smoked = db.DBGetTodayCigarettesCount();
		} catch (DBException e) {
			// no need to do anything
		}

		if (!smoked.isPresent())
			speechText = CigarettesStrings.DIDNT_SMOKED;
		else {
			final Integer count = smoked.get();
			if (count.intValue() == 1)
				speechText = CigarettesStrings.SMOKED_ONE;
			else
				speechText = String.format(CigarettesStrings.SMOKED_MANY, count);

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}

}
