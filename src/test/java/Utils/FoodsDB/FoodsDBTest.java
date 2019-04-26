package Utils.FoodsDB;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.Test;

public class FoodsDBTest {
	
	private final String testingEmail="nwerncgwuinrgw2764nr283h_test@gmail.com";
	
	@Test
	public void testAddDeleteUser() throws FoodsDBException {
		FoodsDB db =new FoodsDB();
		db.addUser(testingEmail);
		db.deleteUser(testingEmail);
	}

}
