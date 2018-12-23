package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;


import com.amazon.ask.model.services.Pair;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Utils.Portion.Type;

/**
 * 
 * @author ShalevKuba
 * @date 12-23-2018
 */
public class DBUtils {

	FirebaseDatabase database;
	String user_mail;

	/*
	 * database's Constructor. Creates a DBUtils instance for user_mail which is the
	 * user mail
	 */
	public DBUtils(String user_mail) {
		try {
			FileInputStream serviceAccount;
			FirebaseOptions options = null;
			serviceAccount = new FileInputStream("db_credentials.json");
			options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://fitnesspeaker-6eee9.firebaseio.com/").build();
			FirebaseApp.initializeApp(options);
		} catch (IllegalStateException | IOException e) {
			if (!e.getClass().equals(IllegalStateException.class))
				e.printStackTrace();
		}
		database = FirebaseDatabase.getInstance();
		this.user_mail = String.valueOf(user_mail);
	}

	/*
	 * Pushes a given portion to user Food directory if this portion already exists,
	 * this function only adds the given amount to the current portion's consumption
	 * amount.
	 */
	public void DBPushFood(Portion p) {
		DatabaseReference dbRef = this.database.getReference().child(this.user_mail).child("Food");
		String added_food=p.getName();
		final List<Pair<String,Portion>> portionList = this.DBGetFoodList();
		boolean updated=false;
		for(Pair<String,Portion> pair:portionList)
			if(pair.getValue().getName().equals(added_food)) {
				try {
					dbRef.child(pair.getName()).setValueAsync(
							new Portion(Type.FOOD, added_food,
							p.getAmount() + pair.getValue().getAmount(),
							pair.getValue().getCalories_per_100_grams(),
							pair.getValue().getProteins_per_100_grams(),
							pair.getValue().getCarbs_per_100_grams(), 
							pair.getValue().getFats_per_100_grams())).get();
					updated=true;
				} catch (InterruptedException | ExecutionException e) {
					// should not get here unless there is database error
					e.printStackTrace();
				}
				break;
			}
		if(!updated)
			try {
				if (dbRef != null)
					dbRef.push().setValueAsync(PortionRequestGen.generatePortionWithAmount(added_food, Type.FOOD,
							Integer.valueOf((int) p.getAmount()))).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		
	}

	/*
	 * returns the user's portion list (with their keys)
	 * if the user's Food directory is empty it returns empty list
	 */
	public List<Pair<String,Portion>> DBGetFoodList() {
		DatabaseReference dbRef = this.database.getReference().child(this.user_mail).child("Food");
		final List<Pair<String,Portion>> portionList = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot s) {
				for (final DataSnapshot portionSnapshot : s.getChildren())
					portionList.add(new Pair<String,Portion>(portionSnapshot.getKey(), 
							portionSnapshot.getValue(Portion.class)));
				done.countDown();
			}

			@Override
			public void onCancelled(DatabaseError e) {
				System.out.println("The read failed: " + e.getCode());
			}

		});
		try {
			done.await();
		} catch (InterruptedException e1) {
			// should not get here, if it does, it is database error- nothing we can do
			e1.printStackTrace();
		}
		return portionList;
	}
	

}
