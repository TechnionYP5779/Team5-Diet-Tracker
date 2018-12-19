/** a test module for class PortionRequestGen.
 * @author Shaked Sapir
 * @since 2018-12-17*/
package Utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class PortionRequestGenTest {
	

	@Test
	public void test() {
	assertEquals(PortionRequestGen.generatePortion("banana", Portion.Type.FOOD).toString(),
				"Portion name: banana , 100.0 grams\nPortion type: FOOD\n----------------------------------\n"
						+ "Nutritional Values per 100 grams:\nCalories: 346.0\nProteins: 3.89\n"
						+ "Carbohydrates: 88.28\nFats: 1.81");
	}

}