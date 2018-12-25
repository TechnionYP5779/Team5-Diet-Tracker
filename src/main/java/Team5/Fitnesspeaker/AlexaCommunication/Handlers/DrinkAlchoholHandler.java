package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static Team5.Fitnesspeaker.AlexaCommunication.Handlers.WhatIAteIntentHandler.FOOD_SLOT;
import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class DrinkAlchoholHandler implements RequestHandler{
	public static final String ALCHOHOL_SLOT = "alchohol";
	
	private double getAlchoholPrecentage(final String alchoholDrink) {
		switch (alchoholDrink) {
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
	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("DrinkAlchohol"));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		final Slot alchoholType = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(ALCHOHOL_SLOT);
		final String alchoholName = alchoholType.getValue();
		final double alchoholPrecentage = getAlchoholPrecentage(alchoholName);
		if(alchoholPrecentage == 0)
			return i.getResponseBuilder()
					.withSimpleCard("FitnessSpeakerSession", "This is not an alchohol drink")
					.withSpeech("This is not an alchohol drink").withShouldEndSession(Boolean.FALSE).build();
		
		
		return null;
	}

}
