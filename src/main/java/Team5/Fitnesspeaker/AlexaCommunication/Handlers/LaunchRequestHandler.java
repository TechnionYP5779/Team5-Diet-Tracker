package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.requestType;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Portion;
import Utils.Portion.Type;
import Utils.Strings;

public class LaunchRequestHandler implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(requestType(LaunchRequest.class));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {

		final OffsetDateTime CurrentTime = OffsetDateTime.now();
		final int hour = CurrentTime.getHour(), lastHour = getLastHour(i);
		String selected = ", i'm listening";
		final String partOfDay = getPartOfDay(hour), meal = getMealName(hour);

		if (lastHour < 0 || lastHour >= 0 && hour >= 4 && lastHour <= hour - 4)
			selected = ", please tell me what did you eat for " + meal;

		final String speechText = "good " + partOfDay + " "
				+ i.getServiceClientFactory().getUpsService().getProfileGivenName() + selected;

		return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withReprompt("I will repeat, " + speechText).build();
	}

	private static String getPartOfDay(final int hour) {
		return hour >= 10 && hour <= 16 ? "afternoon"
				: hour >= 19 && hour <= 23 || hour >= 0 && hour < 3 ? "night"
						: hour < 3 || hour >= 10 ? "evening" : "morning";
	}

	private static String getMealName(final int hour) {
		return hour >= 10 && hour <= 16 ? "lunch"
				: hour >= 19 && hour <= 23 || hour >= 0 && hour < 3 || hour < 3 || hour >= 10 ? "dinner" : "breakfast";
	}

	@SuppressWarnings("deprecation")
	private static int getLastHour(final HandlerInput i) {
		final DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());
		List<Portion> FoodList;
		try {
			FoodList = db.DBGetTodayFoodList().stream().filter(p -> p.getValue().getType() == Type.FOOD)
					.map(p -> p.getValue()).collect(Collectors.toList());
		} catch (final DBException e) {
			return -1;
		}
		if (FoodList.isEmpty())
			return -1;
		Date last = FoodList.get(0).getTime();
		for (final Portion p : FoodList)
			if (p.getTime().after(last))
				last = p.getTime();
		return last.getHours();
	}

}