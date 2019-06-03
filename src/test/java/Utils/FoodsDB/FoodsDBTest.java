package Utils.FoodsDB;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.Test;

public class FoodsDBTest {
	
	private final String testingEmail="nwerncgwuinrgw2764nr283h_test@gmail.com";
	
	/*@Test
	public void testAddDeleteUser() throws FoodsDBException {
		FoodsDB db =new FoodsDB();
		db.addUser(testingEmail);
		db.deleteUser(testingEmail);
	}
	
	@Test
	public void testAteToday() throws FoodsDBException {
		FoodsDB db =new FoodsDB();
		db.addUser(testingEmail);
		db.UserAte(testingEmail, "cheese", 100);
		JSONObject vals= db.NutVal4T4Today(testingEmail);
		if((Long)vals.get("Protein") != 4 || (Long)vals.get("Fat") != 10 )
			fail("wrong nutritional values");
		db.deleteUser(testingEmail);
	}*/

}
