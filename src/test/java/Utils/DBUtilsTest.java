package Utils;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DBUtilsTest {

	@Test
	public void testDrinkInsert() {
		String UserMail = "shalev@gmail";
		DatabaseReference dbRef = null;
		try {
			FileInputStream serviceAccount;
			FirebaseOptions options = null;
			try {
				serviceAccount = new FileInputStream("credentials/db_credentials.json");
				options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://fitnesspeaker.firebaseio.com/").build();
				FirebaseApp.initializeApp(options);
			} catch (Exception e1) {
				//
			}
			FirebaseDatabase database = FirebaseDatabase.getInstance();
			if (database != null) {
				dbRef = database.getReference().child(UserMail).child("Drink");
			}
		} catch (Exception e) {
			//
		}
		
		final List<Long> DrinkCount = new LinkedList();
		DrinkCount.add(Long.valueOf(2));
		CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Long count = dataSnapshot.getValue(Long.class);
				if(count!=null) DrinkCount.set(0, Long.valueOf(count.longValue() + DrinkCount.get(0).longValue()));
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

		if (dbRef != null)
			try {
				dbRef.setValueAsync(DrinkCount.get(0)).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
