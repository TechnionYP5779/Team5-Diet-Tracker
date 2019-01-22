package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.services.Pair;

import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Portion;
import Utils.Portion.Type;

public class HowManyIDrankIntent implements RequestHandler {
	public static final String DRINK_NAME_SLOT = "drink";
	
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("HowMuchIDrankIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot DrinkSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(DRINK_NAME_SLOT);
		String speechText = "", repromptText = "";
		
		if (DrinkSlot == null) {
			speechText = "I'm not sure what drink did you ask for. Please tell me again";
			repromptText = "I will repeat, I'm not sure what drink did you ask for. Please tell me again";
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
		}
		
		String drink_name=DrinkSlot.getValue();
		
		// initialize database object with the user mail
		DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());
		
		//retrieving the information
		Optional<Integer> Count = Optional.empty();
		try {
			if(drink_name.equals("water"))
				Count=db.DBGetTodayWaterCups();
			else if(drink_name.contains("coffee"))
				Count=db.DBGetTodayCofeeCups();
			else {
				List<Pair<String,Portion>> Drinklist= db.DBGetTodayFoodList().stream().
						filter(p->(p.getValue().getName().equals(drink_name))&&(p.getValue().getType()==Type.DRINK)).collect(Collectors.toList());
				int countOz=0;
				for(Pair<String,Portion> p : Drinklist) countOz+=(int)p.getValue().getAmount();
				Count=Optional.ofNullable(Integer.valueOf(countOz/8));
			}
		} catch (DBException e) {
			// no need to do anything
		}

		if ((!Count.isPresent())||Count.get().intValue()<=0)
			speechText = String.format("You haven't drink %s today",drink_name);
		else {
			final Integer count = Count.get();
			if (count.intValue() == 1)
				speechText = String.format("so far, you have drank a single cup of %s",drink_name);
			else
				speechText = String.format("so far, you have drank %d cups of %s", count,drink_name);

		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText).withReprompt(speechText)
				.withShouldEndSession(Boolean.TRUE).build();

	}
}
