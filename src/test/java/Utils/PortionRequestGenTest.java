/** a test module for class PortionRequestGen.
 * @author Shaked Sapir
 * @since 2018-12-17*/
package Utils;

import java.util.ArrayList;

import org.junit.Test;

@SuppressWarnings("static-method")
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
		String s1 = PortionRequestGen.generatePortionWithAmount("banana", Portion.Type.FOOD, Double.valueOf(50), "grams").toString();
//		assert s1.contains("Portion name: banana , 50.0 grams\nPortion type: FOOD\n----------------------------------\n"
//						+ "Nutritional Values per 100 grams:\nCalories: 44.5\nProteins: 0.545\n"
//						+ "Carbohydrates: 11.42\nFats: 0.165\n");
		contained.add("Portion name: banana , 50.0 grams\nPortion type: FOOD\n----------------------------------\n"
						+ "Nutritional Values per 100 grams:\nCalories: 44.5\nProteins: 0.545\n"
						+ "Carbohydrates: 11.42\nFats: 0.165\n");
//		System.out.println(s1);
		strings.add(s1);
		assert s1.contains("Nutritional Values per 100 grams:\nCalories: 44.5\nProteins: 0.545\n"
				+ "Carbohydrates: 11.42\nFats: 0.165\n");
		
	}
	
	@Test
	public void test_cup() {
		/**this should return the value for cup of sliced banana, as appears in the usda JSON*/
		String s2 =  PortionRequestGen.generatePortionWithAmount("banana", Portion.Type.FOOD, Double.valueOf(2), "cups").toString();
//		assert s2.contains("Portion name: banana , 50.0 grams\nPortion type: FOOD\n----------------------------------\n"
//				+ "Nutritional Values per 100 grams:\nCalories: 400.0\nProteins: 4.9\n"
//				+ "Carbohydrates: 102.78\nFats: 1.48\n");
		contained.add("Nutritional Values per 100 grams:\nCalories: 400.0\nProteins: 4.9\n"
				+ "Carbohydrates: 102.78\nFats: 1.48\n");
		strings.add(s2);
//		System.out.println(s2);
		assert s2.contains("Nutritional Values per 100 grams:\nCalories: 400.0\nProteins: 4.9\n"
				+ "Carbohydrates: 102.78\nFats: 1.48\n");

	}
	
	@Test
	public void test_cup2() {
		/**this should return the value for cup of sliced banana, as appears in the usda JSON*/
		String s =  PortionRequestGen.generatePortionWithAmount("rice", Portion.Type.FOOD, Double.valueOf(2), "cups").toString();
//		assert s2.contains("Portion name: banana , 50.0 grams\nPortion type: FOOD\n----------------------------------\n"
//				+ "Nutritional Values per 100 grams:\nCalories: 400.0\nProteins: 4.9\n"
//				+ "Carbohydrates: 102.78\nFats: 1.48\n");
//		contained.add("Nutritional Values per 100 grams:\nCalories: 400.0\nProteins: 4.9\n"
//				+ "Carbohydrates: 102.78\nFats: 1.48\n");
		strings.add(s);
//		System.out.println(s2);
		assert s.contains("Nutritional Values per 100 grams:\nCalories: 1142.0\nProteins: 47.14\n"
				+ "Carbohydrates: 239.68\nFats: 3.46\n");

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