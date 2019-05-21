package Team5.Fitnesspeaker.AlexaCommunication.Handlers.FoodHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import java.util.Random;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import Utils.Portion.PortionRequestGen;
import Utils.Portion.Portion.Type;
import Utils.Strings;
import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Strings.FoodStrings;
import Utils.Strings.IntentsNames;
import Utils.Strings.SlotString;

/**
 * this class handles food recording
 * 
 * @author Shalev Kuba
 * @since 2018-12-07
 */
public class AddFoodIntentHandler implements RequestHandler {

	public static final String[] tips = { FoodStrings.SITTING_TIP, FoodStrings.SLOWLY_TIP, FoodStrings.DRINK_LIQUID_TIP,
			FoodStrings.WATER_BEFORE_MEAL_TIP };

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.ADD_FOOD_INTENT));
	}
	
	public static String addFoodToDB(Slot amountSlot, Slot unitSlot, Slot foodSlot,DBUtils db) throws Exception,DBException {
		final Integer amount = Integer.valueOf(Integer.parseInt(amountSlot.getValue()));
		final String units = unitSlot.getValue(), added_food = foodSlot.getValue();
		db.DBPushFood(PortionRequestGen.generatePortionWithAmount(added_food, Type.FOOD,
				Double.valueOf(amount.intValue()).doubleValue(), units));
		return String.format(FoodStrings.FOOD_LOGGED, amount, units, added_food);
		
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot foodSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.FOOD_SLOT),
				amountSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
						.get(SlotString.AMOUNT_SLOT),
				unitSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
						.get(SlotString.UNIT_SLOT),
				foodSlot2 = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
						.get(SlotString.FOOD_SLOT2),
				amountSlot2 = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
								.get(SlotString.AMOUNT_SLOT2),
				unitSlot2 = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
								.get(SlotString.UNIT_SLOT2),
				foodSlot3 = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
								.get(SlotString.FOOD_SLOT3),
				amountSlot3 = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
								.get(SlotString.AMOUNT_SLOT3),
				unitSlot3 = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
								.get(SlotString.UNIT_SLOT3),
				foodSlot4 = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
								.get(SlotString.FOOD_SLOT4),
				amountSlot4 = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
								.get(SlotString.AMOUNT_SLOT4),
				unitSlot4 = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
								.get(SlotString.UNIT_SLOT4);
				

		String speechText = String.format(FoodStrings.FOOD_LOGGED_START);
		final String repromptText = "";
		
		if (foodSlot.getValue() == null)
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.TELL_FOOD_AGAIN)
					.withSpeech(FoodStrings.TELL_FOOD_AGAIN).withReprompt(FoodStrings.TELL_FOOD_AGAIN_REPEAT)
					.withShouldEndSession(Boolean.FALSE).build();

		if (amountSlot.getValue() == null)
			return i.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.TELL_FOOD_AMOUNT_AGAIN)
					.withSpeech(FoodStrings.TELL_FOOD_AMOUNT_AGAIN)
					.withReprompt(FoodStrings.TELL_FOOD_AMOUNT_AGAIN_REPEAT).withShouldEndSession(Boolean.FALSE)
					.build();

		if (unitSlot.getValue() == null)
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.TELL_FOOD_UNITS_AGAIN)
					.withSpeech(FoodStrings.TELL_FOOD_UNITS_AGAIN)
					.withReprompt(FoodStrings.TELL_FOOD_UNITS_AGAIN_REPEAT).withShouldEndSession(Boolean.FALSE).build();

		// initialize database object with the user mail
		final DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());
		
		final Integer amount = Integer.valueOf(Integer.parseInt(amountSlot.getValue()));
		final String units = unitSlot.getValue(), added_food = foodSlot.getValue();

		// insert the portion to the DB
		try {
			db.DBPushFood(PortionRequestGen.generatePortionWithAmount(added_food, Type.FOOD,
					Double.valueOf(amount.intValue()).doubleValue(), units));
		} catch (final DBException e) {
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_LOGGING_PROBLEM)
					.withSpeech(FoodStrings.FOOD_LOGGING_PROBLEM).withReprompt(FoodStrings.FOOD_LOGGING_PROBLEM_REPEAT)
					.withShouldEndSession(Boolean.FALSE).build();
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
		
		// now we will enter the second food if we got it
		
		if(foodSlot2.getValue()!= null) {
			final Integer amount2 = Integer.valueOf(Integer.parseInt(amountSlot2.getValue()));
			final String units2 = unitSlot2.getValue(), added_food2 = foodSlot2.getValue();

			// insert the portion to the DB
			try {
				db.DBPushFood(PortionRequestGen.generatePortionWithAmount(added_food2, Type.FOOD,
						Double.valueOf(amount2.intValue()).doubleValue(), units2));
			} catch (final DBException e) {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_LOGGING_PROBLEM)
						.withSpeech(FoodStrings.FOOD_LOGGING_PROBLEM).withReprompt(FoodStrings.FOOD_LOGGING_PROBLEM_REPEAT)
						.withShouldEndSession(Boolean.FALSE).build();
				/**
				 * right now, the only other specific option we take care of is the option that
				 * we didn't find the portion units in the DB or in our modules.
				 */
			} catch (final Exception e) {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_UNITS_PROBLEM)
						.withSpeech(FoodStrings.FOOD_UNITS_PROBLEM).withReprompt(FoodStrings.FOOD_UNITS_PROBLEM_REPEAT)
						.withShouldEndSession(Boolean.FALSE).build();
			}

			speechText += String.format(FoodStrings.FOOD_LOGGED, amount2, units2, added_food2);
		}
		
		// now we will enter the third food if we got it
		if(foodSlot3.getValue()!= null) {
			try {
				speechText += addFoodToDB(amountSlot3,unitSlot3,foodSlot3,db);
			} catch (final DBException e) {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_LOGGING_PROBLEM)
						.withSpeech(FoodStrings.FOOD_LOGGING_PROBLEM).withReprompt(FoodStrings.FOOD_LOGGING_PROBLEM_REPEAT)
						.withShouldEndSession(Boolean.FALSE).build();
				/**
				 * right now, the only other specific option we take care of is the option that
				 * we didn't find the portion units in the DB or in our modules.
				 */
			} catch (final Exception e) {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_UNITS_PROBLEM)
						.withSpeech(FoodStrings.FOOD_UNITS_PROBLEM).withReprompt(FoodStrings.FOOD_UNITS_PROBLEM_REPEAT)
						.withShouldEndSession(Boolean.FALSE).build();
			}
		}
		
		// now we will enter the fourth food if we got it
		if(foodSlot4.getValue()!= null) {
			try {
				speechText += addFoodToDB(amountSlot4,unitSlot4,foodSlot4,db);
			} catch (final DBException e) {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_LOGGING_PROBLEM)
						.withSpeech(FoodStrings.FOOD_LOGGING_PROBLEM).withReprompt(FoodStrings.FOOD_LOGGING_PROBLEM_REPEAT)
						.withShouldEndSession(Boolean.FALSE).build();
				/**
				 * right now, the only other specific option we take care of is the option that
				 * we didn't find the portion units in the DB or in our modules.
				 */
			} catch (final Exception e) {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_UNITS_PROBLEM)
						.withSpeech(FoodStrings.FOOD_UNITS_PROBLEM).withReprompt(FoodStrings.FOOD_UNITS_PROBLEM_REPEAT)
						.withShouldEndSession(Boolean.FALSE).build();
			}
		}
		
		speechText += String.format(FoodStrings.FOOD_LOGGED_END);

		final Random rand = new Random();
		if (rand.nextInt(6) == 2)
			speechText += String.format(tips[rand.nextInt(tips.length)]);

		// the Boolean.TRUE says that the Alexa will end the session
		return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();
	}

}