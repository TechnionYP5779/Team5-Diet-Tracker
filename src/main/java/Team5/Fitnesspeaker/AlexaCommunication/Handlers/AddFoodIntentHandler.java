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

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

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

public class AddFoodIntentHandler implements RequestHandler {
	public static final String NUMBER_SLOT = "Number";
	
	public static String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}
	
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("AddFoodIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot favoriteFoodSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(FOOD_SLOT);
		
		final Slot NumberSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(NUMBER_SLOT);
		Integer grams=NumberSlot == null ? Integer.valueOf(100) : Integer.valueOf(Integer.parseInt(NumberSlot.getValue()));
		String speechText = "", repromptText;
		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		DatabaseReference dbRef = null;
		try {
			FileInputStream serviceAccount;
			FirebaseOptions options = null;
			try {
				serviceAccount = new FileInputStream("db_credentials.json");
				options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://fitnesspeaker-6eee9.firebaseio.com/").build();
				FirebaseApp.initializeApp(options);
			} catch (final Exception e1) {
				speechText += e1.getMessage() + " ";// its ok
			}
			final FirebaseDatabase database = FirebaseDatabase.getInstance();
			if (database != null)
				dbRef = database.getReference().child(UserMail).child("Dates").child(getDate()).child("Food");
		} catch (final Exception e) {
			speechText += e.getMessage() + " ";// its ok
		}

		if (favoriteFoodSlot == null || favoriteFoodSlot.getResolutions() == null
				|| !favoriteFoodSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")
						&& !favoriteFoodSlot.getResolutions().toString().contains("ER_SUCCESS_NO_MATCH")) {
			speechText = "You need to provide a valid food. Please try again.";
			repromptText = "I will repeat, I'm not sure what you ate. Please tell me what you ate, for example, i ate twenty grams of pasta.";
		} else {
			final String added_food = favoriteFoodSlot.getValue();
			final List<Portion> FoodList = new LinkedList<>();
			final List<String> FoodId = new LinkedList<>();
			final CountDownLatch done = new CountDownLatch(1);
			dbRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(final DataSnapshot s) {
					for (final DataSnapshot portionSnapshot : s.getChildren()) {
						final Portion portion = portionSnapshot.getValue(Portion.class);
						if (portion.getName().equals(added_food)) {
							FoodList.add(portion);
							FoodId.add(portionSnapshot.getKey());

						}

					}
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

			if (FoodList.isEmpty())
				try {
					if (dbRef != null)
						dbRef.push().setValueAsync(PortionRequestGen.generatePortionWithAmount(added_food, Type.FOOD,grams)).get();
				} catch (InterruptedException | ExecutionException e) {
					speechText += e.getMessage() + " ";
				}
			else
				try {
					FirebaseDatabase.getInstance().getReference().child(UserMail).child("Dates").child(getDate()).child("Food").child(FoodId.get(0))
							.setValueAsync(new Portion(Type.FOOD, added_food,Double.valueOf(grams.intValue()).doubleValue() + FoodList.get(0).getAmount(),
									FoodList.get(0).getCalories_per_100_grams(),
									FoodList.get(0).getProteins_per_100_grams(),
									FoodList.get(0).getCarbs_per_100_grams(), FoodList.get(0).getFats_per_100_grams()))
							.get();
				} catch (final InterruptedException e) {
					e.printStackTrace();
				} catch (final ExecutionException e) {
					e.printStackTrace();
				}
			speechText = String.format("you added %s, You can ask me what you have eaten so far saying, what did i eat?",
					added_food);
			repromptText = "You can ask me what you have eaten so far saying, what did i eat?";
		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).withShouldEndSession(Boolean.FALSE).build();
	}

}