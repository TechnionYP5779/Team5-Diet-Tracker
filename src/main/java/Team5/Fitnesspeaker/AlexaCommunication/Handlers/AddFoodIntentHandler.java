/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at
         http://aws.amazon.com/apache2.0/
     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Portion.Type;
import Utils.PortionRequestGen;

public class AddFoodIntentHandler implements RequestHandler {
	public static final String AMOUNT_SLOT = "Number";
	public static final String FOOD_SLOT = "Food";
	public static final String UNIT_SLOT = "Unit";
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("AddFoodIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot foodSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(FOOD_SLOT);

		final Slot AmountSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(AMOUNT_SLOT);
		
		final Slot UnitSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(UNIT_SLOT);

		String speechText, repromptText="";

		if (foodSlot == null) {
			speechText = "I'm not sure what you ate. Please tell me again";
			repromptText = "I will repeat, I'm not sure what you ate. Please tell me again";
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
		}

		if (AmountSlot == null) {
			speechText = "I'm not sure how much you ate. Please tell me again";
			repromptText = "I will repeat, I'm not sure how much you ate. Please tell me again";
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
		}
		
		if (UnitSlot == null) {
			speechText = "I'm not sure about the units. Please tell me again";
			repromptText = "I will repeat, I'm not sure about the units. Please tell me again";
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
		}

		Integer amount = Integer.valueOf(Integer.parseInt(AmountSlot.getValue()));
		String units = UnitSlot.getValue();
		String added_food = foodSlot.getValue();

		// initialize database object with the user mail
		DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());
		
		//insert the portion to the DB
		try {
			db.DBPushFood(PortionRequestGen.generatePortionWithAmount(added_food, Type.FOOD, Double.valueOf(amount.intValue()).doubleValue(), units));
		} catch (DBException e) {
			speechText = String.format("There was a problem with the portion logging, Please tell me again");
			repromptText = String.format("I'll repeat, there was a problem with the portion logging,  Please tell me again");
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
		/** right now, the only other specific option we tke care of is the option 
		 * that we didn't find the portion units in the DB or in our modules.
		 */
		} catch (Exception e) {
			speechText = String.format("There was a problem with the units you said, Please try to supply the food in a different unit, for example, in grams");
			repromptText = String.format("I'll repeat, there was a problem with the units you said, Please try to supply the food in a different unit, for example, in grams");
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
					.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
		}
		
		speechText = String.format("You logged %d %s of %s, bon appetit!", amount, units, added_food);

		//the Boolean.TRUE says that the Alexa will end the session 
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.TRUE).build();
	}

}