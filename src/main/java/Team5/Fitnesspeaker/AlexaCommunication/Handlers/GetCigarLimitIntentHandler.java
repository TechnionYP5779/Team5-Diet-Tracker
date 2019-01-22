package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import Utils.DBUtils;
import Utils.UserInfo;
import Utils.DBUtils.DBException;

public class GetCigarLimitIntentHandler implements RequestHandler{
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("GetCigarLimitIntent"));
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
			speechText = String.format("you didn't tell me what is your cigarettes limit");
		else {
			final int cigarLimit = user.getDailyLimitCigarettes();
			if (cigarLimit == -1)
				speechText = String.format("you didn't tell me what is your cigarettes limit");
			else
				speechText = String.format("you are allowed to smoke %d cigarettes every day", Integer.valueOf(cigarLimit));

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
