package Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.amazon.ask.model.services.Pair;

import Utils.Portion.Type;

@SuppressWarnings("static-method")
public class DBUtilsTest {

	@Test
	public void testDrinkHandling() {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		db.DBUtilsRemoveUserDirectory();
		assertNull(db.DBGetWaterCups().orElse(null));
		db.DBAddWaterCups(Integer.valueOf(5));
		assertEquals(Integer.valueOf(5), db.DBGetWaterCups().orElse(null));
		db.DBAddWaterCups(Integer.valueOf(7));
		assertEquals(Integer.valueOf(12), db.DBGetWaterCups().orElse(null));
		db.DBAddWaterCups(Integer.valueOf(8));
		assertEquals(Integer.valueOf(20), db.DBGetWaterCups().orElse(null));
		db.DBUtilsRemoveUserDirectory();
	}

	@Test
	public void testFoodHandling() {
		final String testUser = "test_user";
		final DBUtils db = new DBUtils(testUser);
		db.DBUtilsRemoveUserDirectory();
		assertNull(db.DBGetFood("banana"));
		assert db.DBGetFoodList().isEmpty();
		db.DBPushFood(PortionRequestGen.generatePortionWithAmount("banana", Type.FOOD, Integer.valueOf(52)));
		Portion p = db.DBGetFood("banana");
		assertNotNull(p);
		assertEquals("banana", p.getName());
		assertEquals(Integer.valueOf(52), Integer.valueOf((int) p.getAmount()));
		db.DBPushFood(PortionRequestGen.generatePortionWithAmount("banana", Type.FOOD, Integer.valueOf(48)));
		p = db.DBGetFood("banana");
		assertNotNull(p);
		assertEquals("banana", p.getName());
		assertEquals(Integer.valueOf(100), Integer.valueOf((int) p.getAmount()));
		p = db.DBGetFood("avocado");
		assertNull(p);
		db.DBPushFood(PortionRequestGen.generatePortionWithAmount("avocado", Type.FOOD, Integer.valueOf(48)));
		p = db.DBGetFood("avocado");
		assertNotNull(p);
		assertEquals("avocado", p.getName());
		assertEquals(Integer.valueOf(48), Integer.valueOf((int) p.getAmount()));

		final List<Pair<String, Portion>> portionList = db.DBGetFoodList();
		assertEquals(2, portionList.size());

		assert portionList.get(0).getName() != null && portionList.get(0).getName().length() > 0
				&& portionList.get(1).getName() != null && portionList.get(1).getName().length() > 0;
		final Portion p1 = portionList.get(0).getValue();
		final Portion p2 = portionList.get(1).getValue();

		final Portion p_banana = "banana".equals(p1.getName()) ? p1 : p2;
		final Portion p_avocado = "avocado".equals(p1.getName()) ? p1 : p2;
		assertEquals(Integer.valueOf(48), Integer.valueOf((int) p_avocado.getAmount()));
		assertEquals(Integer.valueOf(100), Integer.valueOf((int) p_banana.getAmount()));

		db.DBUtilsRemoveUserDirectory();
	}

}
