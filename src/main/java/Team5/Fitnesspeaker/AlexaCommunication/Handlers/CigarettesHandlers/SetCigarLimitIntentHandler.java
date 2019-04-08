package Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers;

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
import Utils.Strings.CigarettesStrings;
import Utils.Strings.GeneralString;
import Utils.Strings.IntentsNames;

public class SetCigarLimitIntentHandler implements RequestHandler{
	public static final String NUMBER_SLOT = "Number";

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.CIGAR_LIMIT_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(NUMBER_SLOT);
		String speechText = "", repromptText;

		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);

		if (NumberSlot == null) {
			speechText = CigarettesStrings.TELL_CIGS_LIMIT_AGAIN;
			repromptText = CigarettesStrings.TELL_CIGS_LIMIT_AGAIN_REPEAT;
		} else {
			final int cigarettesLimit = Integer.parseInt(NumberSlot.getValue());

			UserInfo u = new UserInfo();
			u.setDailyLimitCigarettes(cigarettesLimit);
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
						user.getHeight(),
						user.getDailyCaloriesGoal(),
						user.getDailyProteinGramsGoal(), user.getDailyCarbsGoal(),
						user.getDailyFatsGoal(),cigarettesLimit));

			speechText = GeneralString.LOGGED_SUCCESSFULLY;
			repromptText = GeneralString.LOGGED_SUCCESSFULLY_REPEAT;

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}
}
