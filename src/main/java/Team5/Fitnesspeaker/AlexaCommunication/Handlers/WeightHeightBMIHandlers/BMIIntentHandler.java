package Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import Utils.DailyInfo;
import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Strings.BMIStrings;
import Utils.Strings.IntentsNames;
import Utils.User.UserInfo;

public class BMIIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.BMI_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		String speechText = "";

		final String UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);

		// Get the Weight

		DailyInfo daily = null;
		try {
			daily = db.DBGetTodayDailyInfo();
		} catch (DBException e) {
			// no need to do anything
		}
		final int weight = daily == null ? -1 : (int) daily.getWeight();

		// get the height

		UserInfo user = null;
		try {
			user = db.DBGetUserInfo();
		} catch (DBException e) {
			// no need to do anything
		}
		final int height = user == null ? -1 : user.getHeight();

		if (weight == -1)
			if (height == -1)
				speechText = BMIStrings.BMI_TELL_HEIGHT_AND_WEIGHT;
			else
				speechText = BMIStrings.BMI_TELL_WEIGHT;
		else if (height == -1)
			speechText = BMIStrings.BMI_TELL_HEIGHT;
		else {
			double heightInMeters = ((double) height) / 100.0;
			double bmi = ((double) weight) / (heightInMeters * heightInMeters);
			speechText = String.format(BMIStrings.TELL_BMI, Double.valueOf(bmi));
		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
