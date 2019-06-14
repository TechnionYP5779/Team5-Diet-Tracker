package Team5.Fitnesspeaker.AlexaCommunication.Handlers.FoodHandlers;

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
import Utils.PortionSearchEngine;
import Utils.PortionSearchEngine.SearchResults;
import Utils.Strings;
import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Strings.FoodStrings;
import Utils.Strings.IntentsNames;
import Utils.Strings.SlotString;

/** this class handles food recording
 * @author Shalev Kuba
 * @since 2018-12-07
 * */
public class AddFoodIntentHandler implements RequestHandler {
	public static final String[] tips = { FoodStrings.SITTING_TIP, FoodStrings.SLOWLY_TIP, FoodStrings.DRINK_LIQUID_TIP,
			FoodStrings.WATER_BEFORE_MEAL_TIP };

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.ADD_FOOD_INTENT));
	}
	
	public static Pair<String,SearchResults> addFoodToDB(Slot amountSlot, Slot unitSlot, Slot foodSlot,DBUtils db) throws Exception,DBException {
		Integer amountAux;
		if (amountSlot.getValue() == null || amountSlot.getValue().equals("0")) {
			amountAux = Integer.valueOf(1);
		}else {
			amountAux = Integer.valueOf(Integer.parseInt(amountSlot.getValue()));
		}
		final Integer amount = amountAux;
		final String added_food = foodSlot.getValue();
		final String units=unitSlot.getValue();
		
		
		//add food
		try {
			String munit=units;
			if(units!=null&&amount>1&&(!units.contains("grams")))
				munit = "s".equals(units.substring(units.length() - 1)) ? (String) units.subSequence(0, units.length()-1) : units;
			final String unit = munit;
			Pair<SearchResults, Portion> p=PortionSearchEngine.PortionSearch
					(added_food, unit, Type.FOOD, amount.intValue(),db.DBGetEmail());
			 /*if there was a search error, i.e. the food wasn't found, notify the user to about
			 * the option of custom meal
			 **/
			if(p.getName() == SearchResults.SEARCH_NO_RESULTS || p.getName() == SearchResults.SEARCH_ERROR) {
				return new Pair<String,SearchResults>(String.format(FoodStrings.FOOD_NOT_FOUND,added_food,added_food), SearchResults.SEARCH_NO_RESULTS);
			} else {
				Portion portionToPush=p.getValue();
				portionToPush.units=unit;
				db.DBPushFood(portionToPush);

			}
		}catch (final Exception | DBException e) {
			return new Pair<String,SearchResults>(String.format(FoodStrings.FOOD_NOT_FOUND,added_food,added_food),SearchResults.SEARCH_ERROR);
		}
		if(unitSlot.getValue() == null)
			return new Pair<String, SearchResults>(String.format(FoodStrings.FOOD_LOGGED, amount, " ", added_food),SearchResults.SEARCH_FOUND);
		return new Pair<String, SearchResults>(String.format(FoodStrings.FOOD_LOGGED, amount, units, added_food),SearchResults.SEARCH_FOUND);

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
		
//		String speechText = String.format(FoodStrings.FOOD_LOGGED_START);
		String speechText = "";
		final String repromptText = "";
		
		if (foodSlot.getValue() == null)
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.TELL_FOOD_AGAIN)
					.withSpeech(FoodStrings.TELL_FOOD_AGAIN).withReprompt(FoodStrings.TELL_FOOD_AGAIN_REPEAT)
					.withShouldEndSession(Boolean.FALSE).build();
		// initialize database object with the user mail
		final DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());
		
		try {
			Pair<String,SearchResults> p = addFoodToDB(amountSlot,unitSlot,foodSlot,db);
//			speechText += addFoodToDB(amountSlot,unitSlot,foodSlot,db);
			if(p.getValue()==SearchResults.SEARCH_FOUND) {
				speechText += p.getName();
			} else {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, String.format(FoodStrings.FOOD_NOT_FOUND,foodSlot.getValue(),foodSlot.getValue()))
						.withSpeech(String.format(FoodStrings.FOOD_NOT_FOUND,foodSlot.getValue(),foodSlot.getValue())).withReprompt(String.format(FoodStrings.FOOD_NOT_FOUND_REPEAT,foodSlot.getValue(),foodSlot.getValue()))
						.withShouldEndSession(Boolean.FALSE).build();			}
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
		/** if there wasn't any problem with the first portion, only now can we add the "you logged", 
		 * otherwise there is an option that we accidentally log an error message with the food
		 */
		String speechTextPrefix = String.format(FoodStrings.FOOD_LOGGED_START);
		speechText = speechTextPrefix + " "+speechText;
		// now we will enter the second food if we got it
		
		if(foodSlot2.getValue()!= null) {
			try {
				Pair<String,SearchResults> p = addFoodToDB(amountSlot2,unitSlot2,foodSlot2,db);
//				speechText += addFoodToDB(amountSlot,unitSlot,foodSlot,db);
				if(p.getValue()==SearchResults.SEARCH_FOUND) {
					speechText += p.getName();
				} else {
					return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, String.format(FoodStrings.FOOD_NOT_FOUND,foodSlot2.getValue(),foodSlot2.getValue()))
							.withSpeech(String.format(FoodStrings.FOOD_NOT_FOUND,foodSlot2.getValue(),foodSlot2.getValue())).withReprompt(String.format(FoodStrings.FOOD_NOT_FOUND_REPEAT,foodSlot2.getValue(),foodSlot2.getValue()))
							.withShouldEndSession(Boolean.FALSE).build();			}
				/**
				 * right now, the only other specific option we take care of is the option that
				 * we didn't find the portion units in the DB or in our modules.
				 */
			} catch (final Exception e) {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_UNITS_PROBLEM)
						.withSpeech(FoodStrings.FOOD_UNITS_PROBLEM).withReprompt(FoodStrings.FOOD_UNITS_PROBLEM_REPEAT)
						.withShouldEndSession(Boolean.FALSE).build();
			} catch (final DBException e) {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_LOGGING_PROBLEM)
						.withSpeech(FoodStrings.FOOD_LOGGING_PROBLEM).withReprompt(FoodStrings.FOOD_LOGGING_PROBLEM_REPEAT)
						.withShouldEndSession(Boolean.FALSE).build();
			}
		}
		
		// now we will enter the third food if we got it
		if(foodSlot3.getValue()!= null) {
			try {
				Pair<String,SearchResults> p = addFoodToDB(amountSlot3,unitSlot3,foodSlot3,db);
//				speechText += addFoodToDB(amountSlot,unitSlot,foodSlot,db);
				if(p.getValue()==SearchResults.SEARCH_FOUND) {
					speechText += p.getName();
				} else {
					return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, String.format(FoodStrings.FOOD_NOT_FOUND,foodSlot3.getValue(),foodSlot3.getValue()))
							.withSpeech(String.format(FoodStrings.FOOD_NOT_FOUND,foodSlot3.getValue(),foodSlot3.getValue())).withReprompt(String.format(FoodStrings.FOOD_NOT_FOUND_REPEAT,foodSlot3.getValue(),foodSlot3.getValue()))
							.withShouldEndSession(Boolean.FALSE).build();			}
				/**
				 * right now, the only other specific option we take care of is the option that
				 * we didn't find the portion units in the DB or in our modules.
				 */
			} catch (final Exception e) {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_UNITS_PROBLEM)
						.withSpeech(FoodStrings.FOOD_UNITS_PROBLEM).withReprompt(FoodStrings.FOOD_UNITS_PROBLEM_REPEAT)
						.withShouldEndSession(Boolean.FALSE).build();
			} catch (final DBException e) {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_LOGGING_PROBLEM)
						.withSpeech(FoodStrings.FOOD_LOGGING_PROBLEM).withReprompt(FoodStrings.FOOD_LOGGING_PROBLEM_REPEAT)
						.withShouldEndSession(Boolean.FALSE).build();
			}
		}
		
		// now we will enter the fourth food if we got it
		if(foodSlot4.getValue()!= null) {
			try {
				Pair<String,SearchResults> p = addFoodToDB(amountSlot4,unitSlot4,foodSlot4,db);
//				speechText += addFoodToDB(amountSlot,unitSlot,foodSlot,db);
				if(p.getValue()==SearchResults.SEARCH_FOUND) {
					speechText += p.getName();
				} else {
					return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, String.format(FoodStrings.FOOD_NOT_FOUND,foodSlot4.getValue(),foodSlot4.getValue()))
							.withSpeech(String.format(FoodStrings.FOOD_NOT_FOUND,foodSlot4.getValue(),foodSlot4.getValue())).withReprompt(String.format(FoodStrings.FOOD_NOT_FOUND_REPEAT,foodSlot4.getValue(),foodSlot4.getValue()))
							.withShouldEndSession(Boolean.FALSE).build();			}
				/**
				 * right now, the only other specific option we take care of is the option that
				 * we didn't find the portion units in the DB or in our modules.
				 */
			} catch (final Exception e) {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_UNITS_PROBLEM)
						.withSpeech(FoodStrings.FOOD_UNITS_PROBLEM).withReprompt(FoodStrings.FOOD_UNITS_PROBLEM_REPEAT)
						.withShouldEndSession(Boolean.FALSE).build();
			} catch (final DBException e) {
				return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, FoodStrings.FOOD_LOGGING_PROBLEM)
						.withSpeech(FoodStrings.FOOD_LOGGING_PROBLEM).withReprompt(FoodStrings.FOOD_LOGGING_PROBLEM_REPEAT)
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