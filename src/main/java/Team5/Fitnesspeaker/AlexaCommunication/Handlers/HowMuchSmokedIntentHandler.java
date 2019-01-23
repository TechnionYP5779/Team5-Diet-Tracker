package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import Utils.DBUtils;
import Utils.DBUtils.DBException;

public class HowMuchSmokedIntentHandler implements RequestHandler{
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("HowMuchSmokedIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText = "";

		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);

		Optional<Integer> smoked =  Optional.empty();
		try {
			smoked = db.DBGetTodayCigarettesCount();
		} catch (DBException e) {
			// no need to do anything
		}

		if (!smoked.isPresent())
			speechText = String.format("you did not smoke today, Well Done");
		else {
			final Integer count = smoked.get();
			if (count.intValue() == 1)
				speechText = String.format("you have already smoked one cigarette today");
			else
				speechText = String.format("you have already smoked %d cigarettes today ", count);

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
	
}
