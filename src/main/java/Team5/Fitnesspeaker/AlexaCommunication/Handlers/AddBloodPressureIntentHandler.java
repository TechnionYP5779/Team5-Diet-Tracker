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

public class AddBloodPressureIntentHandler implements RequestHandler{
	public static final String SYSTOLIC = "Number";
	public static final String DIASTOLIC = "NumberTwo";
	
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("AddBloodPressureIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot systolicSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SYSTOLIC);
		
		final Slot diastolicSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(DIASTOLIC);
		if(systolicSlot==null||diastolicSlot==null) 
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", "there was a problem with the blood pressure's logging, Please tell me again").
					withSpeech("there was a problem with the blood pressure's logging, Please tell me again")
				.withReprompt("there was a problem with the blood pressure's logging, Please tell me again").withShouldEndSession(Boolean.FALSE).build();
		
		Integer systolic = Integer.valueOf(Integer.parseInt(systolicSlot.getValue()));
		Integer diastolic = Integer.valueOf(Integer.parseInt(diastolicSlot.getValue()));
		String speechText , repromptText="";
		
		//initialize database object with the user mail
		DBUtils db=new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());
		
		//log the new blood pressure measure
		try {
			db.DBPushBloodPressureMeasure(new BloodPressure(systolic,diastolic,new Date()));

		} catch (DBException e) {
			speechText = String.format("There was a problem with the blood pressure's logging, Please tell me again");
			repromptText = String.format("I'll repeat, there was a problem with the blood pressure's logging, Please tell me again");
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
		}
		
		speechText = String.format("blood pressure's measure was logged successfully");
		
		//the Boolean.TRUE says that the Alexa will end the session 
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();
	}

}
