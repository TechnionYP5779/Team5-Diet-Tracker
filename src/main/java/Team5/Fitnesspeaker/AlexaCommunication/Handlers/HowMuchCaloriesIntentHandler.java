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
	public static final String CALORIES = "calories";
	public static final String CARBS = "carbs";
	public static final String PROTEINS = "proteins";
	public static final String FATS = "fats";
	
	public static boolean eatTooMuch(int progressPerc, int hour) {
		if(hour>4 && hour < 12 && progressPerc > 40) {
			return true;
		}
		if(hour > 4 && hour < 18 && progressPerc > 90) {
			return true;
		}
		if(progressPerc > 100) {
			return true;
		}
		return false;
	}
	
	public static boolean eatTooFew(int progressPerc, int hour) {
		if(hour > 12 && progressPerc < 20) {
			return true;
		}
		if(hour > 18 && progressPerc < 60) {
			return true;
		}
		return false;
	}

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
			if (FATS.contains(measure_str))
				measure = p.getFats_per_100_grams();
			else if (CARBS.contains(measure_str))
				measure = p.getCarbs_per_100_grams();
			else if (PROTEINS.contains(measure_str))
				measure = p.getProteins_per_100_grams();
			else if (CALORIES.contains(measure_str))
				measure = p.getCalories_per_100_grams();
			total_measure += measure * ((double) amount / 100);
		}
		total_measure += (total_measure%50>0 ? 50: 0) - (total_measure%50);
		
		// after we got the total measure we look for the goal
		
		UserInfo user = null;
		try {
			user = db.DBGetUserInfo();
		} catch (DBException e) {
			// no need to do anything
		}
		String speechText2 = "", speechText3= "";
		if (user == null || (user.getDailyCaloriesGoal() == -1 && CALORIES.contains(measure_str))
				|| (user.getDailyProteinGramsGoal() == -1 && PROTEINS.contains(measure_str))
				|| (user.getDailyCarbsGoal() == -1 && CARBS.contains(measure_str))
				|| (user.getDailyFatsGoal() == -1 && FATS.contains(measure_str)))
			speechText2 = String.format("You didn't tell me your goal yet!");
		else {
			int goal=0;
			if (FATS.contains(measure_str))
				goal = (int) user.getDailyFatsGoal();
			else if (CARBS.contains(measure_str))
				goal = (int) user.getDailyCarbsGoal();
			else if (PROTEINS.contains(measure_str))
				goal = (int) user.getDailyProteinGramsGoal();
			else if (CALORIES.contains(measure_str))
				goal = (int) user.getDailyCaloriesGoal();
			int progress = (int)((total_measure / (double) goal) * 100.0);
			int amount = goal - (int)total_measure;
			Calendar curr = Calendar.getInstance();
			int hour = curr.get(Calendar.HOUR_OF_DAY);
			if(eatTooMuch(progress, hour))
				speechText3 = " You are close to your goal. Try to minimize your eating rate for the rest of the day ";
			else if(eatTooFew(progress, hour))
				speechText3 = " You are far from your goal. Try to increase your eating rate for the rest of the day ";
			else
				speechText3 = " Keep going! ";
			
			if (CALORIES.contains(measure_str)) {
				if (amount > 0)
					speechText2 = String.format(" There are %d %s left for your goal! ",
							Integer.valueOf(amount), measure_str) + speechText3;
				else if (amount == 0)
					speechText2 = String.format(" You achieved your %s goal, Well Done!", measure_str);
				else
					speechText2 = String.format(" You passed your %s goal by %d %s . Try to minimize your eating rate for the rest of the day ", measure_str,
							Integer.valueOf(-amount), measure_str);
			} else {
				if (amount > 0)
					speechText2 = String.format(" There are %d grams of %s left for your goal! Keep going!",
							Integer.valueOf(amount), measure_str)+ speechText3;
				else if (amount == 0)
					speechText2 = String.format(" You achieved your %s goal, Well Done!", measure_str);
				else
					speechText2 = String.format(" You passed your %s goal by %d grams . Try to minimize your eating rate for the rest of the day", measure_str,
							Integer.valueOf(-amount));
			}

		}

		if (total_measure == 0)
			speechText = "you didn't eat anything today. " + speechText2;
		else if (CALORIES.contains(measure_str))
			speechText = String.format("You ate %d %s today. ", Integer.valueOf((int) total_measure), measure_str)
					+ speechText2;
		else
			speechText = String.format("You ate %d grams of %s today. ", Integer.valueOf((int) total_measure),
					measure_str) + speechText2;

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
