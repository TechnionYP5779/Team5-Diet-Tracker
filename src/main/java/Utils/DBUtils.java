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

/** this class communicate with the db for storing/querying information
 * @author Shalev Kuba
 * @since 2018-12-23
 * */
public class DBUtils {

	FirebaseDatabase database;
	String user_mail;

	public static String getDate() {
		final String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	public class DBException extends Throwable {
		private static final long serialVersionUID = 0x6D63E5B0EAF28558L;
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
	public void DBPushFood(final Portion p) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(getDate())
				.child("Food");
		try {
			if (dbRef != null)
				dbRef.push().setValueAsync(p).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new DBException();
		}
	}

	/*
	 * returns the user's portion list (with their keys) at given date if the user's
	 * Food directory is empty it returns empty list
	 */
	public List<Pair<String, Portion>> DBGetDateFoodList(final String date) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(date)
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
			throw new DBException();
		}
		return portionList;
	}

	/*
	 * returns the Today user's portion list (with their keys) if the user's Food
	 * directory is empty it returns empty list
	 */
	public List<Pair<String, Portion>> DBGetTodayFoodList() throws DBException {
		return DBGetDateFoodList(getDate());
	}

	/*
	 * returns the stored portion (which was inserted at the same day) object with
	 * the key food_key or null if it doesn't exists
	 */
	public Portion DBGetFoodByKey(final String food_key) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(getDate())
				.child("Food").child(food_key);
		final List<Portion> portionList = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		if (dbRef == null)
			return portionList.isEmpty() ? null : portionList.get(0);
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
			throw new DBException();
		}
		return portionList.isEmpty() ? null : portionList.get(0);
	}

	/*
	 * add "added_cups" water cups to user counter where added_cups is a given
	 * integer parameter
	 */
	public void DBAddWaterCups(final Integer added_cups) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(getDate())
				.child("Drink");
		final Integer updatedCount = Integer
				.valueOf(added_cups.intValue() + DBGetTodayWaterCups().orElse(Integer.valueOf(0)).intValue());
		try {
			dbRef.setValueAsync(updatedCount).get();
		} catch (ExecutionException | InterruptedException e) {
			// should not get here, if it does, it is database error- nothing we can do
			throw new DBException();
		}
	}

	/*
	 * returns the current water count of the user at given date or an empty
	 * Optional if there is no counter for this user
	 */
	public Optional<Integer> DBGetDateWaterCups(final String date) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(date)
				.child("Drink");
		final List<Integer> DrinkCount = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				final Integer count = s.getValue(Integer.class);
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
			throw new DBException();
		}
		return DrinkCount.isEmpty() ? Optional.empty() : Optional.ofNullable(DrinkCount.get(0));
	}

	/*
	 * returns the today's current water count of the user or an empty Optional if
	 * there is no counter for this user
	 */
	public Optional<Integer> DBGetTodayWaterCups() throws DBException {
		return DBGetDateWaterCups(getDate());
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
	public void DBUpdateTodayDailyInfo(final DailyInfo daily_info) throws DBException {
		DBUpdateDateDailyInfo(daily_info, getDate());
	}

	/*
	 * update dailyInfo of given day to the given object
	 */
	public void DBUpdateDateDailyInfo(final DailyInfo daily_info, final String day) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(day)
				.child("Daily-Info");
		try {
			dbRef.setValueAsync(daily_info).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new DBException();
		}
	}

	/*
	 * returns dailyInfo of current day
	 */
	public DailyInfo DBGetTodayDailyInfo() throws DBException {
		return DBGetDateDailyInfo(getDate());
	}

	/*
	 * returns dailyInfo of given day
	 */
	public DailyInfo DBGetDateDailyInfo(final String day) throws DBException {
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
			throw new DBException();
		}
		return daily_info.isEmpty() ? null : daily_info.get(0);
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
	public UserInfo DBGetUserInfo() throws DBException {
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
			throw new DBException();
		}
		return user_info.isEmpty() ? null : user_info.get(0);
	}

	/*
	 * add "added_cups" coffee cups to user counter where added_cups is a given
	 * integer parameter
	 */
	public void DBAddCoffeeCups(final Integer added_cups) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(getDate())
				.child("Coffee");
		final Integer updatedCount = Integer
				.valueOf(added_cups.intValue() + DBGetTodayCofeeCups().orElse(Integer.valueOf(0)).intValue());
		try {
			dbRef.setValueAsync(updatedCount).get();
		} catch (ExecutionException | InterruptedException e) {
			// should not get here, if it does, it is database error- nothing we can do
			throw new DBException();
		}
	}

	/*
	 * returns the current coffee count of the user at given date or an empty
	 * Optional if there is no counter for this user
	 */
	public Optional<Integer> DBGetDateCofeeCups(final String date) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(date)
				.child("Coffee");
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
			throw new DBException();
		}
		return DrinkCount.isEmpty() ? Optional.empty() : Optional.ofNullable(DrinkCount.get(0));
	}

	/*
	 * returns the current coffee count of the user (today) or an empty Optional if
	 * there is no counter for this user
	 */
	public Optional<Integer> DBGetTodayCofeeCups() throws DBException {
		return DBGetDateCofeeCups(getDate());
	}

	/*
	 * add "added_cigarettes" cigarettes to user counter where added_cigarettes is a
	 * given integer parameter
	 */
	public void DBAddCigarettes(final Integer added_cigarettes) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(getDate())
				.child("Cigarettes");
		final Integer updatedCount = Integer.valueOf(
				added_cigarettes.intValue() + DBGetTodayCigarettesCount().orElse(Integer.valueOf(0)).intValue());
		try {
			dbRef.setValueAsync(updatedCount).get();
		} catch (ExecutionException | InterruptedException e) {
			// should not get here, if it does, it is database error- nothing we can do
			throw new DBException();
		}
	}

	/*
	 * returns the current cigarettes count of the user at given date or an empty
	 * Optional if there is no counter for this user
	 */
	public Optional<Integer> DBGetDateCigarettesCount(final String date) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(date)
				.child("Cigarettes");
		final List<Integer> SmokeCount = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				final Integer count = s.getValue(Integer.class);
				if (count != null)
					SmokeCount.add(count);
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
			throw new DBException();
		}
		return SmokeCount.isEmpty() ? Optional.empty() : Optional.ofNullable(SmokeCount.get(0));
	}

	/*
	 * returns the current cigarettes count of the user (today) or an empty Optional
	 * if there is no counter for this user
	 */
	public Optional<Integer> DBGetTodayCigarettesCount() throws DBException {
		return DBGetDateCigarettesCount(getDate());
	}

	/*
	 * Pushes a given alcohol to user Alcohol directory.
	 */
	public void DBPushAlcohol(final Portion p) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(getDate())
				.child("Alcohol");
		try {
			if (dbRef != null)
				dbRef.push().setValueAsync(p).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new DBException();
		}
	}

	/*
	 * returns the user's portion list (with their keys) if the user's Alcohol
	 * directory is empty it returns empty list
	 */
	public List<Pair<String, Portion>> DBGetTodayAlcoholList() throws DBException {
		return DBGetDateAlcoholList(getDate());
	}

	/*
	 * returns the user's portion list (with their keys) if the user's Alcohol
	 * directory is empty it returns empty list
	 */
	public List<Pair<String, Portion>> DBGetDateAlcoholList(final String date) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(date)
				.child("Alcohol");
		final List<Pair<String, Portion>> portionList = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot Snapshot : s.getChildren())
					portionList.add(new Pair<>(Snapshot.getKey(), Snapshot.getValue(Portion.class)));
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
			throw new DBException();
		}
		return portionList;
	}

	/*
	 * Pushes a given measure to user Blood Pressure directory.
	 */
	public void DBPushBloodPressureMeasure(final BloodPressure p) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(getDate())
				.child("BloodPressure");
		try {
			if (dbRef != null)
				dbRef.push().setValueAsync(p).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new DBException();
		}
	}

	/*
	 * returns the user's blood pressure measure list (with their keys) if the
	 * user's blood pressure directory is empty it returns empty list
	 */
	public List<Pair<String, BloodPressure>> DBGetTodayBloodPressureMeasuresList() throws DBException {
		return DBGetDateBloodPressureMeasuresList(getDate());
	}

	/*
	 * returns the user's blood pressure measure list (with their keys) if the
	 * user's blood pressure directory is empty it returns empty list
	 */
	public List<Pair<String, BloodPressure>> DBGetDateBloodPressureMeasuresList(final String date) throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates").child(date)
				.child("BloodPressure");
		final List<Pair<String, BloodPressure>> BloodpressureList = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot Snapshot : s.getChildren())
					BloodpressureList.add(new Pair<>(Snapshot.getKey(), Snapshot.getValue(BloodPressure.class)));
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
			throw new DBException();
		}
		return BloodpressureList;
	}

	/*
	 * Get the User day List
	 */
	public List<String> DBGetDates() throws DBException {
		final DatabaseReference dbRef = database.getReference().child(user_mail).child("Dates");
		final List<String> dayList = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot Snapshot : s.getChildren())
					dayList.add(Snapshot.getKey());
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
			throw new DBException();
		}
		return dayList;
	}
}
