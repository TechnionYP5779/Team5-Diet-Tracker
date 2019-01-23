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

public class GetBloodPressureIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("GetBloodPressureIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {

		String speechText = "";
		// initialize database object with the user mail
		DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());

		// retrieving all logs from the DB
		List<BloodPressure> logListDB = new LinkedList<>();
		try {
			logListDB = db.DBGetTodayBloodPressureMeasuresList().stream().map(p -> p.getValue())
					.collect(Collectors.toList());
		} catch (DBException e) {
			// no need to do anything
		}

		if (logListDB.isEmpty())
			speechText = "You haven't logged any blood pressure measure today yet";
		else
			for (BloodPressure log : logListDB)
				speechText += log.toString() + ", ";

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(speechText).withShouldEndSession(Boolean.TRUE).build();
	}

}
