package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.services.Pair;

import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Portion;
import Utils.Portion.Type;
import Utils.Strings;
import Utils.Strings.DrinkStrings;
import Utils.Strings.IntentsNames;

public class HowManyIDrankIntent implements RequestHandler {
	public static final String DRINK_NAME_SLOT = "drink";

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.HOW_MUCH_DRANK_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot DrinkSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(DRINK_NAME_SLOT);
		String speechText = "";

		if (DrinkSlot == null)
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, DrinkStrings.TELL_DRINKS_AGAIN)
					.withSpeech(DrinkStrings.TELL_DRINKS_AGAIN).withReprompt(DrinkStrings.TELL_DRINKS_AGAIN_REPEAT)
					.withShouldEndSession(Boolean.FALSE).build();

		final String drink_name = DrinkSlot.getValue();

		// initialize database object with the user mail
		final DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());

		// retrieving the information
		Optional<Integer> Count = Optional.empty();
		try {
			if ("water".equals(drink_name))
				Count = db.DBGetTodayWaterCups();
			else if (drink_name.contains("coffee"))
				Count = db.DBGetTodayCofeeCups();
			else {
				final List<Pair<String, Portion>> Drinklist = db.DBGetTodayFoodList().stream()
						.filter(p -> p.getValue().getName().equals(drink_name) && p.getValue().getType() == Type.DRINK)
						.collect(Collectors.toList());
				int countOz = 0;
				for (final Pair<String, Portion> p : Drinklist)
					countOz += (int) p.getValue().getAmount();
				Count = Optional.ofNullable(Integer.valueOf(countOz / 30));
			}
		} catch (final DBException e) {
			// no need to do anything
		}

		if (!Count.isPresent() || Count.get().intValue() <= 0)
			speechText = String.format(DrinkStrings.DIDNT_DRINKED_TODAY, drink_name);
		else {
			final Integer count = Count.get();
			speechText = count.intValue() == 1 ? String.format(DrinkStrings.DRINKED_SO_FAR_ONE, drink_name)
					: String.format(DrinkStrings.DRINKED_SO_FAR_MANY, count, drink_name);

		}

		return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withReprompt(speechText).withShouldEndSession(Boolean.TRUE).build();

	}
}
