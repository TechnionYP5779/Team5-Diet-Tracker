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
import Utils.Portion.Portion;
import Utils.Portion.Portion.Type;
import Utils.Strings;
import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.FoodsDB.FoodsDB;
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

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot foodSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.FOOD_SLOT),
				AmountSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
						.get(SlotString.AMOUNT_SLOT),
				UnitSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
						.get(SlotString.UNIT_SLOT);

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
		String UserEmail=i.getServiceClientFactory().getUpsService().getProfileEmail();
		

		// insert the portion to the DB
		try {
			final FoodsDB db =new FoodsDB();
			db.UserAte(UserEmail, added_food, Portion.toGrams(units, amount));
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