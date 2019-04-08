package Team5.Fitnesspeaker.AlexaCommunication.Handlers.GoalsHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import Utils.UserInfo;
import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Strings.GoalsAndMeasuresStrings;
import Utils.Strings.IntentsNames;
import Utils.Strings.NutritionalString;
import Utils.Strings.SlotString;

public class GetGoalIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.GET_GOAL_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {

		final Slot MeasureSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.MEASURE_SLOT);
		String speechText = "", repromptText = "";
		final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);

		if (MeasureSlot == null) {
			speechText = GoalsAndMeasuresStrings.TELL_MEASURE_AGAIN;
			repromptText = GoalsAndMeasuresStrings.TELL_MEASURE_AGAIN_REPEAT;
		} else {
			final String measure_str = MeasureSlot.getValue();
			UserInfo user = null;
			try {
				user = db.DBGetUserInfo();
			} catch (DBException e) {
				// no need to do anything
			}

			if (user == null)
				speechText = String.format(GoalsAndMeasuresStrings.DIDNT_TELL_MEASURE_GOAL, measure_str);
			else {
				int amount = 0;
				if (NutritionalString.FATS.contains(measure_str))
					amount = (int) user.getDailyFatsGoal();
				else if (NutritionalString.CARBS.contains(measure_str))
					amount = (int) user.getDailyCarbsGoal();
				else if (NutritionalString.PROTEINS.contains(measure_str))
					amount = (int) user.getDailyProteinGramsGoal();
				else if (NutritionalString.CALORIES.contains(measure_str))
					amount = (int) user.getDailyCaloriesGoal();
				if (amount == -1)
					speechText = String.format(GoalsAndMeasuresStrings.DIDNT_TELL_MEASURE_GOAL, measure_str);
				else if (NutritionalString.CALORIES.contains(measure_str))
					speechText = String.format(GoalsAndMeasuresStrings.MEASURE_GOAL_LOGGED_CALORIES, measure_str,
							Integer.valueOf(amount));
				else
					speechText = String.format(GoalsAndMeasuresStrings.MEASURE_GOAL_LOGGED_GRAMS, measure_str,
							Integer.valueOf(amount));
			}
		}
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();

	}
}
