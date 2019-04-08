package Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers;

import static com.amazon.ask.request.Predicates.intentName;
import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import Utils.DBUtils;
import Utils.DailyInfo;
import Utils.DBUtils.DBException;
import Utils.Strings.SlotString;

public class AddWeightIntentHandler implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("AddWeightIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.NUMBER_SLOT);
		String speechText = "", repromptText;

		// added database
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		DBUtils db = new DBUtils(UserMail);
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		if (NumberSlot == null) {
			speechText = "I'm not sure what is your weight. Please tell me again";
			repromptText = "I will repeat, I'm not sure what is your weight. Please tell me again";
		} else {
			final int weight = Integer.parseInt(NumberSlot.getValue());
			DailyInfo di = new DailyInfo();
			di.setWeight(weight);
			DailyInfo di2 = null;
			try {
				di2 = db.DBGetTodayDailyInfo();
			} catch (DBException e) {
				// no need to do anything
			}
			if (di2 == null)
				try {
					db.DBUpdateTodayDailyInfo(di);
				} catch (DBException e) { // nothing to do
				}
			else
				try {
					db.DBUpdateTodayDailyInfo(new DailyInfo(weight, di2.getDailyCalories(), di2.getDailyLitresOfWater(),
							di2.getDailyProteinGrams()));
				} catch (DBException e) { // nothing to do
				}

			speechText = String.format("your weight is %d kilograms", Integer.valueOf(weight));
			repromptText = "I will repeat, You can ask me what is your weight saying, what is my weight?";

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}
}
