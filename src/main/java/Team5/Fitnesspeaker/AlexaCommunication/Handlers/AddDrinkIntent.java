package Team5.Fitnesspeaker.AlexaCommunication.Handlers;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static com.amazon.ask.request.Predicates.intentName;

public class AddDrinkIntent implements RequestHandler {
	public static final String NUMBER_SLOT = "Number";
    @Override
    public boolean canHandle(HandlerInput i) {
        return i.matches(intentName("addDrinkIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput i) {
        Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().get(NUMBER_SLOT);
        String speechText, repromptText;
        
        if (NumberSlot == null ) {
			speechText = "I'm not sure how many glasses you drank. Please tell me again";
			repromptText = "I will repeat, I'm not sure how many glasses you drank. Please tell me again";
		} else {
			int added_num_of_glasses = Integer.parseInt(NumberSlot.getValue());
			Map<String, Object> items = new TreeMap<String, Object>(
					i.getAttributesManager().getSessionAttributes());
			Integer counter=items.get("Drink")==null? Integer.valueOf(added_num_of_glasses):
				Integer.valueOf(((Integer)items.get("Drink")).intValue()+added_num_of_glasses);
			items.put("Drink", counter);
			i.getAttributesManager().setSessionAttributes(items);
			if(added_num_of_glasses==1) speechText = String.format("you added one glass of water. You can ask me how many glasses you drank so far saying, "
					+ "how many glasses of water i drank so far?",
					counter);
			else
			speechText = String.format("you added %d glasses of water. You can ask me how many glasses you drank so far saying, "
					+ "how many cups of water i drank so far?",
					Integer.valueOf(added_num_of_glasses));
			repromptText = "I will repeat, You can ask me how many you drank so far saying, how many cups of water i drank so far?";
		}

        return i.getResponseBuilder()
                .withSimpleCard("FitnessSpeakerSession", speechText)
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .withShouldEndSession(Boolean.FALSE)
                .build();
    }

}