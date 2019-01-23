package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

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

public class GetGoalIntentHandler implements RequestHandler{
	public static final String MEASURE_SLOT = "Measure";
	
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("GetGoalIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		
		final Slot MeasureSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(MEASURE_SLOT);
		String speechText = "", repromptText="";
		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);
		
		if (MeasureSlot == null) {
			speechText = "I'm not sure which goal do you want. Please tell me again";
			repromptText = "I will repeat, I'm not sure which goal do you want. Please tell me again";
		} else {
			final String measure_str = MeasureSlot.getValue();
			UserInfo user = null;
			try {
				user = db.DBGetUserInfo();
			} catch (DBException e) {
				// no need to do anything
			}
			

			if (user==null)
				speechText = String.format("you didn't tell me what is your " + measure_str + " goal");
			else {
				int amount=0;
				if ("fats".contains(measure_str))
					amount = (int)user.getDailyFatsGoal();
				else if ("carbs".contains(measure_str))
					amount = (int)user.getDailyCarbsGoal();
				else if ("proteins".contains(measure_str))
					amount = (int)user.getDailyProteinGramsGoal();
				else if ("calories".contains(measure_str))
					amount = (int)user.getDailyCaloriesGoal();
				if (amount == -1)
					speechText = String.format("you didn't tell me what is your " + measure_str + " goal");
				else if ("calories".contains(measure_str))
					speechText = String.format("your " + measure_str + " goal is %d calories", Integer.valueOf(amount));
				else
					speechText = String.format("your " + measure_str + " goal is %d grams", Integer.valueOf(amount));
			}
		}
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();

		

	}
}
