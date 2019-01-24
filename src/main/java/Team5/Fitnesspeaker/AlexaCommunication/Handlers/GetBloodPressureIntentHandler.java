package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import Utils.BloodPressure;
import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Strings;
import Utils.Strings.BloodPressureString;
import Utils.Strings.IntentsNames;

/** this class handles blood pressure querying
 * @author Shalev Kuba
 * @since 2018-12-26
 * */
public class GetBloodPressureIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.GET_BLOOD_PRESSURE_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {

		String speechText = "";
		// initialize database object with the user mail
		final DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());

		// retrieving all logs from the DB
		List<BloodPressure> logListDB = new LinkedList<>();
		try {
			logListDB = db.DBGetTodayBloodPressureMeasuresList().stream().map(p -> p.getValue())
					.collect(Collectors.toList());
		} catch (final DBException e) {
			// no need to do anything
		}

		for (final BloodPressure log : logListDB)
			speechText += log.toString() + ", ";

		if (speechText.isEmpty())
			speechText = BloodPressureString.NO_LOGGED_BLODD_PRESSURE;

		return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withReprompt(speechText).withShouldEndSession(Boolean.TRUE).build();
	}

}
