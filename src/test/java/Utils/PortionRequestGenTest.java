/** a test module for class PortionRequestGen.
 * @author Shaked Sapir
 * @since 2018-12-17*/
package Utils;

import java.util.ArrayList;

import org.junit.Test;

public class PortionRequestGenTest {

	ArrayList<String> strings = new ArrayList<>();
	ArrayList<String> contained = new ArrayList<>();
	@Test
	public void test_grams() {
//		assert PortionRequestGen.generatePortion("banana", Portion.Type.FOOD).toString()
//				.contains("Portion name: banana , 100.0 grams\nPortion type: FOOD\n----------------------------------\n"
//						+ "Nutritional Values per 100 grams:\nCalories: 346.0\nProteins: 3.89\n"
//						+ "Carbohydrates: 88.28\nFats: 1.81");
		/** this should pass through all options till it gets to the converter **/
		Portion p = PortionRequestGen.generatePortionWithAmount("banana", Portion.Type.FOOD, 50, "milligrams");
		String s1 = p.toString();
//		assert s1.contains("Portion name: banana , 50.0 grams\nPortion type: FOOD\n----------------------------------\n"
//						+ "Nutritional Values per 100 grams:\nCalories: 44.5\nProteins: 0.545\n"
//						+ "Carbohydrates: 11.42\nFats: 0.165\n");
		contained.add("Portion name: banana , 50.0 grams\nPortion type: FOOD\n----------------------------------\n"
						+ "Nutritional Values per 100 grams:\nCalories: 89\nProteins: 1.09\n"
						+ "Carbohydrates: 22.84\nFats: 0.33\n");
//		System.out.println(s1);
		strings.add(s1);
		assert s1.contains("Nutritional Values per 100 grams:\nCalories: 89.0\nProteins: 1.09\n"
				+ "Carbohydrates: 22.84\nFats: 0.33\n");
		assert p.getAmount() == 0.05;
		
	}
	
	@Test
	public void test_cup() {
		/**this should return the value for cup of sliced banana, as appears in the usda JSON*/
		String s2 =  PortionRequestGen.generatePortionWithAmount("banana", Portion.Type.FOOD, 2, "cups").toString();
//		assert s2.contains("Portion name: banana , 50.0 grams\nPortion type: FOOD\n----------------------------------\n"
//				+ "Nutritional Values per 100 grams:\nCalories: 400.0\nProteins: 4.9\n"
//				+ "Carbohydrates: 102.78\nFats: 1.48\n");
		contained.add("Nutritional Values per 100 grams:\nCalories: 400.0\nProteins: 4.9\n"
				+ "Carbohydrates: 102.78\nFats: 1.48\n");
		strings.add(s2);
//		System.out.println(s2);
		assert s2.contains("Nutritional Values per 100 grams:\nCalories: 89.0\nProteins: 1.09\n"
				+ "Carbohydrates: 22.84\nFats: 0.33\n");

	}
	
	@Test
	public void test_cup2() {
		/**this should return the value for cup of rice, as appears in the usda JSON*/
		Portion p = PortionRequestGen.generatePortionWithAmount("rice", Portion.Type.FOOD, 2, "cups");
		String s = p.toString();
//		assert s2.contains("Portion name: banana , 50.0 grams\nPortion type: FOOD\n----------------------------------\n"
//				+ "Nutritional Values per 100 grams:\nCalories: 400.0\nProteins: 4.9\n"
//				+ "Carbohydrates: 102.78\nFats: 1.48\n");
//		contained.add("Nutritional Values per 100 grams:\nCalories: 400.0\nProteins: 4.9\n"
//				+ "Carbohydrates: 102.78\nFats: 1.48\n");
		strings.add(s);
//		System.out.println(s);
		assert s.contains("Nutritional Values per 100 grams:\nCalories: 357.0\nProteins: 14.73\n"
				+ "Carbohydrates: 74.9\nFats: 1.08\n");
		assert p.getAmount() == 320.0;

	}
	@Test 
	public void check_raw_egg_suffix() {
		String s =  PortionRequestGen.generatePortionWithAmount("egg", Portion.Type.FOOD, 1, "large").toString();
//		assert s2.contains("Portion name: banana , 50.0 grams\nPortion type: FOOD\n----------------------------------\n"
//				+ "Nutritional Values per 100 grams:\nCalories: 400.0\nProteins: 4.9\n"
//				+ "Carbohydrates: 102.78\nFats: 1.48\n");
//		contained.add("Nutritional Values per 100 grams:\nCalories: 400.0\nProteins: 4.9\n"
//				+ "Carbohydrates: 102.78\nFats: 1.48\n");
		strings.add(s);
//		System.out.println(s);
		assert s.contains("Nutritional Values per 100 grams:\nCalories: 143.0\nProteins: 12.56\n"
				+ "Carbohydrates: 0.72\nFats: 9.51\n");

	}
	
//	@Test
//	public void test_apple_size() {
//		/** this should return the measures of a "medium" apple**/
//		String s3 = PortionRequestGen.generatePortionWithAmount("apple", Portion.Type.FOOD, Double.valueOf(2), "None").toString();
////		System.out.println(s3);
//		contained.add("Nutritional Values per 100 grams:\nCalories: 190.0\nProteins: 0.94\n"
//				+ "Carbohydrates: 50.26\nFats: 0.62\n");
//		strings.add(s3);
//		assert s3.contains("Nutritional Values per 100 grams:\nCalories: 190.0\nProteins: 0.94\n"
//				+ "Carbohydrates: 50.26\nFats: 0.62\n");
//	}
	/** this test checks all strings, after all HTTP reads have stablized**/
	@Test
	public void check_strings() {
		for(int i=0;i<strings.size();i++)
			assert strings.get(i).contains(contained.get(i));
	}
}