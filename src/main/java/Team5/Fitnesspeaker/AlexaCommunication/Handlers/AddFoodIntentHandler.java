package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import java.util.Random;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Portion.Type;
import Utils.PortionRequestGen;
import Utils.PortionSearchEngine;
import Utils.Strings;
import Utils.Strings.FoodStrings;
import Utils.Strings.IntentsNames;

/** this class handles food recording
 * @author Shalev Kuba
 * @since 2018-12-07
 * */
public class AddFoodIntentHandler implements RequestHandler {
	public static final String AMOUNT_SLOT = "Number";
	public static final String FOOD_SLOT = "Food";
	public static final String UNIT_SLOT = "Unit";
	public static final String[] tips = { FoodStrings.SITTING_TIP, FoodStrings.SLOWLY_TIP, FoodStrings.DRINK_LIQUID_TIP,
			FoodStrings.WATER_BEFORE_MEAL_TIP };

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.ADD_FOOD_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot foodSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().get(FOOD_SLOT),
				AmountSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().get(AMOUNT_SLOT),
				UnitSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().get(UNIT_SLOT);

		String speechText;
		final String repromptText = "";

		if (foodSlot == null)
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.TELL_FOOD_AGAIN)
					.withSpeech(FoodStrings.TELL_FOOD_AGAIN).withReprompt(FoodStrings.TELL_FOOD_AGAIN_REPEAT)
					.withShouldEndSession(Boolean.FALSE).build();

		if (AmountSlot == null)
			return i.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.TELL_FOOD_AMOUNT_AGAIN)
					.withSpeech(FoodStrings.TELL_FOOD_AMOUNT_AGAIN)
					.withReprompt(FoodStrings.TELL_FOOD_AMOUNT_AGAIN_REPEAT).withShouldEndSession(Boolean.FALSE)
					.build();

		if (UnitSlot == null)
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.TELL_FOOD_UNITS_AGAIN)
					.withSpeech(FoodStrings.TELL_FOOD_UNITS_AGAIN)
					.withReprompt(FoodStrings.TELL_FOOD_UNITS_AGAIN_REPEAT).withShouldEndSession(Boolean.FALSE).build();

		final Integer amount = Integer.valueOf(Integer.parseInt(AmountSlot.getValue()));
		final String units = UnitSlot.getValue(), added_food = foodSlot.getValue();
		// initialize database object with the user mail
		final DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());

		// insert the portion to the DB
		try {
//			db.DBPushFood(PortionRequestGen.generatePortionWithAmount(added_food, Type.FOOD,
//					Double.valueOf(amount.intValue()).doubleValue(), units));
			PortionSearchEngine.PortionSearch
					(added_food, units, Type.FOOD, Double.valueOf(amount.intValue()).doubleValue(), i.getServiceClientFactory().getUpsService().getProfileEmail());
//		} catch (final DBException e) {
//			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_LOGGING_PROBLEM)
//					.withSpeech(FoodStrings.FOOD_LOGGING_PROBLEM).withReprompt(FoodStrings.FOOD_LOGGING_PROBLEM_REPEAT)
//					.withShouldEndSession(Boolean.FALSE).build();
			/**
			 * right now, the only other specific option we take care of is the option that
			 * we didn't find the portion units in the DB or in our modules.
			 */
		} catch (final Exception e) {
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_UNITS_PROBLEM)
					.withSpeech(FoodStrings.FOOD_UNITS_PROBLEM).withReprompt(FoodStrings.FOOD_UNITS_PROBLEM_REPEAT)
					.withShouldEndSession(Boolean.FALSE).build();
		}

		speechText = String.format(FoodStrings.FOOD_LOGGED, amount, units, added_food);

		final Random rand = new Random();
		if (rand.nextInt(6) == 2)
			speechText += String.format(tips[rand.nextInt(tips.length)]);

		// the Boolean.TRUE says that the Alexa will end the session
		return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();
	}

}