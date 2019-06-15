package Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Strings.HeightStrings;
import Utils.Strings.IntentsNames;
import Utils.User.UserInfo;

public class GetHeightIntentHandler implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.HOW_MUCH_HEIGHT_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText = "";

		final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);

		UserInfo user = null;
		try {
			user = db.DBGetUserInfo();
		} catch (DBException e) {
			// no need to do anything
		}

		if (user == null)
			speechText = HeightStrings.DIDNT_TELL_HEIGHT;
		else {
			final int height = user.getHeight();
			if (height == -1)
				speechText = HeightStrings.DIDNT_TELL_HEIGHT;
			else
				speechText = String.format(HeightStrings.HEIGHT_IS, Integer.valueOf(height));

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
