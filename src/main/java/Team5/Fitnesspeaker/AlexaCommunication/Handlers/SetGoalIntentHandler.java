package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import Utils.DBUtils;
import Utils.UserInfo;
import Utils.DBUtils.DBException;
import Utils.Strings.GeneralString;
import Utils.Strings.GoalsAndMeasuresStrings;
import Utils.Strings.IntentsNames;

public class SetGoalIntentHandler implements RequestHandler{
	public static final String MEASURE_SLOT = "Measure";
	public static final String NUMBER_SLOT = "Number";
	public static final String CALORIES = "calories";
	public static final String CARBS = "carbs";
	public static final String PROTEINS = "proteins";
	public static final String FATS = "fats";
	
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.SET_GOAL_INTENT));
	}
	
	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(NUMBER_SLOT);
		
		final Slot MeasureSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(MEASURE_SLOT);
		
		String speechText = "", repromptText;
		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);

		if (NumberSlot == null || MeasureSlot == null) {
			speechText = GoalsAndMeasuresStrings.TELL_MEASURE_GOAL_AGAIN;
			repromptText = GoalsAndMeasuresStrings.TELL_MEASURE_AGAIN_REPEAT;
		} else {
			final String measure_str = MeasureSlot.getValue();
			final int amount = Integer.parseInt(NumberSlot.getValue());
			UserInfo u = new UserInfo();
			if (FATS.contains(measure_str))
				u.setDailyFatsGoal(amount);
			else if (CARBS.contains(measure_str))
				u.setDailyCarbsGoal(amount);
			else if (PROTEINS.contains(measure_str))
				u.setDailyProteinGramsGoal(amount);
			else if (CALORIES.contains(measure_str))
				u.setDailyCaloriesGoal(amount);
			UserInfo user = null;
			try {
				user = db.DBGetUserInfo();
			} catch (DBException e) {
				// no need to do anything
			}
			if (user == null)
				db.DBUpdateUserInfo(u);
			else {
				if (FATS.contains(measure_str))
					db.DBUpdateUserInfo(new UserInfo(user.getGender(), user.getAge(),
							user.getHeight(),
							user.getDailyCaloriesGoal(),
							user.getDailyProteinGramsGoal(), user.getDailyCarbsGoal(),
							amount, user.getDailyLimitCigarettes()));
				else if (CARBS.contains(measure_str))
					db.DBUpdateUserInfo(new UserInfo(user.getGender(), user.getAge(),
							user.getHeight(),
							user.getDailyCaloriesGoal(),
							user.getDailyProteinGramsGoal(), amount,
							user.getDailyFatsGoal(), user.getDailyLimitCigarettes()));
				else if (PROTEINS.contains(measure_str))
					db.DBUpdateUserInfo(new UserInfo(user.getGender(), user.getAge(),
							user.getHeight(),
							user.getDailyCaloriesGoal(),
							amount, user.getDailyCarbsGoal(),
							user.getDailyFatsGoal(), user.getDailyLimitCigarettes()));
				else if (CALORIES.contains(measure_str))
					db.DBUpdateUserInfo(new UserInfo(user.getGender(), user.getAge(),
							user.getHeight(),amount,
							user.getDailyProteinGramsGoal(), user.getDailyCarbsGoal(),
							user.getDailyFatsGoal(), user.getDailyLimitCigarettes()));
			}

			speechText = GeneralString.LOGGED_SUCCESSFULLY;
			repromptText = GeneralString.LOGGED_SUCCESSFULLY_REPEAT;

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}
}
