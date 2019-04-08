package Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Calendar;
import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Strings.CigarettesStrings;
import Utils.Strings.IntentsNames;

public class AddSmokeIntentHandler implements RequestHandler {
	public static final String NUMBER_SLOT = "Number";

	public static String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.ADD_SMOKE_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(NUMBER_SLOT);
		String speechText = "", repromptText;

		final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);

		if (NumberSlot == null) {
			speechText = CigarettesStrings.TELL_CIGS_AGAIN;
			repromptText = CigarettesStrings.TELL_CIGS_AGAIN_REPEAT;
		} else {
			final int added_num_of_cigarettes = Integer.parseInt(NumberSlot.getValue());

			try {
				db.DBAddCigarettes(Integer.valueOf(added_num_of_cigarettes));
			} catch (DBException e) {
				speechText = CigarettesStrings.CIGS_LOGGING_PROBLEM;
				repromptText = CigarettesStrings.CIGS_LOGGING_PROBLEM_REPEAT;
				return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
						.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
			}

			if (added_num_of_cigarettes == 1)
				speechText = CigarettesStrings.ONE_CIG_LOGGED;
			else
				speechText = String.format(CigarettesStrings.MANY_CIGS_LOGGED,
						Integer.valueOf(added_num_of_cigarettes));
			repromptText = CigarettesStrings.ASK_CIGS;

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}

}
