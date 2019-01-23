package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import Utils.DBUtils;
//import Utils.DailyInfo;
import Utils.Portion;
import Utils.UserInfo;
import Utils.DBUtils.DBException;
import Utils.Portion.Type;

public class HowMuchCaloriesIntentHandler implements RequestHandler {
	public static final String MEASURE_SLOT = "Measure";

	public static String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("HowMuchMeasureIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText = "";
		final Slot measureSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(MEASURE_SLOT);

		// added
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		DBUtils db = new DBUtils(UserMail);

		List<Portion> FoodList = new LinkedList<>();
		// retrieving the information
		try {
			FoodList = db.DBGetTodayFoodList().stream().map(p -> p.getValue()).filter(p -> p.getType() == Type.FOOD)
					.collect(Collectors.toList());
		} catch (DBException e) {
			// no need to do anything
		}
		final String measure_str = measureSlot.getValue();
		double total_measure = 0;
		for (final Portion p : FoodList) {
			double measure = 0;
			double amount = p.getAmount();
			if ("fats".contains(measure_str))
				measure = p.getFats_per_100_grams();
			else if ("carbs".contains(measure_str))
				measure = p.getCarbs_per_100_grams();
			else if ("proteins".contains(measure_str))
				measure = p.getProteins_per_100_grams();
			else if ("calories".contains(measure_str))
				measure = p.getCalories_per_100_grams();
			total_measure += measure * ((double) amount / 100);
		}
		UserInfo user = null;
		try {
			user = db.DBGetUserInfo();
		} catch (DBException e) {
			// no need to do anything
		}
		String speechText2 = "";
		if (user == null || (user.getDailyCaloriesGoal() == -1 && "calories".contains(measure_str))
				|| (user.getDailyProteinGramsGoal() == -1 && "proteins".contains(measure_str))
				|| (user.getDailyCarbsGoal() == -1 && "carbs".contains(measure_str))
				|| (user.getDailyFatsGoal() == -1 && "fats".contains(measure_str)))
			speechText2 = String.format("You didn't tell me your goal yet!");
		else {
			int calories = (int) user.getDailyCaloriesGoal();
			int fats = (int) user.getDailyFatsGoal();
			int carbs = (int) user.getDailyCarbsGoal();
			int proteins = (int) user.getDailyProteinGramsGoal();
			if ("calories".contains(measure_str)) {
				int amount = calories - (int) total_measure;
				if (amount > 0)
					speechText2 = String.format(" There are %d %s left for your goal! Keep going!",
							Integer.valueOf(amount), measure_str);
				else if (amount == 0)
					speechText2 = String.format(" You achieved your %s goal, Well Done!", measure_str);
				else
					speechText2 = String.format(" You passed your %s goal by %d %s", measure_str,
							Integer.valueOf(-amount), measure_str);
			} else {
				int amount = ("proteins".contains(measure_str) ? proteins - (int) total_measure
						: ("carbs".contains(measure_str) ? carbs - (int) total_measure
								: (!"fats".contains(measure_str) ? 0 : fats - (int) total_measure)));
				speechText2 = String.format(" There are %d grams of %s left for your goal! Keep going!",
						Integer.valueOf(amount), measure_str);
				if (amount > 0)
					speechText2 = String.format(" There are %d grams of %s left for your goal! Keep going!",
							Integer.valueOf(amount), measure_str);
				else if (amount == 0)
					speechText2 = String.format(" You achieved your %s goal, Well Done!", measure_str);
				else
					speechText2 = String.format(" You passed your %s goal by %d grams", measure_str,
							Integer.valueOf(-amount));
			}

		}

		if (total_measure == 0)
			speechText = String.format("you didn't eat anything today. ") + speechText2;
		else if ("calories".contains(measure_str))
			speechText = String.format("You ate %d %s today. ", Integer.valueOf((int) total_measure), measure_str)
					+ speechText2;
		else
			speechText = String.format("You ate %d grams of %s today. ", Integer.valueOf((int) total_measure),
					measure_str) + speechText2;

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
