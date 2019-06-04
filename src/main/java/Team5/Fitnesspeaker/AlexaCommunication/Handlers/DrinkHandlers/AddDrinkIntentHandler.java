package Team5.Fitnesspeaker.AlexaCommunication.Handlers.DrinkHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import java.util.Random;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.services.Pair;

import Utils.Portion.Portion;
import Utils.Portion.PortionRequestGen;
import Utils.Portion.Portion.Type;
import Utils.PortionSearchEngine.SearchResults;
import Utils.PortionSearchEngine;
import Utils.Strings;
import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Strings.DrinkStrings;
import Utils.Strings.FoodStrings;
import Utils.Strings.IntentsNames;
import Utils.Strings.SlotString;

/** this class handles drink recording
 * @author Shalev Kuba
 * @since 2018-12-07
 * */
public class AddDrinkIntentHandler implements RequestHandler {

	public static final String[] tips = { DrinkStrings.SITTING_TIP };
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.ADD_DRINK_INTENT));
	}
	
	public static String addDrinkToDB(Slot amountSlot, Slot unitSlot, Slot foodSlot,DBUtils db) throws Exception,DBException {
		Integer amountAux;
		if (amountSlot.getValue() == null) {
			amountAux = Integer.valueOf(1);
		}else {
			amountAux = Integer.valueOf(Integer.parseInt(amountSlot.getValue()));
		}
		final Integer amount = amountAux;
		final String units = unitSlot.getValue(), added_food = foodSlot.getValue();
		
		//add food
		try {
			Pair<SearchResults, Portion> p=PortionSearchEngine.PortionSearch
					(added_food, units, Type.FOOD, Double.valueOf(amount.intValue()).doubleValue(),db.DBGetEmail());
			 /*if there was a search error, i.e. the food wasn't found, notify the user to about
			 * the option of custom meal
			 **/
			if(p.getName() == SearchResults.SEARCH_NO_RESULTS || p.getName() == SearchResults.SEARCH_ERROR) {
				return String.format(FoodStrings.FOOD_NOT_FOUND,added_food,added_food);
			} else {
				db.DBPushFood(p.getValue());

			}
		}catch (final Exception | DBException e) {
			return String.format(FoodStrings.FOOD_NOT_FOUND,added_food,added_food);
		}
		return String.format(FoodStrings.FOOD_LOGGED, amount, units, added_food);
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.ADD_COUNT_SLOT),
				DrinkSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
						.get(SlotString.DRINK_NAME_SLOT);
		String speechText, repromptText = "";

		if (NumberSlot == null)
			return i.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, DrinkStrings.TELL_DRINKS_AMOUNT_AGAIN)
					.withSpeech(DrinkStrings.TELL_DRINKS_AMOUNT_AGAIN)
					.withReprompt(DrinkStrings.TELL_DRINKS_AMOUNT_AGAIN_REPEAT).withShouldEndSession(Boolean.FALSE)
					.build();

		if (DrinkSlot == null)
			return i.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText = DrinkStrings.TELL_DRINKS_AGAIN)
					.withSpeech(speechText = DrinkStrings.TELL_DRINKS_AGAIN)
					.withReprompt(repromptText = DrinkStrings.TELL_DRINKS_AGAIN_REPEAT)
					.withShouldEndSession(Boolean.FALSE).build();

		final int added_num_of_cups = Integer.parseInt(NumberSlot.getValue());
		final String drink_name = DrinkSlot.getValue();
		// initialize database object with the user mail
		final DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());

		// log the drinking
		try {
			if ("water".equals(drink_name))
				db.DBAddWaterCups(Integer.valueOf(added_num_of_cups));
			else {
				db.DBPushFood(PortionRequestGen.generatePortionWithAmount(drink_name, Type.DRINK,
						Double.valueOf(240.0 * added_num_of_cups).doubleValue(), "grams"));
				if (drink_name.contains("coffee"))
					db.DBAddCoffeeCups(Integer.valueOf(added_num_of_cups));
			}
		} catch (final DBException e) {
			return i.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, DrinkStrings.DRINKS_LOGGING_PROBLEM)
					.withSpeech(DrinkStrings.DRINKS_LOGGING_PROBLEM)
					.withReprompt(DrinkStrings.DRINKS_LOGGING_PROBLEM_REPEAT).withShouldEndSession(Boolean.FALSE)
					.build();
		} catch (Exception e) {
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_UNITS_PROBLEM)
					.withSpeech(FoodStrings.FOOD_UNITS_PROBLEM).withReprompt(FoodStrings.FOOD_UNITS_PROBLEM_REPEAT)
					.withShouldEndSession(Boolean.FALSE).build();
		}

		speechText = added_num_of_cups == 1 ? String.format(DrinkStrings.ONE_DRINK_LOGGED, drink_name)
				: String.format(DrinkStrings.MANY_DRINKS_LOGGED, Integer.valueOf(added_num_of_cups), drink_name);

		final Random rand = new Random();
		if (rand.nextInt(6) != 2)
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();

		speechText += String.format(tips[rand.nextInt(tips.length)]);
		return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();
	}

}