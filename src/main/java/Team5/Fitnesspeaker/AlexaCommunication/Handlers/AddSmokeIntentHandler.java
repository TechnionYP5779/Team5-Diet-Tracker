package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

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

public class AddSmokeIntentHandler implements RequestHandler{
	public static final String NUMBER_SLOT = "Number";
	
	public static String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("AddSmokeIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(NUMBER_SLOT);
		String speechText = "", repromptText;

		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);

		if (NumberSlot == null) {
			speechText = "I'm not sure how many cigarettes you smoked. Please tell me again";
			repromptText = "I will repeat, I'm not sure how many cigarettes you smoked. Please tell me again";
		} else {
			final int added_num_of_cigarettes = Integer.parseInt(NumberSlot.getValue());

			try {
				db.DBAddCigarettes(Integer.valueOf(added_num_of_cigarettes));
			} catch (DBException e) {
				speechText = String.format("There was a problem with the smoking logging, Please tell me again");
				repromptText = String.format("I'll repeat, there was a problem with the smoking logging,  Please tell me again");
				return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
						.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
			}

			if (added_num_of_cigarettes == 1)
				speechText = String.format(
						"you added one cigarette. You can ask me how many cigarettes you have smoked so far saying, "
								+ "how many cigarettes i smoked?");
			else
				speechText = String.format(
						"you added %d cigarettes. You can ask me how many cigarettes you have smoked so far saying, "
								+ "how many cigarettes i smoked?",
						Integer.valueOf(added_num_of_cigarettes));
			repromptText = "I will repeat, You can ask me how many you have smoked so far saying, how many cigarettes i smoked?";

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}

}
