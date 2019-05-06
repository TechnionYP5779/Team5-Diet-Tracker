package Team5.Fitnesspeaker.AlexaCommunication.CustomMealHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import Utils.Strings;
import Utils.Strings.FoodStrings;
import Utils.Strings.IntentsNames;
import Utils.Strings.SlotString;

public class RemoveCustomMealIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName(IntentsNames.REMOVE_CUSTOM_MEAL_INTENT));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		final Slot mealSLot = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.CUSTOM_MEAL_SLOT);
		
		String speechText;
		final String repromptText = "";
		
		if (mealSLot == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.TELL_CUSTOM_MEAL_AGAIN)
					.withSpeech(FoodStrings.TELL_CUSTOM_MEAL_AGAIN).withReprompt(FoodStrings.TELL_CUSTOM_MEAL_AGAIN)
					.withShouldEndSession(Boolean.FALSE).build();
		
		//TODO check if custom meal already in users meals
			//TODO if not then tell him there is no such meal 
			
			//TODO if present then remove the meal

		return null;
	}

}
