package Team5.Fitnesspeaker.AlexaCommunication.Handlers.API;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import org.json.simple.JSONObject;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import Utils.Strings;
import Utils.FoodsDB.FoodsDB;
import Utils.FoodsDB.FoodsDBException;

public class APIgetValues  implements RequestHandler{

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("APIgetValues"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		
		String email=input.getServiceClientFactory().getUpsService().getProfileEmail();
		
		
		String speechText, repromptText = "";
		
		
		speechText = repromptText = "";
		JSONObject vals=null;
		try {
			FoodsDB db =new FoodsDB();
			vals=db.NutVal4T4Today(email);
		} catch (FoodsDBException e) {
			speechText = repromptText = e.specError();
		}
		
		if(vals!=null) {
			speechText+=("Protein: "+ vals.get("Protein") +
						"Fat: "+ vals.get("Fat") +
						"Carbohydrate: "+ vals.get("Carbohydrate")+
						"Calorie: "+vals.get("Calorie")
						);
			repromptText=speechText;
		}
		
		return input.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();
	}
}