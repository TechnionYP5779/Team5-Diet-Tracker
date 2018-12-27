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

import java.io.FileInputStream;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
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

public class LaunchRequestHandler implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(requestType(LaunchRequest.class));
	}
	public static String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}
	@Override
	public Optional<Response> handle(final HandlerInput i) {

		OffsetDateTime CurrentTime=OffsetDateTime.now();
		int hour=CurrentTime.getHour();
		String selected=", i'm listening";
		String partOfDay="morning";
		String meal="breakfast";
		
		if(hour>=14&&hour<=20) {
			partOfDay="afternoon";
			meal="lunch";
		}else if(hour>=20&&hour<=23||hour>=0&&hour<=7) {
			partOfDay="night";
			meal="dinner";
		}
		int lastHour=getLastHour(i);
		if(lastHour<0||(lastHour>=0&&hour >= 4&&lastHour <= hour - 4))
					selected = ", please tell me what did you eat for " + meal;
		
		final String speechText = "good "+partOfDay+" "+i.getServiceClientFactory().getUpsService().getProfileGivenName()
				+selected;
		final String repromptText = "I will repeat, " + speechText;
		
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(repromptText).build();
	}
	
	@SuppressWarnings("deprecation")
	private static int getLastHour(final HandlerInput i) {

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
				System.out.println(e1.getMessage());
			}
			final FirebaseDatabase database = FirebaseDatabase.getInstance();
			if (database != null)
				dbRef = database.getReference().child(UserMail).child("Dates").child(getDate()).child("Food");
		} catch (final Exception e) {
			System.out.println(e.getMessage());// its ok
		}

			final List<Portion> FoodList = new LinkedList<>();
			final CountDownLatch done = new CountDownLatch(1);
			dbRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(final DataSnapshot s) {
					for (final DataSnapshot portionSnapshot : s.getChildren()) {
						final Portion portion = portionSnapshot.getValue(Portion.class);
							FoodList.add(portion);

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
				return -1;
			else {
				Date last=FoodList.get(0).getTime();
				for(Portion p:FoodList)
					if (p.getTime().after(last))
						last = p.getTime();
				return last.getHours();
			}
			
				
		}
		
}