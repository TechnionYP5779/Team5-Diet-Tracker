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

import Utils.EmailSender;
import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Utils.Portion;

public class WhatIAteIntentHandler implements RequestHandler {
	public static final String FOOD_SLOT = "Food";
	public static final int START_INDEX_OF_FOOD = 5;

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("WhatIAteIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		// Get a reference to our posts

		final String UserMail = "shalev@gmail";
		try {
			FileInputStream serviceAccount;
			FirebaseOptions options = null;
			try {
				serviceAccount = new FileInputStream("db_credentials.json");
				options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://fitnesspeaker-6eee9.firebaseio.com/").build();
				FirebaseApp.initializeApp(options);
			} catch (final Exception e1) {
				// empty block

			}
		} catch (final Exception e) {
			// empty block

		}
		final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(UserMail).child("Food");

		final List<Portion> FoodList = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot portionSnapshot : s.getChildren())
					FoodList.add(portionSnapshot.getValue(Portion.class));
				done.countDown();

			}

			@Override
			public void onCancelled(final DatabaseError e) {
				System.out.println("The read failed: " + e.getCode());
			}
		});
		try {
			done.await();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
		}

		String speechText;
		// Map<String, Object> items = new TreeMap<String,
		// Object>(input.getAttributesManager().getSessionAttributes());
		// final Set<String> food_set = items.keySet();
		String foods_eaten = "";
		/*
		 * int i = 0; for (final String key : food_set) if (key.startsWith("Food-")) {
		 * String val = key.substring(START_INDEX_OF_FOOD); Integer count =
		 * (Integer)items.get(key); if(i!=0&&i< food_set.size() - 1) foods_eaten+=", ";
		 * else if(i==food_set.size() - 1 && i!=0) foods_eaten+=", and ";
		 * foods_eaten+=val; if (count.intValue()> 1) foods_eaten +=
		 * String.format(" %d times",count); ++i; }
		 */
		for (final Portion p : FoodList)
			foods_eaten += ", " + p.getName() + " " + Integer.valueOf((int) p.getAmount()) + " grams ";
		if (!foods_eaten.isEmpty()) {
			speechText = String.format("You ate %s.", foods_eaten);
			(new EmailSender()).sendMail(speechText, "food eaten", "igor731996@gmail.com");
		}
			
		
		else {
			speechText = "you ate nothing. you can tell me what you ate, for example, i ate pasta.";
			(new EmailSender()).sendMail(speechText, "food eaten", "igor731996@gmail.com");
		}
			

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();

	}
}