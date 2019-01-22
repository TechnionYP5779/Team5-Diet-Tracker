package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import Utils.DBUtils;
import Utils.PortionRequestGen;
import Utils.DBUtils.DBException;
import Utils.Portion.Type;

public class AddDrinkIntent implements RequestHandler {
	public static final String ADD_COUNT_SLOT = "Number";
	public static final String DRINK_NAME_SLOT = "drink";

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("addDrinkIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(ADD_COUNT_SLOT);
		final Slot DrinkSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(DRINK_NAME_SLOT);
		String speechText, repromptText = "";

		if (NumberSlot == null) {
			speechText = "I'm not sure how many cups you drank. Please tell me again";
			repromptText = "I will repeat, I'm not sure how many cups you drank. Please tell me again";
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
		}
		
		if (DrinkSlot == null) {
			speechText = "I'm not sure what did you  drink. Please tell me again";
			repromptText = "I will repeat, I'm not sure what did you  drink. Please tell me again";
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
		}

		int added_num_of_cups = Integer.parseInt(NumberSlot.getValue());
		String drink_name=DrinkSlot.getValue();
		// initialize database object with the user mail
		DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());
		
		//log the drinking
		try {
			if(drink_name.equals("water"))
				db.DBAddWaterCups(Integer.valueOf(added_num_of_cups));
			else {
				db.DBPushFood(PortionRequestGen.generatePortionWithAmount(drink_name, Type.DRINK, Double.valueOf(added_num_of_cups*8).doubleValue(), "fl oz"));	
				if(drink_name.contains("coffee"))
					db.DBAddCoffeeCups(Integer.valueOf(added_num_of_cups));
			}
		} catch (DBException e) {
			speechText = String.format("There was a problem with the water logging, Please tell me again");
			repromptText = String.format("I'll repeat, there was a problem with the water logging,  Please tell me again");
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
		}

		if (added_num_of_cups == 1)
			speechText = String.format("You logged one cup of %s, to your health!",drink_name);
		else
			speechText = String.format("You logged %d cups of %s, to your health!",
					Integer.valueOf(added_num_of_cups),drink_name);

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();
	}

}