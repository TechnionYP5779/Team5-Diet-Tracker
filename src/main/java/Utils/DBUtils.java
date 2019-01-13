package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

/**
 *
 * @author ShalevKuba
 * @date 12-23-2018
 */
public class DBUtils {

	FirebaseDatabase database;
	String user_mail;

	private static String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	/*
	 * database's Constructor. Creates a DBUtils instance for user_mail which is the
	 * user mail
	 */
	public DBUtils(final String user_mail) {
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
		this.user_mail = String.valueOf(user_mail).replace(".", "_dot_");
	}

	/*
	 * Pushes a given portion to user Food directory.
	 */
	public void DBPushFood(final Portion p) {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(getDate())
				.child("Food");
		try {
			if (dbRef != null)
				dbRef.push().setValueAsync(p).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	/*
	 * returns the user's portion list (with their keys) if the user's Food
	 * directory is empty it returns empty list
	 */
	public List<Pair<String, Portion>> DBGetFoodList() {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(getDate())
				.child("Food");
		final List<Pair<String, Portion>> portionList = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot portionSnapshot : s.getChildren())
					portionList.add(new Pair<>(portionSnapshot.getKey(), portionSnapshot.getValue(Portion.class)));
				done.countDown();
			}

			@Override
			public void onCancelled(final DatabaseError e) {
				System.out.println("The read failed: " + e.getCode());
			}

		});
		try {
			done.await();
		} catch (final InterruptedException e1) {
			// should not get here, if it does, it is database error- nothing we can do
			e1.printStackTrace();
		}
		return portionList;
	}

	/*
	 * returns the stored portion object with the key food_key or null if it doesn't
	 * exists
	 */
	public Portion DBGetFoodByKey(final String food_key) {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(getDate())
				.child("Food").child(food_key);
		final List<Portion> portionList = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		if (dbRef != null) {
			dbRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(final DataSnapshot s) {
					portionList.add(s.getValue(Portion.class));
					done.countDown();
				}

				@Override
				public void onCancelled(final DatabaseError e) {
					System.out.println("The read failed: " + e.getCode());
				}
			});
			try {
				done.await();
			} catch (final InterruptedException e1) {
				// should not get here, if it does, it is database error- nothing we can do
				e1.printStackTrace();
			}
		}

		if (!portionList.isEmpty())
			return portionList.get(0);
		return null;
	}

	/*
	 * add "added_cups" water cups to user counter where added_cups is a given
	 * integer parameter
	 */
	public void DBAddWaterCups(final Integer added_cups) {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(getDate())
				.child("Drink");
		final Integer updatedCount = Integer
				.valueOf(added_cups.intValue() + DBGetWaterCups().orElse(Integer.valueOf(0)).intValue());
		try {
			dbRef.setValueAsync(updatedCount).get();
		} catch (ExecutionException | InterruptedException e) {
			// should not get here, if it does, it is database error- nothing we can do
			e.printStackTrace();
		}
	}

	/*
	 * returns the current water count of the user or an empty Optional if there is
	 * no counter for this user
	 */
	public Optional<Integer> DBGetWaterCups() {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(getDate())
				.child("Drink");
		final List<Integer> DrinkCount = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				final Integer count = s.getValue(Integer.class);
				if (count != null)
					DrinkCount.add(count);
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
			// should not get here, if it does, it is database error- nothing we can do
			e.printStackTrace();
		}
		if (DrinkCount.isEmpty())
			return Optional.empty();
		return Optional.ofNullable(DrinkCount.get(0));
	}

	/*
	 * removes the user directory, BE CAREFUL WITH THIS!!!
	 */
	public void DBUtilsRemoveUserDirectory() {
		try {
			this.database.getReference().child(this.user_mail).removeValueAsync().get();
		} catch (ExecutionException | InterruptedException e) {
			// should not happen
		}
	}

	/*
	 * update dailyInfo of current day to the given object
	 */
	public void DBUpdateTodayDailyInfo(final DailyInfo daily_info) {
		DBUpdateDateDailyInfo(daily_info, getDate());
	}

	/*
	 * update dailyInfo of given day to the given object
	 */
	public void DBUpdateDateDailyInfo(final DailyInfo daily_info, final String day) {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(day)
				.child("Daily-Info");
		try {
			dbRef.setValueAsync(daily_info).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	/*
	 * returns dailyInfo of current day
	 */
	public DailyInfo DBGetTodayDailyInfo() {
		return DBGetDateDailyInfo(getDate());
	}

	/*
	 * returns dailyInfo of given day
	 */
	public DailyInfo DBGetDateDailyInfo(final String day) {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(day)
				.child("Daily-Info");
		final List<DailyInfo> daily_info = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				daily_info.add(s.getValue(DailyInfo.class));
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
			// should not get here, if it does, it is database error- nothing we can do
			e.printStackTrace();
		}
		if (daily_info.isEmpty())
			return null;
		return daily_info.get(0);
	}
	
	/*
	 * update UserInfo of to the given object
	 */
	public void DBUpdateUserInfo(final UserInfo user_info) {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("User-Info");
		try {
			dbRef.setValueAsync(user_info).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * returns UserInfo
	 */
	public UserInfo DBGetUserInfo() {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("User-Info");
		final List<UserInfo> user_info = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				user_info.add(s.getValue(UserInfo.class));
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
			// should not get here, if it does, it is database error- nothing we can do
			e.printStackTrace();
		}
		if (user_info.isEmpty())
			return null;
		return user_info.get(0);
	}


}
