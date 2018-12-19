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
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Utils.Portion;
import Utils.Portion.Type;
import Utils.PortionRequestGen;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import static Team5.Fitnesspeaker.AlexaCommunication.Handlers.WhatIAteIntentHandler.FOOD_SLOT;
import static com.amazon.ask.request.Predicates.intentName;
import static org.junit.Assert.assertNotNull;

public class AddFoodIntentHandler implements RequestHandler {
	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("AddFoodIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		Slot favoriteFoodSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(FOOD_SLOT);
		String speechText = "", repromptText;
		String UserMail = "shalev@gmail";
		DatabaseReference dbRef = null;
		try {
			FileInputStream serviceAccount;
			FirebaseOptions options = null;
			try {
				serviceAccount = new FileInputStream("db_credentials.json");
				options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://fitnesspeaker.firebaseio.com/").build();
				FirebaseApp.initializeApp(options);
			} catch (Exception e1) {
				speechText += e1.getMessage() + " ";// its ok
			}
			FirebaseDatabase database = FirebaseDatabase.getInstance();
			if (database != null)
				dbRef = database.getReference().child(UserMail).child("Food");
		} catch (Exception e) {
			speechText += e.getMessage() + " ";// its ok
		}

		if (favoriteFoodSlot == null || favoriteFoodSlot.getResolutions() == null
				|| (!favoriteFoodSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")
						&& !favoriteFoodSlot.getResolutions().toString().contains("ER_SUCCESS_NO_MATCH"))) {
			speechText = "You need to provide a valid food. Please try again.";
			repromptText = "I will repeat, I'm not sure what you ate. Please tell me what you ate, for example, i ate pasta.";
		} else {
			String added_food = favoriteFoodSlot.getValue();
			final List<Portion> FoodList = new LinkedList();
			final List<String> FoodId = new LinkedList();
			CountDownLatch done = new CountDownLatch(1);
			dbRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					for (DataSnapshot portionSnapshot : dataSnapshot.getChildren()) {
						Portion portion = portionSnapshot.getValue(Portion.class);
						if (portion.getName().equals(added_food)) {
							FoodList.add(portion);
							FoodId.add(portionSnapshot.getKey());
							
						}

					}
					done.countDown();

				}

				@Override
				public void onCancelled(DatabaseError databaseError) {
					System.out.println("The read failed: " + databaseError.getCode());
				}
			});
			try {
				done.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}

			if (FoodList.isEmpty()) {
				try {
					if (dbRef != null)
						dbRef.push().setValueAsync(PortionRequestGen.generatePortion(added_food, Type.FOOD)).get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					speechText += e.getMessage() + " ";
				}
			}else {
				try {
					FirebaseDatabase.getInstance().getReference().child(UserMail).child("Food")
					.child(FoodId.get(0)).setValueAsync(new Portion(Type.FOOD, added_food,
							100 + FoodList.get(0).getAmount(), FoodList.get(0).getCalories_per_100_grams(),
							FoodList.get(0).getProteins_per_100_grams(), FoodList.get(0).getCarbs_per_100_grams(),
							FoodList.get(0).getFats_per_100_grams())).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//Map<String, Object> items = new TreeMap<String, Object>(i.getAttributesManager().getSessionAttributes());
			//Integer count = items.get("Food-" + added_food) == null ? Integer.valueOf(1)
			//		: (Integer.valueOf(((Integer) items.get("Food-" + added_food)).intValue() + 1));
			//items.put("Food-" + added_food, Integer.valueOf(count.intValue()));
			//i.getAttributesManager().setSessionAttributes(items);
			speechText = String.format(
					"you added %s, You can ask me what you ate so far saying, what did i eat?",
					added_food);
			repromptText = "You can ask me what you ate so far saying, what did i eat?";
		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}

}