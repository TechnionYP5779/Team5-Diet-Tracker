package Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import Utils.DBUtils;
import Utils.UserInfo;
import Utils.DBUtils.DBException;
import Utils.Strings.CigarettesStrings;
import Utils.Strings.IntentsNames;

public class GetCigarLimitIntentHandler implements RequestHandler{
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.GET_CIGAR_LIMIT_INTENT));
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
			speechText = CigarettesStrings.DIDNT_TELL_CIGS_LIMIT;
		else {
			final int cigarLimit = user.getDailyLimitCigarettes();
			if (cigarLimit == -1)
				speechText = CigarettesStrings.DIDNT_TELL_CIGS_LIMIT;
			else
				speechText = String.format(CigarettesStrings.TELL_CIGS_LIMIT, Integer.valueOf(cigarLimit));

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
