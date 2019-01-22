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

import static com.amazon.ask.request.Predicates.requestType;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Portion;
import Utils.Portion.Type;

public class LaunchRequestHandler implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(requestType(LaunchRequest.class));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {

		OffsetDateTime CurrentTime = OffsetDateTime.now();
		int hour = CurrentTime.getHour();
		String selected = ", i'm listening";
		String partOfDay = "morning";
		String meal = "breakfast";

		if (hour >= 10 && hour <= 16) {
			partOfDay = "afternoon";
			meal = "lunch";
		} else if (hour >= 19 && hour <= 23 || hour >= 0 && hour <= 3) {
			partOfDay = "night";
			meal = "dinner";
		} else if (hour >= 16 && hour <= 19) {
			partOfDay = "evening";
			meal = "dinner";
		}
		int lastHour = getLastHour(i);
		if (lastHour < 0 || (lastHour >= 0 && hour >= 4 && lastHour <= hour - 4))
			selected = ", please tell me what did you eat for " + meal;

		final String speechText = "good " + partOfDay + " "
				+ i.getServiceClientFactory().getUpsService().getProfileGivenName() + selected;
		final String repromptText = "I will repeat, " + speechText;

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).build();
	}

	@SuppressWarnings("deprecation")
	private static int getLastHour(final HandlerInput i) {

		// initialize database object with the user mail
		DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());

		List<Portion> FoodList;
		try {
			FoodList = db.DBGetTodayFoodList().stream().filter(p->p.getValue().getType()==Type.FOOD).map(p->p.getValue()).collect(Collectors.toList());
		} catch (DBException e) {
			return -1;
		}
		
		if (FoodList.isEmpty())
			return -1;
		else {
			Date last = FoodList.get(0).getTime();
			for (Portion p : FoodList)
				if (p.getTime().after(last))
					last = p.getTime();
			return last.getHours();
		}

	}

}