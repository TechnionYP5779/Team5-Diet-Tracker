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

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

public class WhatIAteIntentHandler implements RequestHandler {
	public static final String FOOD_SLOT = "Food";
	public static final int START_INDEX_OF_FOOD =5;
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("WhatIAteIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput input) {
		String speechText;
		Map<String, Object> items = new TreeMap<String, Object>(
				input.getAttributesManager().getSessionAttributes());
		final Set<String> food_set = items.keySet();
		String foods_eaten = "";
		int i = 0;
		for (final String key : food_set)
			if (key.startsWith("Food-")) {
				String val = key.substring(START_INDEX_OF_FOOD);
				Integer count = (Integer)items.get(key);
				if(i!=0&&i< food_set.size() - 1) foods_eaten+=", ";
				else if(i==food_set.size() - 1 && i!=0) foods_eaten+=", and ";
				foods_eaten+=val;
				if (count.intValue()> 1)
					foods_eaten += String.format(" %d times",count);
				++i;
			}

		if (!foods_eaten.isEmpty())
			speechText = String.format("You ate %s.", foods_eaten);
		else
			speechText = "you ate nothing. you can tell me what you ate, for example, i ate pasta.";

		return input.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}