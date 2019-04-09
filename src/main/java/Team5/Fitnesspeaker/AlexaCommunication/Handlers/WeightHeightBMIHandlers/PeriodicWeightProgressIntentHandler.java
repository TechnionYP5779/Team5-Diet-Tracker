package Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.DailyInfo;
import Utils.Strings.IntentsNames;
import Utils.UserInfo;

public class PeriodicWeightProgressIntentHandler implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.PERIODIC_WEIGHT_PROGRESS));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText = "", repromptText = "";
		final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);
		UserInfo ui = null;
		DailyInfo di = null;
		String c = "";
		try {
			ui = db.DBGetUserInfo();
			di = db.DBGetTodayDailyInfo();
		} catch (DBException e) {
			// nothing to do here
		}
		if (ui == null) {
			c += "ui";
			speechText = Utils.Strings.GoalsAndMeasuresStrings.DIDNT_TELL_WEIGHT_GOAL;
			repromptText = Utils.Strings.GoalsAndMeasuresStrings.DIDNT_TELL_WEIGHT_GOAL;
			ui = new UserInfo();
		}

		int wg = ui.getWeightGoal();
		int w = (int) di.getWeight();

		if (wg == -1) {
			speechText = Utils.Strings.GoalsAndMeasuresStrings.DIDNT_TELL_WEIGHT_GOAL;
			repromptText = Utils.Strings.GoalsAndMeasuresStrings.DIDNT_TELL_WEIGHT_GOAL;
		}

		if (w == -1) {
			speechText = Utils.Strings.GoalsAndMeasuresStrings.DIDNT_TELL_WEIGHT;
			repromptText = Utils.Strings.GoalsAndMeasuresStrings.DIDNT_TELL_WEIGHT;
		}

		if (wg != -1 && w != -1) {
			int diff = wg - w;
			if (diff < 0)
				diff = -diff;
			if (diff < 3) {
				speechText = Utils.Strings.GoalsAndMeasuresStrings.DOING_GREAT;
				speechText = Utils.Strings.GoalsAndMeasuresStrings.DOING_GREAT;
			} else {
				speechText = Utils.Strings.GoalsAndMeasuresStrings.FAR_FROM_GOAL;
				repromptText = Utils.Strings.GoalsAndMeasuresStrings.FAR_FROM_GOAL;
			}
		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText + c).withSpeech(speechText + c)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}
}