package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import Utils.Strings;
import Utils.Strings.FallbackString;
import Utils.Strings.IntentsNames;

public class FallbackIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.FALL_BACK_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		return i.getResponseBuilder().withSpeech(FallbackString.TRY_HELP)
				.withSimpleCard(Strings.GLOBAL_SESSION_NAME, FallbackString.TRY_HELP)
				.withReprompt(FallbackString.TRY_HELP).build();
	}

}