package Utils;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

@SuppressWarnings("static-method")
public class DBUtilsTest {

	@Test
	public void testDrinkHandling() {
		String testUser="test_user";
		DBUtils db=new DBUtils(testUser);
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

}
