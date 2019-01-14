package Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.amazon.ask.model.services.Pair;

import Utils.DBUtils.DBException;
import Utils.Portion.Type;
import Utils.UserInfo.Gender;

@SuppressWarnings("static-method")
public class DBUtilsTest {

	@Test
	public void testDrinkHandling() throws DBException {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		db.DBUtilsRemoveUserDirectory();
		assertNull(db.DBGetTodayWaterCups().orElse(null));
		db.DBAddWaterCups(Integer.valueOf(5));
		assertEquals(Integer.valueOf(5), db.DBGetTodayWaterCups().orElse(null));
		db.DBAddWaterCups(Integer.valueOf(7));
		assertEquals(Integer.valueOf(12), db.DBGetTodayWaterCups().orElse(null));
		db.DBAddWaterCups(Integer.valueOf(8));
		assertEquals(Integer.valueOf(20), db.DBGetTodayWaterCups().orElse(null));
		db.DBUtilsRemoveUserDirectory();
	}
	
	@Test
	public void testCoffeeDrinkHandling() throws DBException {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		db.DBUtilsRemoveUserDirectory();
		assertNull(db.DBGetTodayCofeeCups().orElse(null));
		db.DBAddCoffeeCups(Integer.valueOf(5));
		assertEquals(Integer.valueOf(5), db.DBGetTodayCofeeCups().orElse(null));
		db.DBAddCoffeeCups(Integer.valueOf(7));
		assertEquals(Integer.valueOf(12), db.DBGetTodayCofeeCups().orElse(null));
		db.DBAddCoffeeCups(Integer.valueOf(8));
		assertEquals(Integer.valueOf(20), db.DBGetTodayCofeeCups().orElse(null));
		db.DBUtilsRemoveUserDirectory();
	}
	
	@Test
	public void testCigarettesHandling() throws DBException {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		db.DBUtilsRemoveUserDirectory();
		assertNull(db.DBGetTodayCigarettesCount().orElse(null));
		db.DBAddCigarettes(Integer.valueOf(5));
		assertEquals(Integer.valueOf(5), db.DBGetTodayCigarettesCount().orElse(null));
		db.DBAddCigarettes(Integer.valueOf(7));
		assertEquals(Integer.valueOf(12), db.DBGetTodayCigarettesCount().orElse(null));
		db.DBAddCigarettes(Integer.valueOf(8));
		assertEquals(Integer.valueOf(20), db.DBGetTodayCigarettesCount().orElse(null));
		db.DBUtilsRemoveUserDirectory();
	}

	@Test
	public void testFoodHandling() throws DBException {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		db.DBUtilsRemoveUserDirectory();
		assertNull(db.DBGetFoodByKey("123"));
		assert db.DBGetTodayFoodList().isEmpty();
		db.DBPushFood(PortionRequestGen.generatePortionWithAmount("banana", Type.FOOD, Integer.valueOf(52)));
		Portion p = db.DBGetFoodByKey(db.DBGetTodayFoodList().get(0).getName());
		assertNotNull(p);
		assertEquals("banana", p.getName());
		assertEquals(Integer.valueOf(52), Integer.valueOf((int) p.getAmount()));
		
		db.DBPushFood(PortionRequestGen.generatePortionWithAmount("banana", Type.FOOD, Integer.valueOf(48)));
		List<Pair<String, Portion>> portionList=db.DBGetTodayFoodList();
		p = db.DBGetFoodByKey(portionList.get(0).getName());
		assertNotNull(p);
		assertEquals("banana", p.getName());
		p = db.DBGetFoodByKey(portionList.get(1).getName());
		assertNotNull(p);
		assertEquals("banana", p.getName());
		p = db.DBGetFoodByKey("avocado");
		assertNull(p);
		db.DBPushFood(PortionRequestGen.generatePortionWithAmount("avocado", Type.FOOD, Integer.valueOf(48)));
		portionList=db.DBGetTodayFoodList();
		assertEquals(1, portionList.stream().filter(s->s.getValue().getName().contains("avocado")).count());
		assertEquals(Integer.valueOf(48), Integer.valueOf((int) portionList.stream().filter(s->s.getValue().getName().contains("avocado")).
				collect(Collectors.toList()).get(0).getValue().getAmount()));
		assertEquals(3, portionList.size());
		db.DBUtilsRemoveUserDirectory();
	}
	
	@Test
	public void testDailyInfo() throws DBException {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		db.DBUtilsRemoveUserDirectory();
		
		assertNull(db.DBGetTodayDailyInfo());
		db.DBUpdateTodayDailyInfo(new DailyInfo(52.5,1000.0,2,100.0));
		assertNotNull(db.DBGetTodayDailyInfo());
		assertEquals(Double.valueOf(52.5), Double.valueOf(db.DBGetTodayDailyInfo().getWeight()));
		assertEquals(Double.valueOf(1000), Double.valueOf(db.DBGetTodayDailyInfo().getDailyCalories()));
		assertEquals(Double.valueOf(2), Double.valueOf(db.DBGetTodayDailyInfo().getDailyLitresOfWater()));
		assertEquals(Double.valueOf(100), Double.valueOf(db.DBGetTodayDailyInfo().getDailyProteinGrams()));
		
		assertNull(db.DBGetDateDailyInfo("27-Dec-2018"));

		db.DBUpdateTodayDailyInfo(new DailyInfo(53.0,1000.0,2,100.0));
		
		assertNull(db.DBGetDateDailyInfo("27-Dec-2018"));
		assertNotNull(db.DBGetTodayDailyInfo());
		assertEquals(Double.valueOf(53), Double.valueOf(db.DBGetTodayDailyInfo().getWeight()));
		assertEquals(Double.valueOf(1000), Double.valueOf(db.DBGetTodayDailyInfo().getDailyCalories()));
		assertEquals(Double.valueOf(2), Double.valueOf(db.DBGetTodayDailyInfo().getDailyLitresOfWater()));
		assertEquals(Double.valueOf(100), Double.valueOf(db.DBGetTodayDailyInfo().getDailyProteinGrams()));
		db.DBUtilsRemoveUserDirectory();
	}
	
	@Test
	public void testUserInfo() throws DBException {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		db.DBUtilsRemoveUserDirectory();
		
		assertNull(db.DBGetUserInfo());
		db.DBUpdateUserInfo(new UserInfo(Gender.MALE, 22, 180, 2000, 120, 60, 5, 0));
		assertNotNull(db.DBGetUserInfo());
		assertEquals(Gender.MALE, db.DBGetUserInfo().getGender());
		assertEquals( Integer.valueOf(22), Integer.valueOf(db.DBGetUserInfo().getAge()));
		assertEquals( Integer.valueOf(180), Integer.valueOf(db.DBGetUserInfo().getHeight()));
		assertEquals( Double.valueOf(2000), Double.valueOf(db.DBGetUserInfo().getDailyCaloriesGoal()));
		Double d1;
		d1 = Double.valueOf(60);
		assertEquals(d1, Double.valueOf(db.DBGetUserInfo().getDailyCarbsGoal()));
		assertEquals( Double.valueOf(120), Double.valueOf(db.DBGetUserInfo().getDailyProteinGramsGoal()));
		assertEquals( Double.valueOf(5), Double.valueOf(db.DBGetUserInfo().getDailyFatsGoal()));
		assertEquals( Double.valueOf(0), Double.valueOf(db.DBGetUserInfo().getDailyLimitCigarettes()));
		
		db.DBUpdateUserInfo(new UserInfo(Gender.MALE, 22, 180, 1500, 120, 60, 5, 0));
		assertNotNull(db.DBGetUserInfo());
		assertEquals(Gender.MALE, db.DBGetUserInfo().getGender());
		assertEquals( Integer.valueOf(22), Integer.valueOf(db.DBGetUserInfo().getAge()));
		assertEquals( Integer.valueOf(180), Integer.valueOf(db.DBGetUserInfo().getHeight()));
		assertEquals( Double.valueOf(1500), Double.valueOf(db.DBGetUserInfo().getDailyCaloriesGoal()));
		assertEquals( Double.valueOf(60), Double.valueOf(db.DBGetUserInfo().getDailyCarbsGoal()));
		assertEquals( Double.valueOf(120), Double.valueOf(db.DBGetUserInfo().getDailyProteinGramsGoal()));
		assertEquals( Double.valueOf(5), Double.valueOf(db.DBGetUserInfo().getDailyFatsGoal()));
		assertEquals( Double.valueOf(0), Double.valueOf(db.DBGetUserInfo().getDailyLimitCigarettes()));
		
		
		db.DBUtilsRemoveUserDirectory();
	}
	
	@Test
	public void testAlcoholHandling() throws DBException {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		db.DBUtilsRemoveUserDirectory();
		assert db.DBGetTodayAlcoholList().isEmpty();
		db.DBPushAlcohol(new Portion(Type.DRINK,"vodka",40.0,50,0,0,0,40));
		List<Pair<String, Portion>> portionList=db.DBGetTodayAlcoholList();
		assertEquals(Integer.valueOf(1), Integer.valueOf(portionList.size()));
		assertEquals("vodka", portionList.get(0).getValue().getName());
		assertEquals(Double.valueOf(40), Double.valueOf(portionList.get(0).getValue().
				getAlchohol_by_volume()));
		assertEquals(Double.valueOf(0), Double.valueOf(portionList.get(0).getValue().
				getCarbs_per_100_grams()));
		db.DBPushAlcohol(new Portion(Type.DRINK,"wine",40.0,50,0,0,0,40));
		portionList=db.DBGetTodayAlcoholList();
		assertEquals(Integer.valueOf(2), Integer.valueOf(portionList.size()));
		assertEquals(Long.valueOf(1), Long.valueOf( portionList.stream().filter(f->f.getValue().getName().contains("vodka")).count()));
		assertEquals(Long.valueOf(1), Long.valueOf( portionList.stream().filter(f->f.getValue().getName().contains("wine")).count()));
		db.DBUtilsRemoveUserDirectory();
	}
	
	@Test
	public void testBloodPressureHandling() throws DBException {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		assert db.DBGetTodayBloodPressureMeasuresList().isEmpty();
		db.DBPushBloodPressureMeasure(new BloodPressure(Integer.valueOf(120), Integer.valueOf(80), new Date()));
		List<Pair<String, BloodPressure>> bloodPressureList=db.DBGetTodayBloodPressureMeasuresList();
		assertEquals(Integer.valueOf(1), Integer.valueOf(bloodPressureList.size()));
		assertEquals(Integer.valueOf(120), bloodPressureList.get(0).getValue().getSystolic());
		assertEquals(Integer.valueOf(80), bloodPressureList.get(0).getValue().getDiastolic());
		db.DBPushBloodPressureMeasure(new BloodPressure(Integer.valueOf(150), Integer.valueOf(60), new Date()));
		bloodPressureList=db.DBGetTodayBloodPressureMeasuresList();
		assertEquals(Integer.valueOf(2), Integer.valueOf(bloodPressureList.size()));
		assertEquals(Long.valueOf(1), Long.valueOf( bloodPressureList.stream().filter(f->f.getValue().getSystolic().equals(Integer.valueOf(120))).count()));
		assertEquals(Long.valueOf(1), Long.valueOf( bloodPressureList.stream().filter(f->f.getValue().getSystolic().equals(Integer.valueOf(150))).count()));
		db.DBUtilsRemoveUserDirectory();
	}	

}
