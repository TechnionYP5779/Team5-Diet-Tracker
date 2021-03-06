package Team5.Fitnesspeaker.AlexaCommunication.Handlers.CustomMealHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import Utils.CustomMeal;
import Utils.Strings;
import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Strings.CustomMealStrings;
import Utils.Strings.IntentsNames;
import Utils.Strings.SlotString;

public class AddCustomMealIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName(IntentsNames.ADD_CUSTOM_MEAL_INTENT));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		final Slot mealSLot = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.CUSTOM_MEAL_SLOT);

		if (mealSLot == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN)
					.withSpeech(CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN)
					.withReprompt(CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN).withShouldEndSession(Boolean.FALSE).build();

		String speechText;
		final String customMeal = mealSLot.getValue();
		final DBUtils db = new DBUtils(input.getServiceClientFactory().getUpsService().getProfileEmail());

		try {
			try {
				if (db.DBGetCustomMeals().containsKey(customMeal)) {
					speechText = CustomMealStrings.CUSTOM_MEAL_EXISTS;
				} else {
					CustomMeal meal = new CustomMeal(customMeal);
					db.DBPushCustomMeal(meal);
					speechText = CustomMealStrings.CUSTOM_MEAL_LOGGED;
				}
			} catch (DBException e) {
				return input.getResponseBuilder()
						.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.CUSTOM_MEAL_ADDING_ERROR)
						.withSpeech(CustomMealStrings.CUSTOM_MEAL_ADDING_ERROR)
						.withReprompt(CustomMealStrings.CUSTOM_MEAL_ADDING_ERROR).withShouldEndSession(Boolean.FALSE)
						.build();
			}
		} catch (Exception e) {
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.CUSTOM_MEAL_ADDING_ERROR)
					.withSpeech(CustomMealStrings.CUSTOM_MEAL_ADDING_ERROR)
					.withReprompt(CustomMealStrings.CUSTOM_MEAL_ADDING_ERROR).withShouldEndSession(Boolean.FALSE)
					.build();
		}
		return input.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withReprompt(speechText).withShouldEndSession(Boolean.FALSE).build();
	}

}
