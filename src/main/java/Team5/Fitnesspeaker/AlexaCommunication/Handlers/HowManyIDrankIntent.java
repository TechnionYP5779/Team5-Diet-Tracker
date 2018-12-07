package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

public class HowManyIDrankIntent implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("HowMuchIDrankIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText;
		Map<String, Object> items = new TreeMap<String, Object>(
				i.getAttributesManager().getSessionAttributes());
		Integer count =items.get("Drink")==null? Integer.valueOf(0):(Integer)items.get("Drink");
		if (count.intValue()>0)
			if (count.intValue() == 1)
				speechText = String.format("you drank one glass of water");
			else
				speechText = String.format("you drank %d glasses of water", count);
		else
			speechText = "you did not drink so far";

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
