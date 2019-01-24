package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import Utils.Strings;
import Utils.Strings.HelpString;
import Utils.Strings.IntentsNames;

public class HelpIntentHandler implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.HELP_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, HelpString.SAY_HELP)
				.withSpeech(HelpString.SAY_HELP).withReprompt(HelpString.SAY_HELP_REPEAT)
				.withShouldEndSession(Boolean.FALSE).build();
	}
}