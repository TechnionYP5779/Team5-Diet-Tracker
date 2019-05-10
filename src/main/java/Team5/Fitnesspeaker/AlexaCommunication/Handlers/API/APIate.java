package Team5.Fitnesspeaker.AlexaCommunication.Handlers.API;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import Utils.Strings;
import Utils.Strings.DrinkStrings;
import Utils.Strings.SlotString;
import Utils.FoodsDB.FoodsDB;
import Utils.FoodsDB.FoodsDBException;

public class APIate  implements RequestHandler{

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("APIate"));
	}

	private String fit2USDA(String p) {
		String res=p;
	     switch(p) {
		  case "ounces":
		      res="oz";
		      break;
		  case "ounce":
			    res="oz";
			    break;
		  case "tablespoons":
			    res="tbsp";
			    break;
		  case "tablespoon":
			    res="tbsp";
			    break;
		  case "pound":
			    res="lb";
			    break;
		  case "pounds":
			    res="lb";
			    break;
		  case "teaspoon":
			    res="tsp";
			    break;
		  case "teaspoons":
			    res="tsp";
			    break;
		  default:
	}
	     return res;
}
	
	@Override
	public Optional<Response> handle(HandlerInput input) {
		
		String email=input.getServiceClientFactory().getUpsService().getProfileEmail();
		
		Slot numberSlot = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.NUMBER_SLOT);
		Slot  foodSlot = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots()
			    .get(SlotString.FOOD_SLOT);
		
		Slot  portionSlot = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots()
			    .get(SlotString.PORTION_SLOT);
		
		String speechText, repromptText = "";
		if (numberSlot == null && portionSlot==null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, DrinkStrings.TELL_DRINKS_AMOUNT_AGAIN)
					.withSpeech(DrinkStrings.TELL_DRINKS_AMOUNT_AGAIN)
					.withReprompt(DrinkStrings.TELL_DRINKS_AMOUNT_AGAIN_REPEAT).withShouldEndSession(Boolean.FALSE)
					.build();

		if (foodSlot == null)
			return input.getResponseBuilder()
					.withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText = DrinkStrings.TELL_DRINKS_AGAIN)
					.withSpeech(speechText = DrinkStrings.TELL_DRINKS_AGAIN)
					.withReprompt(repromptText = DrinkStrings.TELL_DRINKS_AGAIN_REPEAT)
					.withShouldEndSession(Boolean.FALSE).build();
		
		speechText = repromptText = "logged successfully";
		try {
			FoodsDB db =new FoodsDB();
			if(portionSlot!=null) {
				db.UserAte(email,foodSlot.getValue(), (numberSlot == null) ? 1 : Integer.parseInt(numberSlot.getValue()),fit2USDA(portionSlot.getValue()));
			}
			db.UserAte(email, foodSlot.getValue(), Integer.parseInt(numberSlot.getValue()));
		} catch (FoodsDBException e) {
			speechText = repromptText = e.specError();
		}
		
		return input.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();
	}

}
