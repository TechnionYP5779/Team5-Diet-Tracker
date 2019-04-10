/** a test module for class PortionRequestGen.
 * @author Shaked Sapir
 * @since 2018-12-17*/
package Utils;

import org.junit.Test;

import Utils.Portion.Portion;
import Utils.Portion.PortionRequestGen;

public class PortionRequestGenTest {

	@Test
	/** a simple test to check a simple conversion between units **/
	public void test_grams() {
		Portion p = PortionRequestGen.generatePortionWithAmount("banana", Portion.Type.FOOD, 50, "milligrams");
		String s1 = p.toString();

		assert s1.contains("Nutritional Values per 100 grams:\nCalories: 89.0\nProteins: 1.09\n"
				+ "Carbohydrates: 22.84\nFats: 0.33\n");
		assert p.getAmount() == 0.05;

	}

	@Test
	/** a test to check we can get portion in units other than grams **/
	public void test_cup() {
		assert PortionRequestGen.generatePortionWithAmount("banana", Portion.Type.FOOD, 2, "cups").toString()
				.contains("Nutritional Values per 100 grams:\nCalories: 89.0\nProteins: 1.09\n"
						+ "Carbohydrates: 22.84\nFats: 0.33\n");

	}

	@Test
	public void test_cup2() {
		/** this should return the value for cup of rice, as appears in the usda JSON */
		Portion p = PortionRequestGen.generatePortionWithAmount("rice", Portion.Type.FOOD, 2, "cups");
		String s = p.toString();
		assert s.contains("Nutritional Values per 100 grams:\nCalories: 357.0\nProteins: 14.73\n"
				+ "Carbohydrates: 74.9\nFats: 1.08\n");
		assert p.getAmount() == 320.0;

	}

	@Test
	/**
	 * a test to check that we can search for food in "raw" version. such as eggs,
	 * fruits..
	 **/
	public void check_raw_egg_suffix() {
		assert PortionRequestGen.generatePortionWithAmount("egg", Portion.Type.FOOD, 1, "large").toString()
				.contains("Nutritional Values per 100 grams:\nCalories: 143.0\nProteins: 12.56\n"
						+ "Carbohydrates: 0.72\nFats: 9.51\n");

	}

}