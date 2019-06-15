package Team5.Fitnesspeaker.AlexaCommunication.Handlers.CustomMealHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import Utils.Strings;
import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Portion.PortionRequestGen;
import Utils.Portion.Portion;
import Utils.Portion.Portion.Type;
import Utils.Strings.CustomMealStrings;
import Utils.Strings.IntentsNames;
import Utils.Strings.SlotString;

public class AddIngredientToMealIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName(IntentsNames.ADD_INGREDIENT_MEAL_INTENT));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		final Slot mealSlot = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.CUSTOM_MEAL_SLOT);
		final Slot numberSlot = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.NUMBER_SLOT);
		final Slot unitSlot = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.UNIT_SLOT);
		final Slot ingrediantSlot = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.INGREDIANT_SLOT);

		if (mealSlot == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN)
					.withSpeech(CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN)
					.withReprompt(CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN).withShouldEndSession(Boolean.FALSE).build();

		if (numberSlot == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.TELL_INGREDIENT_AMOUNT_AGAIN)
					.withSpeech(CustomMealStrings.TELL_INGREDIENT_AMOUNT_AGAIN)
					.withReprompt(CustomMealStrings.TELL_INGREDIENT_AMOUNT_AGAIN).withShouldEndSession(Boolean.FALSE)
					.build();

		if (ingrediantSlot == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.TELL_INGREDIANT_AGAIN)
					.withSpeech(CustomMealStrings.TELL_INGREDIANT_AGAIN)
					.withReprompt(CustomMealStrings.TELL_INGREDIANT_AGAIN).withShouldEndSession(Boolean.FALSE).build();

		if (unitSlot == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.TELL_INGREDIENT_UNITS_AGAIN)
					.withSpeech(CustomMealStrings.TELL_INGREDIENT_UNITS_AGAIN)
					.withReprompt(CustomMealStrings.TELL_INGREDIENT_UNITS_AGAIN).withShouldEndSession(Boolean.FALSE).build();

		final String meal = mealSlot.getValue();
		final Integer amount = Integer.valueOf(Integer.parseInt(numberSlot.getValue()));
		final String ingrediant = ingrediantSlot.getValue();
		final String unit = unitSlot.getValue();
		
		if (meal == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN)
					.withSpeech(CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN)
					.withReprompt(CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN).withShouldEndSession(Boolean.FALSE).build();

		if (amount == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.TELL_INGREDIENT_AMOUNT_AGAIN)
					.withSpeech(CustomMealStrings.TELL_INGREDIENT_AMOUNT_AGAIN)
					.withReprompt(CustomMealStrings.TELL_INGREDIENT_AMOUNT_AGAIN).withShouldEndSession(Boolean.FALSE)
					.build();

		if (ingrediant == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.TELL_INGREDIANT_AGAIN)
					.withSpeech(CustomMealStrings.TELL_INGREDIANT_AGAIN)
					.withReprompt(CustomMealStrings.TELL_INGREDIANT_AGAIN).withShouldEndSession(Boolean.FALSE).build();

		if (unit == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.TELL_INGREDIENT_UNITS_AGAIN)
					.withSpeech(CustomMealStrings.TELL_INGREDIENT_UNITS_AGAIN)
					.withReprompt(CustomMealStrings.TELL_INGREDIENT_UNITS_AGAIN).withShouldEndSession(Boolean.FALSE).build();
		
		final DBUtils db = new DBUtils(input.getServiceClientFactory().getUpsService().getProfileEmail());

		Portion ingrediantPortion = PortionRequestGen.generatePortionWithAmount(ingrediant, Type.FOOD,
				Double.valueOf(amount.intValue()).doubleValue(), unit);

		try {
			if (!db.DBUpdateCustomMeal(meal, ingrediantPortion))
				return input.getResponseBuilder()
						.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.CUSTOM_MEAL_DOESNT_EXISTS)
						.withSpeech(CustomMealStrings.CUSTOM_MEAL_DOESNT_EXISTS)
						.withReprompt(CustomMealStrings.CUSTOM_MEAL_DOESNT_EXISTS).withShouldEndSession(Boolean.FALSE)
						.build();

			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.ADDED_INGREDIANT)
					.withSpeech(CustomMealStrings.ADDED_INGREDIANT).withReprompt(CustomMealStrings.ADDED_INGREDIANT)
					.withShouldEndSession(Boolean.FALSE).build();
		} catch (DBException e) {
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.CUSTOM_MEAL_UPDATE_ERROR)
					.withSpeech(CustomMealStrings.CUSTOM_MEAL_UPDATE_ERROR)
					.withReprompt(CustomMealStrings.CUSTOM_MEAL_UPDATE_ERROR).withShouldEndSession(Boolean.FALSE)
					.build();
		}
	}

}
