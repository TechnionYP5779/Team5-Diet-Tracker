package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import Utils.DBUtils;
import Utils.DBUtils.DBException;

public class HowManyIDrankIntent implements RequestHandler {

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("HowMuchIDrankIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		
		String speechText = "";
		
		// initialize database object with the user mail
		DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());
		
		//retrieving the information
		Optional<Integer> waterCount = Optional.empty();
		try {
			waterCount=db.DBGetTodayWaterCups();
		} catch (DBException e) {
			// no need to do anything
		}

		if ((!waterCount.isPresent())||waterCount.get().intValue()<=0)
			speechText = String.format("You haven't drink anything today yet");
		else {
			final Integer count = waterCount.get();
			if (count.intValue() == 1)
				speechText = String.format("so far, you have drank a single cup of water");
			else
				speechText = String.format("so far, you have drank %d cups of water", count);

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText).withReprompt(speechText)
				.withShouldEndSession(Boolean.TRUE).build();

	}
}
