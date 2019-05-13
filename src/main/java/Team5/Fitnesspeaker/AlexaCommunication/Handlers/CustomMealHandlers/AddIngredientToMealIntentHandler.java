package Team5.Fitnesspeaker.AlexaCommunication.Handlers.CustomMealHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import Utils.Strings.IntentsNames;

public class AddIngredientToMealIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName(IntentsNames.ADD_INGREDIENT_MEAL_INTENT));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		// TODO get the input
		// TODO check input correctness

		// TODO check if meal exists
			// TODO if not tell the user
			// TODO if exists
				// TODO create right portion
				// TODO update the meal
				// TODO inform the user the meal updated with given ingredient
		return null;
	}

}
