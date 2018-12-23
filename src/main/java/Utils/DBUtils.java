package Utils;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class DBUtils {

	public static void DBInitialization() {
		try {
			FileInputStream serviceAccount;
			FirebaseOptions options = null;
			serviceAccount = new FileInputStream("db_credentials.json");
			options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://fitnesspeaker-6eee9.firebaseio.com/").build();
			FirebaseApp.initializeApp(options);
		} catch (IllegalStateException | IOException  e) {
			if(!e.getClass().equals(IllegalStateException.class)) e.printStackTrace();
		}
	}
	
	

}
