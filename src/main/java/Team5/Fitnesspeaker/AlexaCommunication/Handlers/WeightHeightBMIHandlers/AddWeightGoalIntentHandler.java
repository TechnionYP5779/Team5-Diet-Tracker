package Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Strings.SlotString;
import Utils.User.UserInfo;

public class AddWeightGoalIntentHandler implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("addWeightGoalIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.NUMBER_SLOT);
		String speechText = "", repromptText = "";

		if (NumberSlot == null) {
			speechText = Utils.Strings.GoalsAndMeasuresStrings.TELL_MEASURE_GOAL_AGAIN;
			repromptText = Utils.Strings.GoalsAndMeasuresStrings.TELL_MEASURE_AGAIN_REPEAT;
		} else {
			final int weight = Integer.parseInt(NumberSlot.getValue());
			speechText = NumberSlot.getValue();
			final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail();
			DBUtils db = new DBUtils(UserMail);

			UserInfo ui = null;
			try {
				ui = db.DBGetUserInfo();
			} catch (DBException e) { // nothing to do here

			}
			if (ui == null)
				ui = new UserInfo();
			ui.setWeightGoal(weight);
			db.DBUpdateUserInfo(ui);
			speechText = Utils.Strings.GeneralString.LOGGED_SUCCESSFULLY;
			repromptText = Utils.Strings.GeneralString.LOGGED_SUCCESSFULLY_REPEAT;
		}
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}
}