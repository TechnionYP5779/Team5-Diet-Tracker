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

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static Team5.Fitnesspeaker.AlexaCommunication.Handlers.WhatIAteIntentHandler.FOOD_SLOT;
import static com.amazon.ask.request.Predicates.intentName;

public class AddFoodIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput i) {
        return i.matches(intentName("AddFoodIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput i) {
        Slot favoriteFoodSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().get(FOOD_SLOT);
        String speechText="", repromptText;
        
        if (favoriteFoodSlot == null || favoriteFoodSlot.getResolutions() == null
				|| (!favoriteFoodSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")
						&& !favoriteFoodSlot.getResolutions().toString().contains("ER_SUCCESS_NO_MATCH"))) {
			speechText = "You need to provide a valid food. Please try again.";
			repromptText = "I will repeat, I'm not sure what you ate. Please tell me what you ate, for example, i ate pasta.";
		} else {
			String added_food = favoriteFoodSlot.getValue();
			Map<String, Object> items = new TreeMap<String, Object>(
					i.getAttributesManager().getSessionAttributes());
			Integer count=items.get("Food-" + added_food) == null ? Integer.valueOf(1) : (Integer.valueOf(((Integer) items.get("Food-" + added_food)).intValue() + 1));
			items.put("Food-"+added_food, Integer.valueOf(count.intValue()));
			i.getAttributesManager().setSessionAttributes(items);
			speechText = String.format("you added %s. you ate so far %d of this, You can ask me what you ate so far saying, what did i eat?",
					added_food,count);
			repromptText = "You can ask me what you ate so far saying, what did i eat?";
		}

        return i.getResponseBuilder()
                .withSimpleCard("FitnessSpeakerSession", speechText)
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .withShouldEndSession(Boolean.FALSE)
                .build();
    }

}