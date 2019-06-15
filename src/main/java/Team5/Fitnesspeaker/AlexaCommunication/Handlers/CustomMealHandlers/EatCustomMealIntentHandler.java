package Team5.Fitnesspeaker.AlexaCommunication.Handlers.CustomMealHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import java.util.Random;

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
import Utils.Strings.FoodStrings;
import Utils.Strings.IntentsNames;
import Utils.Strings.SlotString;

public class EatCustomMealIntentHandler implements RequestHandler {

	public static final String[] tips = { FoodStrings.SITTING_TIP, FoodStrings.SLOWLY_TIP, FoodStrings.DRINK_LIQUID_TIP,
			FoodStrings.WATER_BEFORE_MEAL_TIP };

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName(IntentsNames.EAT_CUSTOM_MEAL_INTENT));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		final Slot mealSlot = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.CUSTOM_MEAL_SLOT);
		final Slot numberSlot = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.NUMBER_SLOT);

		if (mealSlot == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN)
					.withSpeech(CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN)
					.withReprompt(CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN).withShouldEndSession(Boolean.FALSE).build();

		if (numberSlot == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.TELL_FOOD_AMOUNT_AGAIN)
					.withSpeech(FoodStrings.TELL_FOOD_AMOUNT_AGAIN)
					.withReprompt(FoodStrings.TELL_FOOD_AMOUNT_AGAIN_REPEAT).withShouldEndSession(Boolean.FALSE)
					.build();

		final String meal = mealSlot.getValue();
		final Integer amount = Integer.valueOf(Integer.parseInt(numberSlot.getValue()));

		if (meal == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN)
					.withSpeech(CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN)
					.withReprompt(CustomMealStrings.TELL_CUSTOM_MEAL_AGAIN).withShouldEndSession(Boolean.FALSE).build();

		if (amount == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.TELL_FOOD_AMOUNT_AGAIN)
					.withSpeech(FoodStrings.TELL_FOOD_AMOUNT_AGAIN)
					.withReprompt(FoodStrings.TELL_FOOD_AMOUNT_AGAIN_REPEAT).withShouldEndSession(Boolean.FALSE)
					.build();

		final DBUtils db = new DBUtils(input.getServiceClientFactory().getUpsService().getProfileEmail());
		String speechText;
		final String repromptText = "";

		try {
			try {
				CustomMeal customaMeal = db.DBGetCustomMeal(meal);
				if (customaMeal == null) {
					return input.getResponseBuilder()
							.withSimpleCard(Strings.GLOBAL_SESSION_NAME, CustomMealStrings.CUSTOM_MEAL_DOESNT_EXISTS)
							.withSpeech(CustomMealStrings.CUSTOM_MEAL_DOESNT_EXISTS)
							.withReprompt(CustomMealStrings.CUSTOM_MEAL_DOESNT_EXISTS)
							.withShouldEndSession(Boolean.FALSE).build();
				}
				db.DBPushFood(customaMeal.toPortion(amount.intValue()));
			} catch (DBException e) {
				return input.getResponseBuilder()
						.withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_LOGGING_PROBLEM)
						.withSpeech(FoodStrings.FOOD_LOGGING_PROBLEM)
						.withReprompt(FoodStrings.FOOD_LOGGING_PROBLEM_REPEAT).withShouldEndSession(Boolean.FALSE)
						.build();
			}
		} catch (final Exception e) {
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_LOGGING_PROBLEM)
					.withSpeech(FoodStrings.FOOD_LOGGING_PROBLEM).withReprompt(FoodStrings.FOOD_LOGGING_PROBLEM_REPEAT)
					.withShouldEndSession(Boolean.FALSE).build();
		}

		speechText = String.format(FoodStrings.FOOD_LOGGED, amount, "meals", meal);

		final Random rand = new Random();
		if (rand.nextInt(6) == 2)
			speechText += String.format(tips[rand.nextInt(tips.length)]);

		// the Boolean.TRUE says that the Alexa will end the session
		return input.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();

	}

}
