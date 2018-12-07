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

import static Team5.Fitnesspeaker.AlexaCommunication.Handlers.WhatIAteIntentHandler.FOOD_SLOT;
import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class AddFoodIntentHandler implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("AddFoodIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot favoriteFoodSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(FOOD_SLOT);
		String speechText, repromptText;

		if (favoriteFoodSlot == null || favoriteFoodSlot.getResolutions() == null
				|| !favoriteFoodSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")
						&& !favoriteFoodSlot.getResolutions().toString().contains("ER_SUCCESS_NO_MATCH")) {
			speechText = "You need to provide a valid food. Please try again.";
			repromptText = "I will repeat, I'm not sure what you ate. Please tell me what you ate, for example, i ate pasta.";
		} else {
			final String added_food = favoriteFoodSlot.getValue();
			final Map<String, Object> items = new TreeMap<>(i.getAttributesManager().getSessionAttributes());
			items.put(added_food, added_food);
			i.getAttributesManager().setSessionAttributes(items);
			speechText = String.format("you added %s. You can ask me what you ate so far saying, what did i eat?",
					added_food);
			repromptText = "You can ask me what you ate so far saying, what did i eat?";
		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}

}