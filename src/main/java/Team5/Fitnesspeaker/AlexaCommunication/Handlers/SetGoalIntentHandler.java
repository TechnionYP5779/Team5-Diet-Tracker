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

public class SetGoalIntentHandler implements RequestHandler{
	public static final String MEASURE_SLOT = "Measure";
	public static final String NUMBER_SLOT = "Number";
	
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("SetGoalIntent"));
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
			speechText = "I'm not sure what is your goal. Please tell me again";
			repromptText = "I will repeat, I'm not sure what is your goal. Please tell me again";
		} else {
			final String measure_str = MeasureSlot.getValue();
			final int amount = Integer.parseInt(NumberSlot.getValue());
			UserInfo u = new UserInfo();
			if ("fats".contains(measure_str))
				u.setDailyFatsGoal(amount);
			else if ("carbs".contains(measure_str))
				u.setDailyCarbsGoal(amount);
			else if ("proteins".contains(measure_str))
				u.setDailyProteinGramsGoal(amount);
			else if ("calories".contains(measure_str))
				u.setDailyCaloriesGoal(amount);
			UserInfo user = null;
			try {
				user = db.DBGetUserInfo();
			} catch (DBException e) {
				// no need to do anything
			}
			if (user == null) {
				db.DBUpdateUserInfo(u);
			}
			else {
				if ("fats".contains(measure_str))
					db.DBUpdateUserInfo(new UserInfo(user.getGender(), user.getAge(),
							user.getHeight(),
							user.getDailyCaloriesGoal(),
							user.getDailyProteinGramsGoal(), user.getDailyCarbsGoal(),
							amount, user.getDailyLimitCigarettes()));
				else if ("carbs".contains(measure_str))
					db.DBUpdateUserInfo(new UserInfo(user.getGender(), user.getAge(),
							user.getHeight(),
							user.getDailyCaloriesGoal(),
							user.getDailyProteinGramsGoal(), amount,
							user.getDailyFatsGoal(), user.getDailyLimitCigarettes()));
				else if ("proteins".contains(measure_str))
					db.DBUpdateUserInfo(new UserInfo(user.getGender(), user.getAge(),
							user.getHeight(),
							user.getDailyCaloriesGoal(),
							amount, user.getDailyCarbsGoal(),
							user.getDailyFatsGoal(), user.getDailyLimitCigarettes()));
				else if ("calories".contains(measure_str))
					db.DBUpdateUserInfo(new UserInfo(user.getGender(), user.getAge(),
							user.getHeight(),amount,
							user.getDailyProteinGramsGoal(), user.getDailyCarbsGoal(),
							user.getDailyFatsGoal(), user.getDailyLimitCigarettes()));
			}

			speechText = String.format("logged successfully");
			repromptText = "I will repeat, logged successfully";

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}
}
