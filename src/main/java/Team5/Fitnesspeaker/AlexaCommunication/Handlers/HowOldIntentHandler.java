package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import Utils.DBUtils;
import Utils.UserInfo;
import Utils.DBUtils.DBException;

public class HowOldIntentHandler implements RequestHandler{
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("HowOldIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText = "";

		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);

		UserInfo user = null;
		try {
			user = db.DBGetUserInfo();
		} catch (DBException e) {
			// no need to do anything
		}

		if (user==null)
			speechText = String.format("you didn't tell me what is your age yet");
		else {
			final int age = user.getAge();
			if (age == -1)
				speechText = String.format("you didn't tell me what is your age yet");
			else
				speechText = String.format("you are %d years old", Integer.valueOf(age));

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}

}
