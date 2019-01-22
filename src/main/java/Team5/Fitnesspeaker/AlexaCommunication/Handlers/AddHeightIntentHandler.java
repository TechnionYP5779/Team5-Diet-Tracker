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

public class AddHeightIntentHandler implements RequestHandler{
	public static final String NUMBER_SLOT = "Number";

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("AddHeightIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(NUMBER_SLOT);
		String speechText = "", repromptText;

		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);

		if (NumberSlot == null) {
			speechText = "I'm not sure what is your height. Please tell me again";
			repromptText = "I will repeat, I'm not sure what is your height. Please tell me again";
		} else {
			final int height = Integer.parseInt(NumberSlot.getValue());

			UserInfo u = new UserInfo();
			u.setHeight(height);
			UserInfo user = null;
			try {
				user = db.DBGetUserInfo();
			} catch (DBException e) {
				// no need to do anything
			}
			if (user==null)
				db.DBUpdateUserInfo(u);
			else
				db.DBUpdateUserInfo(new UserInfo(user.getGender(), user.getAge(),
						height,
						user.getDailyCaloriesGoal(),
						user.getDailyProteinGramsGoal(), user.getDailyCarbsGoal(),
						user.getDailyFatsGoal(), user.getDailyLimitCigarettes()));

			speechText = String.format("your height is %d centimeters", Integer.valueOf(height));
			repromptText = "I will repeat, You can ask me what is your height saying, what is my height?";

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}
}
