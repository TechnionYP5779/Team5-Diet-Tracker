package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.Pair;
import Utils.AlcoholBloodCalc;
import Utils.Portion;
import Utils.UserInfo;
import Utils.DBUtils;
import Utils.DailyInfo;
import Utils.DBUtils.DBException;

public class CanIDriveIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName(Utils.Strings.IntentsNames.CAN_I_DRIVE_INTENT));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);
		String speechText = "";
		UserInfo ui=null;
		DailyInfo di=null;
		try {
		    ui=db.DBGetUserInfo();
		    di =db.DBGetTodayDailyInfo();
		} catch (DBException e) {}
		
		if(ui==null) {
			speechText = Utils.Strings.AlcoholStrings.CAN_DRIVE_TELL_GENDER;
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withShouldEndSession(Boolean.FALSE).build();
		}
		
		if(di==null) {
			speechText = Utils.Strings.AlcoholStrings.CAN_DRIVE_TELL_WEIGHT;
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withShouldEndSession(Boolean.FALSE).build();
		}
			
		int weight= (int) di.getWeight();
		
		if (weight==-1) {
			speechText = Utils.Strings.AlcoholStrings.CAN_DRIVE_TELL_WEIGHT;
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withShouldEndSession(Boolean.FALSE).build();
		}
			
		List<Portion> todaysAlchohol = new LinkedList<>();
		try {
			for(Pair<String, Portion> p : db.DBGetTodayAlcoholList())
				todaysAlchohol.add(p.getValue());
		} catch (DBException e) {}
		
		double alcInblood=0;
		if(ui.gender==UserInfo.Gender.MALE)
			alcInblood=new AlcoholBloodCalc().setForMale().setWeight(weight).CalcForNow(todaysAlchohol);
		else
			alcInblood=new AlcoholBloodCalc().setForFemale().setWeight(weight).CalcForNow(todaysAlchohol);
		if ( alcInblood>= 0.05)
			speechText = Utils.Strings.AlcoholStrings.CANT_DRIVE;
		else
			speechText = Utils.Strings.AlcoholStrings.CAN_DRIVE;

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
