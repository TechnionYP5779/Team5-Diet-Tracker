package Utils.FoodsDB;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.Test;

public class FoodsDBTest {
	
	
	
	@Test
	public void testAteToday() throws FoodsDBException {
		FoodsDB db =new FoodsDB();
		JSONObject nutvals = db.NutVal4T4Today("or@gmail.com");
		System.out.println(nutvals);
	}

}
