package GraphsMaker;

import java.util.ArrayList;
import java.util.Calendar;
import org.junit.Test;

import Utils.simpleGraph;

@SuppressWarnings("static-method")
public class simpleGraphTest {

	@Test
	public void test() {
		simpleGraph g = new simpleGraph();
		ArrayList<Calendar> dates = new ArrayList<>();
		ArrayList<Integer> weights = new ArrayList<>();

		for (int i = 0; i < 7; ++i) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -i);
			dates.add(c);
			weights.add(Integer.valueOf(70 - i));
		}
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -12);
		dates.add(c);
		weights.add(Integer.valueOf(60));
		g.setDates(dates).setWeights(weights).make().save(800, 300, "test");
		// g.delete(); // set as command if needed
	}
	/*
	 * private String[] getDates() { int MAX_DAYS=14; String[] dates = new
	 * String[MAX_DAYS]; Calendar weekDay = Calendar.getInstance();
	 * weekDay.add(Calendar.DATE, -MAX_DAYS); for (int j = 0; j < MAX_DAYS; ++j) {
	 * weekDay.add(Calendar.DATE, 1); String[] date =
	 * weekDay.getTime().toString().split("\\s+"); dates[j] = date[2] + "-" +
	 * date[1] + "-" + date[5]; } return dates; }
	 * 
	 * private void setWeightByDate(String date,String UserMail,int weight) { final
	 * DatabaseReference dbRef =
	 * FirebaseDatabase.getInstance().getReference().child(UserMail)
	 * .child("Dates").child(date).child("Daily-Info");
	 * 
	 * 
	 * final List<DailyInfo> DailyInfoList = new LinkedList<>(); final List<String>
	 * DailyInfoId = new LinkedList<>(); DailyInfo di = new DailyInfo();
	 * di.setWeight(weight); // UserList.add(u); final CountDownLatch done = new
	 * CountDownLatch(1); dbRef.addValueEventListener(new ValueEventListener() {
	 * 
	 * @Override public void onDataChange(final DataSnapshot s) { for (final
	 * DataSnapshot userSnapshot : s.getChildren()) {
	 * DailyInfoList.add(userSnapshot.getValue(DailyInfo.class));
	 * DailyInfoId.add(userSnapshot.getKey()); } done.countDown(); }
	 * 
	 * @Override public void onCancelled(final DatabaseError e) {
	 * System.out.println("The read failed: " + e.getCode()); } }); try {
	 * done.await(); } catch (final InterruptedException e) { // TODO Auto-generated
	 * catch block } if (DailyInfoList.isEmpty()) try { if (dbRef != null)
	 * dbRef.push().setValueAsync(di).get(); } catch (InterruptedException |
	 * ExecutionException e) { // += e.getMessage() + " "; } else try {
	 * FirebaseDatabase.getInstance().getReference().child(UserMail).child("Dates").
	 * child(date) .child("Daily-Info").setValueAsync(new DailyInfo(weight,
	 * DailyInfoList.get(0).getDailyCalories(),
	 * DailyInfoList.get(0).getDailyLitresOfWater(),
	 * DailyInfoList.get(0).getDailyProteinGrams())) .get(); } catch (final
	 * InterruptedException e) { e.printStackTrace(); } catch (final
	 * ExecutionException e) { e.printStackTrace(); } }
	 * 
	 * 
	 * private void openDatabase() { try { FileInputStream serviceAccount;
	 * FirebaseOptions options = null; try { serviceAccount = new
	 * FileInputStream("db_credentials.json"); options = new
	 * FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(
	 * serviceAccount))
	 * .setDatabaseUrl("https://fitnesspeaker-6eee9.firebaseio.com/").build();
	 * FirebaseApp.initializeApp(options); } catch (final Exception e1) { // empty
	 * block
	 * 
	 * } } catch (final Exception e) { // empty block } }
	 * 
	 * @Test public void enter(){ String UserMail =
	 * "ororfeldman4@gmail.com".replace(".", "_dot_"); String[] dates=getDates();
	 * openDatabase(); for(int i=0;i<14;i++) {
	 * setWeightByDate(dates[i],UserMail,60+i); }
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @Test public void testFile() { File outputfile = new File("testingg.jpg");
	 * try { outputfile.createNewFile(); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 */

}
