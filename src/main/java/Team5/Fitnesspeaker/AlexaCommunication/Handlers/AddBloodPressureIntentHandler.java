package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Date;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import Utils.BloodPressure;
import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Strings;
import Utils.Strings.BloodPressureString;
import Utils.Strings.IntentsNames;

public class AddBloodPressureIntentHandler implements RequestHandler {
	public static final String SYSTOLIC = "Number";
	public static final String DIASTOLIC = "NumberTwo";

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.ADD_BLOOD_PRESSURE_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot systolicSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().get(SYSTOLIC),
				diastolicSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().get(DIASTOLIC);

		final Integer systolic = Integer.valueOf(Integer.parseInt(systolicSlot.getValue())),
				diastolic = Integer.valueOf(Integer.parseInt(diastolicSlot.getValue()));
		String speechText, repromptText = "";

		if (systolicSlot == null || diastolicSlot == null)
			return i.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, BloodPressureString.TELL_BLODD_PRESSURE_AGAIN)
					.withSpeech(BloodPressureString.TELL_BLODD_PRESSURE_AGAIN)
					.withReprompt(BloodPressureString.TELL_BLODD_PRESSURE_AGAIN_REPEAT)
					.withShouldEndSession(Boolean.FALSE).build();

		// initialize database object with the user mail
		final DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());

		// log the new blood pressure measure
		try {
			db.DBPushBloodPressureMeasure(new BloodPressure(systolic, diastolic, new Date()));

		} catch (final DBException e) {
			speechText = String.format(BloodPressureString.TELL_BLODD_PRESSURE_AGAIN);
			repromptText = String.format(BloodPressureString.TELL_BLODD_PRESSURE_AGAIN_REPEAT);
			return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
		}

		speechText = String.format(BloodPressureString.BLODD_PRESSURE_LOGGED);

		// the Boolean.TRUE says that the Alexa will end the session
		return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();
	}

}
