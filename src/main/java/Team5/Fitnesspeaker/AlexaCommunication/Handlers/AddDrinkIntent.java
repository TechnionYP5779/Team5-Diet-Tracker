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
import Utils.Strings;
import Utils.Strings.DrinkStrings;
import Utils.Strings.IntentsNames;

public class AddDrinkIntent implements RequestHandler {
	public static final String ADD_COUNT_SLOT = "Number";
	public static final String DRINK_NAME_SLOT = "drink";
	public static final String[] tips = { DrinkStrings.SITTING_TIP };

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.ADD_DRINK_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(ADD_COUNT_SLOT),
				DrinkSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
						.get(DRINK_NAME_SLOT);
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
		}

		speechText = added_num_of_cups == 1 ? String.format(DrinkStrings.ONE_DRINKS_LOGGED, drink_name)
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