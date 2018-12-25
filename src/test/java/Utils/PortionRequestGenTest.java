/** a test module for class PortionRequestGen.
 * @author Shaked Sapir
 * @since 2018-12-17*/
package Utils;

import org.junit.Test;

public class PortionRequestGenTest {

	@Test
	public void test() {
		assert PortionRequestGen.generatePortion("banana", Portion.Type.FOOD).toString()
				.contains("Portion name: banana , 100.0 grams\nPortion type: FOOD\n----------------------------------\n"
						+ "Nutritional Values per 100 grams:\nCalories: 346.0\nProteins: 3.89\n"
						+ "Carbohydrates: 88.28\nFats: 1.81");

		assert PortionRequestGen.generatePortionWithAmount("banana", Portion.Type.FOOD, Integer.valueOf(50)).toString()
				.contains("Portion name: banana , 50.0 grams\nPortion type: FOOD\n----------------------------------\n"
						+ "Nutritional Values per 100 grams:\nCalories: 346.0\nProteins: 3.89\n"
						+ "Carbohydrates: 88.28\nFats: 1.81");

	}

}