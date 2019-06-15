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
import Utils.Portion.Portion.Type;
import Utils.PortionSearchEngine.SearchResults;
import Utils.PortionSearchEngine;
import Utils.Strings;
import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Strings.DrinkStrings;
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
	
	public static Pair<String, SearchResults> addDrinkToDB(Slot amountSlot, Slot unitSlot, Slot drinkSlot,DBUtils db) throws Exception,DBException {
		Integer amountAux;
		if (amountSlot.getValue() == null || amountSlot.getValue().equals("0")) {
			amountAux = Integer.valueOf(1);
		}else {
			amountAux = Integer.valueOf(Integer.parseInt(amountSlot.getValue()));
		}
		final String units = unitSlot.getValue(), added_drink = drinkSlot.getValue();
		final Integer amount = amountAux;
		String munit=units;
		if(units!=null&&amount.intValue()>1&&(!units.contains("grams")))
			munit = "s".equals(units.substring(units.length() - 1)) ? (String) units.subSequence(0, units.length()-1) : units;
		final String unit = munit;
		
		//add food
		try {
			Pair<SearchResults, Portion> p=PortionSearchEngine.PortionSearch
					(added_drink, unit, Type.DRINK, amount.intValue(),db.DBGetEmail());
			 /*if there was a search error, i.e. the food wasn't found, notify the user to about
			 * the option of custom meal
			 **/
			if(p.getName() == SearchResults.SEARCH_NO_RESULTS || p.getName() == SearchResults.SEARCH_ERROR) {
				return new Pair<String,SearchResults>(String.format(DrinkStrings.DRINK_NOT_FOUND,added_drink,added_drink), SearchResults.SEARCH_NO_RESULTS);
			} else {
				Portion portionToPush=p.getValue();
				portionToPush.name=portionToPush.name.replace("%20", " ");
				portionToPush.units=units;
				db.DBPushFood(portionToPush);

			}
		}catch (final Exception | DBException e) {
			return new Pair<String,SearchResults>(String.format(DrinkStrings.DRINK_NOT_FOUND,added_drink,added_drink),SearchResults.SEARCH_ERROR);
		}
		if(unitSlot.getValue() == null)
			return new Pair<String, SearchResults>(String.format(DrinkStrings.DRINK_LOGGED, amount, " ", added_drink),SearchResults.SEARCH_FOUND);
		return new Pair<String, SearchResults>(String.format(DrinkStrings.DRINK_LOGGED, amount, units, added_drink),SearchResults.SEARCH_FOUND);
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot drinkSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.DRINK_NAME_SLOT),
				amountSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
						.get(SlotString.ADD_COUNT_SLOT),
				unitSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
						.get(SlotString.UNIT_SLOT);
		
		String speechText="";
		final String repromptText = "";
		
		if (drinkSlot.getValue() == null)
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, DrinkStrings.TELL_DRINK_AGAIN)
					.withSpeech(DrinkStrings.TELL_DRINK_AGAIN).withReprompt(DrinkStrings.TELL_DRINK_AGAIN)
					.withShouldEndSession(Boolean.FALSE).build();
		
		// initialize database object with the user mail
		final DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());
		
		try {
			Pair<String,SearchResults> p =addDrinkToDB(amountSlot,unitSlot,drinkSlot,db);
			if(p.getValue()==SearchResults.SEARCH_FOUND) {
				speechText += p.getName();
			}else {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, String.format(DrinkStrings.DRINK_NOT_FOUND,drinkSlot.getValue()))
						.withSpeech(String.format(DrinkStrings.DRINK_NOT_FOUND,drinkSlot.getValue())).withReprompt(String.format(DrinkStrings.DRINK_NOT_FOUND,drinkSlot.getValue()))
						.withShouldEndSession(Boolean.FALSE).build();
			}
		} catch (final DBException e) {
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, DrinkStrings.DRINK_LOGGING_PROBLEM)
					.withSpeech(DrinkStrings.DRINK_LOGGING_PROBLEM).withReprompt(DrinkStrings.DRINK_LOGGING_PROBLEM)
					.withShouldEndSession(Boolean.FALSE).build();
			/**
			 * right now, the only other specific option we take care of is the option that
			 * we didn't find the portion units in the DB or in our modules.
			 */
		} catch (final Exception e) {
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, DrinkStrings.DRINK_UNITS_PROBLEM)
					.withSpeech(DrinkStrings.DRINK_UNITS_PROBLEM).withReprompt(DrinkStrings.DRINK_UNITS_PROBLEM)
					.withShouldEndSession(Boolean.FALSE).build();
		}
		speechText = String.format(DrinkStrings.DRINKS_LOGGED_START)+speechText+ String.format(DrinkStrings.DRINK_LOGGED_END);

		final Random rand = new Random();
		if (rand.nextInt(6) != 2)
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();

		speechText += String.format(tips[rand.nextInt(tips.length)]);
		return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();
		/*
		
		
		
		
		
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
				.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();*/
	}

}