package Team5.Fitnesspeaker.AlexaCommunication.Handlers.FoodHandlers;

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

import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Portion.Portion;
import Utils.Strings.GoalsAndMeasuresStrings;
import Utils.Strings.IntentsNames;
import Utils.Strings.NutritionalString;
import Utils.Strings.SlotString;
import Utils.User.UserInfo;

public class HowMuchNutritionalValuesIntentHandler implements RequestHandler {

	public static boolean eatTooMuch(int progressPerc, int hour) {
		if (hour > 4 && hour < 12 && progressPerc > 40)
			return true;
		if (hour > 4 && hour < 18 && progressPerc > 90)
			return true;
		if (progressPerc > 100)
			return true;
		return false;
	}

	public static boolean eatTooFew(int progressPerc, int hour) {
		if (hour > 12 && progressPerc < 20)
			return true;
		if (hour > 18 && progressPerc < 60)
			return true;
		return false;
	}

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.HOW_MUCH_MEASURE_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText = "";
		final Slot measureSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.MEASURE_SLOT);

		final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		DBUtils db = new DBUtils(UserMail);

		List<Portion> FoodList = new LinkedList<>();
		// retrieving the information
		try {
			FoodList = db.DBGetTodayFoodList().stream().map(p -> p.getValue()).collect(Collectors.toList());
		} catch (DBException e) {
			// no need to do anything
		}
		final String measure_str = measureSlot.getValue();
		double total_measure = 0;
		for (final Portion p : FoodList) {
			double measure = 0;
			double amount = p.getAmount();
			if (NutritionalString.FATS.contains(measure_str))
				measure = p.getFats_per_100_grams();
			else if (NutritionalString.CARBS.contains(measure_str))
				measure = p.getCarbs_per_100_grams();
			else if (NutritionalString.PROTEINS.contains(measure_str))
				measure = p.getProteins_per_100_grams();
			else if (NutritionalString.CALORIES.contains(measure_str))
				measure = p.getCalories_per_100_grams();
			total_measure += measure * ((double) amount);
		}
		//total_measure += (total_measure % 10 > 0 ? 10 : 0) - (total_measure % 10);

		// after we got the total measure we look for the goal

		UserInfo user = null;
		try {
			user = db.DBGetUserInfo();
		} catch (DBException e) {
			// no need to do anything
		}
		String speechText2 = "", speechText3 = "";
		if (user == null || (user.getDailyCaloriesGoal() == -1 && NutritionalString.CALORIES.contains(measure_str))
				|| (user.getDailyProteinGramsGoal() == -1 && NutritionalString.PROTEINS.contains(measure_str))
				|| (user.getDailyCarbsGoal() == -1 && NutritionalString.CARBS.contains(measure_str))
				|| (user.getDailyFatsGoal() == -1 && NutritionalString.FATS.contains(measure_str)))
			speechText2 = GoalsAndMeasuresStrings.DIDNT_TELL_GOAL_YET;
		else {
			int goal = 0;
			if (NutritionalString.FATS.contains(measure_str))
				goal = (int) user.getDailyFatsGoal();
			else if (NutritionalString.CARBS.contains(measure_str))
				goal = (int) user.getDailyCarbsGoal();
			else if (NutritionalString.PROTEINS.contains(measure_str))
				goal = (int) user.getDailyProteinGramsGoal();
			else if (NutritionalString.CALORIES.contains(measure_str))
				goal = (int) user.getDailyCaloriesGoal();
			int progress = (int) ((total_measure / (double) goal) * 100.0);
			int amount = goal - (int) total_measure;
			Calendar curr = Calendar.getInstance();
			int hour = curr.get(Calendar.HOUR_OF_DAY);
			if (eatTooMuch(progress, hour))
				speechText3 = GoalsAndMeasuresStrings.GOAL_CLOSE;
			else if (eatTooFew(progress, hour))
				speechText3 = GoalsAndMeasuresStrings.GOAL_FAR;
			else
				speechText3 = GoalsAndMeasuresStrings.GOAL_KEEP;

			if (NutritionalString.CALORIES.contains(measure_str))
				if (amount > 0)
					speechText2 = String.format(GoalsAndMeasuresStrings.LEFT_FOR_GOAL, Integer.valueOf(amount),
							measure_str) + speechText3;
				else if (amount == 0)
					speechText2 = String.format(GoalsAndMeasuresStrings.GOAL_ACHIEVED, measure_str);
				else
					speechText2 = String.format(GoalsAndMeasuresStrings.GOAL_PASSED, measure_str,
							Integer.valueOf(-amount), measure_str);
			else if (amount > 0)
				speechText2 = String.format(GoalsAndMeasuresStrings.LEFT_FOR_GOAL_GRAMS, Integer.valueOf(amount),
						measure_str) + speechText3;
			else if (amount == 0)
				speechText2 = String.format(GoalsAndMeasuresStrings.GOAL_ACHIEVED, measure_str);
			else
				speechText2 = String.format(GoalsAndMeasuresStrings.GOAL_PASSED_GRAMS, measure_str,
						Integer.valueOf(-amount));

		}

		if (total_measure == 0)
			speechText = GoalsAndMeasuresStrings.DIDNT_EAT + speechText2;
		else if (NutritionalString.CALORIES.contains(measure_str))
			speechText = String.format(GoalsAndMeasuresStrings.MEASURE_AMOUNT_ATE, Integer.valueOf((int) total_measure),
					measure_str) + speechText2;
		else
			speechText = String.format(GoalsAndMeasuresStrings.MEASURE_AMOUNT_ATE_GRAMS,
					Integer.valueOf((int) total_measure), measure_str) + speechText2;

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.TRUE).build();

	}
}
