/**This module generates Portion Objects:
 * it gets a food's name and type, searches it in the USDA DB and
 * generates a Portion object with the related data.
 *
 * Usage example:
 * <\>@code PortionRequestGen.generatePortion("banana",Portion.Type.Food) <\>
 * this call will return a Portion object, containing the relevant data about banana:
 * """
 * Portion name: ba
 * nana , 100.0 grams
 * Portion type: FOOD
 * ----------------------------------
 * Nutritional Values per 100 grams:
 * Calories: 346.0
 * Proteins: 3.89
 * Carbohydrates: 88.28
 * Fats: 1.81
 * """
 * @author Shaked Sapir
 * @since 2018-12-17*/

/**
 * TODO: when it all works, refactor all kinds of search (size, units, manually) to
 *  	 seperated functions, for more fluent readability of code
 */
package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PortionRequestGen {

	/**
	 * list of nutritional values we want to inject and collect about the wanted
	 * food
	 **/
	private static final String[] Nutritional_values = { "Energy", "Protein", "Carbohydrate, by difference",
			"Total lipid (fat)" };
	

	/** this one should not exist: the default and only method has to be with amount.**/
	public static Portion generatePortionWithAmount(final String portion_name, final Portion.Type t, final double amount, final String units) {
		try {
			return PortionRequestGen.generatePortionHandler(portion_name, t, amount, units);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static Portion generatePortionHandler(final String food_name, final Portion.Type t, final double amount, final String units) throws Exception {

		/**
		 * first, try to search for "raw" portion: grains, fruits, vegetables...
		 * if this returns a valid JSON object, then search for the wanted amount and get its values
		 */
		String raw_food_name = food_name + " raw";
		String raw_food_suffix = "raw "+food_name;
		JSONObject portion_search = readJsonFromUrl("https://api.nal.usda.gov/ndb/search/?format=json&q=" + raw_food_name.replace(" ", "%20")
		+ "&max=5&offset=0&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
		/** if no errors occurred, then we have the raw portion */
		if(!portion_search.has("errors")) {
//			System.out.println("no errors, it's raw");
			return queryItem(portion_search.getJSONObject("list").getJSONArray("item").getJSONObject(0).getString("ndbno"), food_name, t, amount, units);
			
		}
			
		else {
//			System.out.println("it's not raw.. check regular");
			portion_search = readJsonFromUrl("https://api.nal.usda.gov/ndb/search/?format=json&q=" + raw_food_suffix.replace(" ","%20")
			+ "&max=5&offset=0&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
//			System.out.println("https://api.nal.usda.gov/ndb/search/?format=json&q=" + raw_food_suffix.replace(' ', '_')
//			+ "&max=5&offset=0&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
			/** if no errors occurred, then we have the raw portion in second search */
			if(!portion_search.has("errors")) {
//				System.out.println("no errors, it's raw");
				return queryItem(portion_search.getJSONObject("list").getJSONArray("item").getJSONObject(0).getString("ndbno"), food_name, t, amount, units);
				
			}else {
//				System.out.println("NOT raw");

				/** otherwise, there isn't a "raw" version of the wanted portion. look for it regularly**/	
			return queryItem(readJsonFromUrl("https://api.nal.usda.gov/ndb/search/?format=json&q=" + food_name.replace(' ', '_')
						+ "&max=5&offset=0&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul")
				.getJSONObject("list").getJSONArray("item").getJSONObject(0).getString("ndbno"), food_name, t, amount, units);
			}
		}
	}

	@SuppressWarnings("boxing")
	public static Portion queryItem(final String id, final String food_name, final Portion.Type t, final double amount, final String units) throws Exception {
		// Read JSON response and print
		final JSONObject myResponse = readJsonFromUrl("https://api.nal.usda.gov/ndb/reports/?ndbno=" + id
				+ "&type=b&format=json&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
		final ArrayList<Double> nutritions = new ArrayList<>();
		/** get (from json) the array that stores the nutritional values we want **/
		final JSONArray nut_arr = myResponse.getJSONObject("report").getJSONObject("food").getJSONArray("nutrients");
		double value_per_100_g = 0.0;
		double unit_to_g =0.0;
//		System.out.println("queryItem :: got JSON nutrients obj");
		/**
		 * for each value: look for it in the array and insert its numeric value to the
		 * list
		 **/
		for (final String nut : Nutritional_values) {
			for (int i = 0; i < nut_arr.length(); i++) {
				if (nut_arr.getJSONObject(i).getString("name").equals(nut)) {
					/** we got the nutritional value we want. we now have to do the following:
					 * 1. check if the units given by user are located in json. if so - capture it and 
					 *    return the needed amount.
					 * 2. if not - convert the units given by the user(e.g., a cup) to the suitable unit in (1)
					 *    using the Weight/Volume Converter, IF ITS RAW then check the conversion by label
					 *    e.g.: apple size - large? small? medium? label contains that.
					 *    2.1 if the conversion exists - good!
					 *    2.2 otherwise - ask for manual update (TODO later)
					 * 3. calculate the exact amount of portion according to the units and amount
					 *    given by the user
					 * 4. create and return a portion record with the proper values
					 */
					
					
					
					boolean label_found = false;
					final JSONArray measures_arr = nut_arr.getJSONObject(i).getJSONArray("measures");
					
					/** if not, search the units as actual labels in the JSON, so it can be more precise**/
					if(!label_found) {
//						label_found = checkForLabel(nutritions, measures_arr,nut_arr, i , amount, units);
						for( int j=0;j<measures_arr.length() && !label_found ;j++) {
							
							/** turn the units to a single form (the way it appears in label) , not plural**/
							String post_processed_units = units.substring(units.length()-1).equals("s")? units.substring(0,units.length()-1) : units;
	//						System.out.println("units: "+post_processed_units );
							/** check for a label that contains the units**/
	//						System.out.println("label: "+measures_arr.getJSONObject(j).getString("label"));
							if(measures_arr.getJSONObject(j).getString("label").contains(post_processed_units)) {
								value_per_100_g = Double.parseDouble(nut_arr.getJSONObject(i).getString("value"));
//								double unit_to_g = (measures_arr.getJSONObject(j).getDouble("eqv")/100)*value_per_100_g*amount;
								unit_to_g = (measures_arr.getJSONObject(j).getDouble("eqv"))*amount;

//								/**limit result to 3 decimal digits**/
//								unit_to_g = ((double)((int)(unit_to_g*1000.0)))/1000.0;
//								nutritions.add(unit_to_g);
								nutritions.add(Double.valueOf(value_per_100_g));

								label_found = true;
							}
						}
					}
					
					
					/** in case the units didn't appear in the JSON object, we convert manually according 
					 *  to the converter module
					 */
					if(!label_found) {
//						label_found = checkForLabel(nutritions, measures_arr,nut_arr, i , amount, units);

//						System.out.println("no label");
						/** if reached here, the unit is not a label in the JSON,  convert manually**/
						value_per_100_g = Double.valueOf(Double.parseDouble(nut_arr.getJSONObject(i).getString("value")));
						/** turn the units to a plural form, not single**/
						String post_processed_units = units.substring(units.length()-1).equals("s")? units : units+"s";
//						System.out.println("queryItem :: checked nutrient val is " + nut);
						try {
							/** now we can find the right converter-method to invoke, using reflection*/
							String converter_func_name = post_processed_units+"ToGrams";	
							Method converter_method = Class.forName(WeightConverter.class.getName()).getDeclaredMethod(converter_func_name, double.class);
							
//							System.out.println("queryItem :: method is " + converter_method.getName());
							
							/** calculate the amount in grams of the original unit**/
							unit_to_g = (double) converter_method.invoke(WeightConverter.class,amount);
//							nutritions.add(unit_to_g);
							/** probably we dont have to calc the exact amount as it is calculated in DailyInfo.java**/
//							double real_nut_value = (unit_to_g/100)*value_per_100_g;						
//							nutritions.add(real_nut_value);
							nutritions.add(Double.valueOf(value_per_100_g));
							label_found = true;
						} catch(NoSuchMethodException  e) {
							/** TODO if reached here, then we have a lack in convertion methods, we should add
							 *  one and notify the user about this
							 */
							label_found = false;
						}
//						
					}
					/** if we didn't find the label, check if there is a size measure label in the
					 *  JSON object, for portions like raw fruits, vegetables...*/
					if(!label_found) {
						for( int j=0;j<measures_arr.length() && !label_found ;j++) {
							
							/** check for a label that contains the size: we use "medium" as default**/
	//						System.out.println("label: "+measures_arr.getJSONObject(j).getString("label"));
							if(measures_arr.getJSONObject(j).getString("label").contains("medium")) {
								value_per_100_g = Double.valueOf(Double.parseDouble(nut_arr.getJSONObject(i).getString("value")));
								unit_to_g = (measures_arr.getJSONObject(j).getDouble("eqv"))*amount;
								nutritions.add(Double.valueOf(value_per_100_g));
								label_found = true;
							}
						}
					}
				}
			}
		}
		/**
		 * right now, assignment is done in-order, according to the order of the values
		 * in "Nutritional_values" . later on, assignment will be using reflection. just
		 * wait for it :)
		 **/
		return new Portion(t, food_name, unit_to_g, nutritions.get(0).doubleValue(), nutritions.get(1).doubleValue(),
				nutritions.get(2).doubleValue(), nutritions.get(3).doubleValue());
	}
	
	private static String readAll(final Reader rd) throws IOException {
		final StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1)
			sb.append((char) cp);
		return sb.toString();
	}

	public static boolean checkForLabel(ArrayList<Double> nutritions, JSONArray measures_arr, JSONArray nut_arr, int nut_arr_idx,
			double amount, String units) {
		boolean label_found = false;		
		/** if not, search the units as actual labels in the JSON, so it can be more precise**/
		
		for( int j=0;j<measures_arr.length() && !label_found ;j++) {
			
			/** turn the units to a single form (the way it appears in label) , not plural**/
			String post_processed_units = units.substring(units.length()-1).equals("s")? units.substring(0,units.length()-1) : units;
//						System.out.println("units: "+post_processed_units );
			/** check for a label that contains the units**/
//						System.out.println("label: "+measures_arr.getJSONObject(j).getString("label"));
			if(measures_arr.getJSONObject(j).getString("label").contains(post_processed_units)) {
				double value_per_100_g = Double.parseDouble(nut_arr.getJSONObject(nut_arr_idx).getString("value"));
				double unit_to_g = (measures_arr.getJSONObject(j).getDouble("eqv")/100)*value_per_100_g*amount;
				/**limit result to 3 decimal digits**/
				unit_to_g = ((double)((int)(unit_to_g*1000.0)))/1000.0;
				nutritions.add(Double.valueOf(unit_to_g));
				label_found = true;
			}
		}
		return label_found;
	}
	
//	public static boolean checkForPredefinedConversion(ArrayList<Double> nutritions, JSONArray measures_arr, JSONArray nut_arr, int nut_arr_idx,
//			double amount, String units) {
//		/** if reached here, the unit is not a label in the JSON,  convert manually**/
//		boolean label_found = false;		
//		double value_per_100_g = Double.valueOf(Double.parseDouble(nut_arr.getJSONObject(nut_arr_idx).getString("value")));
//		/** turn the units to a plural form, not single**/
//		String post_processed_units = units.substring(units.length()-1).equals("s")? units : units+"s";
////		System.out.println("queryItem :: checked nutrient val is " + nut);
//		try {
//			/** now we can find the right converter-method to invoke, using reflection*/
//			String converter_func_name = post_processed_units+"ToGrams";	
//			Method converter_method = Class.forName(WeightConverter.class.getName()).getDeclaredMethod(converter_func_name, double.class);
//			
////			System.out.println("queryItem :: method is " + converter_method.getName());
//			
//			/** calculate the amount in grams of the original unit**/
//			double unit_to_g = (double) converter_method.invoke(WeightConverter.class,amount);
////			nutritions.add(unit_to_g);
//			/** probably we dont have to calc the exact amount as it is calculated in DailyInfo.java**/
//			double real_nut_value = (unit_to_g/100)*value_per_100_g;						
//			nutritions.add(real_nut_value);
//			label_found = true;
//		} catch(Exception  e) {
//			/** TODO if reached here, then we have a lack in convertion methods, we should add
//			 *  one and notify the user about this
//			 */
//			label_found = false;
//		}
//		return label_found;
//	}
	
	/*
	 * creates and returns a JSON object from a url
	 */
	public static JSONObject readJsonFromUrl(final String url) throws IOException, JSONException {
		final InputStream is = new URL(url).openStream();
		try {
			@SuppressWarnings("resource")
			final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			final String jsonText = readAll(rd);
			final JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
}