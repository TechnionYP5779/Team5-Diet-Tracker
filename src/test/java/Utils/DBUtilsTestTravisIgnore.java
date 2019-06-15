package Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.amazon.ask.model.services.Pair;

import Utils.Portion.Portion;
import Utils.Portion.PortionRequestGen;
import Utils.DB.DBUtils;

/**
 * unit tests for DBUtils
 * 
 * @author Shalev Kuba
 * @since 2018-12-23
 */
@SuppressWarnings("static-method")
public class DBUtilsTestTravisIgnore {

	@Test
	public void testDrinkHandling() throws DBUtils.DBException {
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
	public void testCoffeeDrinkHandling() throws DBUtils.DBException {
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
	public void testCigarettesHandling() throws DBUtils.DBException {
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
	public void testFoodHandling() throws DBUtils.DBException {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		db.DBUtilsRemoveUserDirectory();
		assertNull(db.DBGetFoodByKey("123"));
		assert db.DBGetTodayFoodList().isEmpty();
		db.DBPushFood(PortionRequestGen.generatePortionWithAmount("banana", Portion.Type.FOOD, 52, "grams"));
		Portion p = db.DBGetFoodByKey(db.DBGetTodayFoodList().get(0).getName());
		assertNotNull(p);
		assertEquals("banana", p.getName());
		assertEquals(Integer.valueOf(52), Integer.valueOf((int) p.getAmount()));

		db.DBPushFood(PortionRequestGen.generatePortionWithAmount("banana", Portion.Type.FOOD, 48, "grams"));
		List<Pair<String, Portion>> portionList = db.DBGetTodayFoodList();
		p = db.DBGetFoodByKey(portionList.get(0).getName());
		assertNotNull(p);
		assertEquals("banana", p.getName());
		p = db.DBGetFoodByKey(portionList.get(1).getName());
		assertNotNull(p);
		assertEquals("banana", p.getName());
		p = db.DBGetFoodByKey("avocado");
		assertNull(p);
		db.DBPushFood(PortionRequestGen.generatePortionWithAmount("avocado", Portion.Type.FOOD, 48, "grams"));
		portionList = db.DBGetTodayFoodList();
		assertEquals(1, portionList.stream().filter(s -> s.getValue().getName().contains("avocado")).count());
		assertEquals(Integer.valueOf(48),
				Integer.valueOf((int) portionList.stream().filter(s -> s.getValue().getName().contains("avocado"))
						.collect(Collectors.toList()).get(0).getValue().getAmount()));
		assertEquals(3, portionList.size());
		db.DBUtilsRemoveUserDirectory();
	}

	@Test
	public void testDailyInfo() throws DBUtils.DBException {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		db.DBUtilsRemoveUserDirectory();

		assertNull(db.DBGetTodayDailyInfo());
		db.DBUpdateTodayDailyInfo(new DailyInfo(52.5, 1000.0, 2, 100.0));
		assertNotNull(db.DBGetTodayDailyInfo());
		assertEquals(Double.valueOf(52.5), Double.valueOf(db.DBGetTodayDailyInfo().getWeight()));
		assertEquals(Double.valueOf(1000), Double.valueOf(db.DBGetTodayDailyInfo().getDailyCalories()));
		assertEquals(Double.valueOf(2), Double.valueOf(db.DBGetTodayDailyInfo().getDailyLitresOfWater()));
		assertEquals(Double.valueOf(100), Double.valueOf(db.DBGetTodayDailyInfo().getDailyProteinGrams()));

		assertNull(db.DBGetDateDailyInfo("27-Dec-2018"));

		db.DBUpdateTodayDailyInfo(new DailyInfo(53.0, 1000.0, 2, 100.0));

		assertNull(db.DBGetDateDailyInfo("27-Dec-2018"));
		assertNotNull(db.DBGetTodayDailyInfo());
		assertEquals(Double.valueOf(53), Double.valueOf(db.DBGetTodayDailyInfo().getWeight()));
		assertEquals(Double.valueOf(1000), Double.valueOf(db.DBGetTodayDailyInfo().getDailyCalories()));
		assertEquals(Double.valueOf(2), Double.valueOf(db.DBGetTodayDailyInfo().getDailyLitresOfWater()));
		assertEquals(Double.valueOf(100), Double.valueOf(db.DBGetTodayDailyInfo().getDailyProteinGrams()));
		db.DBUtilsRemoveUserDirectory();
	}



	@Test
	public void testAlcoholHandling() throws DBUtils.DBException {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		db.DBUtilsRemoveUserDirectory();
		assert db.DBGetTodayAlcoholList().isEmpty();
		db.DBPushAlcohol(new Portion(Portion.Type.DRINK, "vodka", 40.0, 50, 0, 0, 0, 40));
		List<Pair<String, Portion>> portionList = db.DBGetTodayAlcoholList();
		assertEquals(Integer.valueOf(1), Integer.valueOf(portionList.size()));
		assertEquals("vodka", portionList.get(0).getValue().getName());
		assertEquals(Double.valueOf(40), Double.valueOf(portionList.get(0).getValue().getAlchohol_by_volume()));
		assertEquals(Double.valueOf(0), Double.valueOf(portionList.get(0).getValue().getCarbs_per_100_grams()));
		db.DBPushAlcohol(new Portion(Portion.Type.DRINK, "wine", 40.0, 50, 0, 0, 0, 40));
		portionList = db.DBGetTodayAlcoholList();
		assertEquals(Integer.valueOf(2), Integer.valueOf(portionList.size()));
		assertEquals(Long.valueOf(1),
				Long.valueOf(portionList.stream().filter(f -> f.getValue().getName().contains("vodka")).count()));
		assertEquals(Long.valueOf(1),
				Long.valueOf(portionList.stream().filter(f -> f.getValue().getName().contains("wine")).count()));
		db.DBUtilsRemoveUserDirectory();
	}


}
