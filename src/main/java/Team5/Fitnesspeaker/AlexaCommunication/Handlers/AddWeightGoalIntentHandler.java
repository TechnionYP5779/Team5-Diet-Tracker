package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.UserInfo;

public class AddWeightGoalIntentHandler implements RequestHandler{
	public static final String NUMBER_SLOT = "Number";

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("addWeightGoalIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(NUMBER_SLOT);
		String speechText = "hello", repromptText="world";
		
		if (NumberSlot == null) {
			speechText = "I'm not sure what is your weight goal. Please tell me again";
			repromptText = "I will repeat, I'm not sure what is your weight goal. Please tell me again";
		} else {
			final int weight = Integer.parseInt(NumberSlot.getValue());
			speechText=NumberSlot.getValue();
			final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail();
			DBUtils db=new DBUtils(UserMail);
			
			UserInfo ui=null;
			try {
				ui=db.DBGetUserInfo();
			} catch (DBException e) {}
			if(ui ==null)
				ui=new UserInfo();
			ui.setWeightGoal(weight);
			db.DBUpdateUserInfo(ui);
			speechText = "logged succesfully";
			repromptText = "logged succesfully";
		}
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}
}