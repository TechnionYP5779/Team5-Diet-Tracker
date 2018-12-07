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
import java.util.Set;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

public class WhatIAteIntentHandler implements RequestHandler {
	public static final String FOOD_SLOT = "Food";

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("WhatIAteIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput input) {
		String speechText;
		final Set<String> food_set = input.getAttributesManager().getSessionAttributes().keySet();
		String foods_eaten = "";
		int i = 0;
		for (final String key : food_set) {
			if (i == 0)
				foods_eaten += key + ", ";
			else if (i < food_set.size() - 1)
				foods_eaten += ", " + key + " ";
			else
				foods_eaten += "and " + key;
			++i;
		}

		if (!foods_eaten.isEmpty())
			speechText = String.format("You ate %s.", foods_eaten);
		else
			speechText = "I'm not sure what you ate. you can tell me what you ate, for example, i ate pasta.";

		return input.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}