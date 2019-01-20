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
import Utils.DBUtils.DBException;

public class CanIDriveIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("CanIDriveIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail();
		DBUtils db = new DBUtils(UserMail);
		String speechText = "";
		UserInfo ui=null;
		try {
		    ui=db.DBGetUserInfo();
		}catch(Exception e) {
			speechText = String.format("you didn't tell me what is your gender");
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withShouldEndSession(Boolean.FALSE).build();
		} catch (DBException e) {}
			
		double weight=-1;
		try {
			weight=db.DBGetTodayDailyInfo().getWeight();
		}
		catch(Exception e) {} catch (DBException e) {}
		
		if (weight==-1) {
			speechText = String.format("you didn't tell me what is your weight");
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withShouldEndSession(Boolean.FALSE).build();
		}
			
		List<Portion> todaysAlchohol = new LinkedList<>();
		try {
			for(Pair<String, Portion> p : db.DBGetTodayAlcoholList()) {
				todaysAlchohol.add(p.getValue());
			}
		} catch (DBException e) {}
		
		double alcInblood=0;
		if(ui.gender==UserInfo.Gender.MALE)
			alcInblood=new AlcoholBloodCalc().setForMale().setWeight(weight).CalcForNow(todaysAlchohol);
		else
			alcInblood=new AlcoholBloodCalc().setForFemale().setWeight(weight).CalcForNow(todaysAlchohol);
		if ( alcInblood>= 0.05)
			speechText = "you can't drive right now, you drank too much";
		else
			speechText = "you are allowed to drive, go safely";

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}
