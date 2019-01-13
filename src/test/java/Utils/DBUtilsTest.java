package Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.stream.Collectors;

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
		assertNull(db.DBGetFoodByKey("123"));
		assert db.DBGetFoodList().isEmpty();
		db.DBPushFood(PortionRequestGen.generatePortionWithAmount("banana", Type.FOOD, Integer.valueOf(52)));
		Portion p = db.DBGetFoodByKey(db.DBGetFoodList().get(0).getName());
		assertNotNull(p);
		assertEquals("banana", p.getName());
		assertEquals(Integer.valueOf(52), Integer.valueOf((int) p.getAmount()));
		
		db.DBPushFood(PortionRequestGen.generatePortionWithAmount("banana", Type.FOOD, Integer.valueOf(48)));
		List<Pair<String, Portion>> portionList=db.DBGetFoodList();
		p = db.DBGetFoodByKey(portionList.get(0).getName());
		assertNotNull(p);
		assertEquals("banana", p.getName());
		p = db.DBGetFoodByKey(portionList.get(1).getName());
		assertNotNull(p);
		assertEquals("banana", p.getName());
		p = db.DBGetFoodByKey("avocado");
		assertNull(p);
		db.DBPushFood(PortionRequestGen.generatePortionWithAmount("avocado", Type.FOOD, Integer.valueOf(48)));
		portionList=db.DBGetFoodList();
		assertEquals(1, portionList.stream().filter(s->s.getValue().getName().contains("avocado")).count());
		assertEquals(Integer.valueOf(48), Integer.valueOf((int) portionList.stream().filter(s->s.getValue().getName().contains("avocado")).
				collect(Collectors.toList()).get(0).getValue().getAmount()));
		assertEquals(3, portionList.size());
		db.DBUtilsRemoveUserDirectory();
	}

}
