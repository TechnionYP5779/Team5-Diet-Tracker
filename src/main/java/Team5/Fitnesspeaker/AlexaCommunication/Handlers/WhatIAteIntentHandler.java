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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Portion;
import Utils.Portion.Type;

public class WhatIAteIntentHandler implements RequestHandler {
	
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("WhatIAteIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		
		String speechText = "";
		
		// initialize database object with the user mail
		DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());
		
		List<Portion> FoodList=new LinkedList<>();
		
		//retrieving the information
		try {
			FoodList=db.DBGetTodayFoodList().stream().map(p->p.getValue()).filter(p->p.getType()==Type.FOOD).collect(Collectors.toList());
		} catch (DBException e) {
			// no need to do anything
		}
		
		Portion.Meal m = FoodList.get(0).getMeal();
		speechText += "For " + m.name();
		for (final Portion p : FoodList) {
			if (p.getMeal() != m) {
				speechText += "For " + p.getMeal().name();
				m = p.getMeal();
			}
			String[] splited2 =p.getTime().toString().split(" ")[3].split(":");
			speechText += ", at "+Integer.parseInt(splited2[0]) + ":" + Integer.parseInt(splited2[1])+" you ate " + Integer.valueOf((int) p.getAmount()) + " grams of " + p.getName() ;
		}
		
		if (speechText.isEmpty())
			speechText = "you haven't eaten anything today yet. Please Tell me when you do";

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.TRUE).build();

	}
}