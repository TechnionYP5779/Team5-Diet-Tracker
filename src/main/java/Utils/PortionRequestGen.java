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

import Utils.FoodExceptions.UnitsNotFoundExcpetion;



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
		String raw_food_name = food_name + " raw", raw_food_suffix = "raw " + food_name;
		JSONObject portion_search = readJsonFromUrl(
				"https://api.nal.usda.gov/ndb/search/?format=json&q=" + raw_food_name.replace(" ", "%20")
						+ "&max=5&offset=0&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
		if (!portion_search.has("errors"))
			return queryItem(
					portion_search.getJSONObject("list").getJSONArray("item").getJSONObject(0).getString("ndbno"),
					food_name, t, amount, units);
		portion_search = readJsonFromUrl(
				"https://api.nal.usda.gov/ndb/search/?format=json&q=" + raw_food_suffix.replace(" ", "%20")
						+ "&max=5&offset=0&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
		return queryItem(!portion_search.has("errors")
				? portion_search.getJSONObject("list").getJSONArray("item").getJSONObject(0).getString("ndbno")
				: readJsonFromUrl("https://api.nal.usda.gov/ndb/search/?format=json&q=" + food_name.replace(' ', '_')
						+ "&max=5&offset=0&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul").getJSONObject("list")
								.getJSONArray("item").getJSONObject(0).getString("ndbno"),
				food_name, t, amount, units);
	}

	@SuppressWarnings("boxing")
	public static Portion queryItem(final String id, final String food_name, final Portion.Type t, final double amount, final String units) throws Exception {
		final JSONObject myResponse = readJsonFromUrl("https://api.nal.usda.gov/ndb/reports/?ndbno=" + id
				+ "&type=b&format=json&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
		final ArrayList<Double> nutritions = new ArrayList<>();
		final JSONArray nut_arr = myResponse.getJSONObject("report").getJSONObject("food").getJSONArray("nutrients");
		double value_per_100_g = 0.0, unit_to_g = 0.0;
		boolean label_found = false;
		for (final String nut : Nutritional_values)
			for (int i = 0; i < nut_arr.length(); ++i)
				if (nut_arr.getJSONObject(i).getString("name").equals(nut)) {
					label_found = false;
					final JSONArray measures_arr = nut_arr.getJSONObject(i).getJSONArray("measures");
					if (!label_found)
						for (int j = 0; j < measures_arr.length() && !label_found; ++j)
							if (measures_arr.getJSONObject(j).getString("label")
									.contains(!"s".equals(units.substring(units.length() - 1)) ? units
											: units.substring(0, units.length() - 1))) {
								value_per_100_g = Double.parseDouble(nut_arr.getJSONObject(i).getString("value"));
								unit_to_g = amount * measures_arr.getJSONObject(j).getDouble("eqv");
								nutritions.add(Double.valueOf(value_per_100_g));
								label_found = true;
							}
					if (!label_found) {
						value_per_100_g = Double
								.valueOf(Double.parseDouble(nut_arr.getJSONObject(i).getString("value")));
						String post_processed_units = "s".equals(units.substring(units.length() - 1)) ? units
								: units + "s";
						try {
							String converter_func_name = post_processed_units + "ToGrams";
							Method converter_method = Class.forName(UnitsConverter.class.getName())
									.getDeclaredMethod(converter_func_name, double.class);
							unit_to_g = (double) converter_method.invoke(UnitsConverter.class, amount);
							nutritions.add(Double.valueOf(value_per_100_g));
							label_found = true;
						} catch (NoSuchMethodException e) {
							label_found = false;
						}
					}
					if (!label_found)
						for (int j = 0; j < measures_arr.length() && !label_found; ++j)
							if (measures_arr.getJSONObject(j).getString("label").contains("medium")) {
								value_per_100_g = Double
										.valueOf(Double.parseDouble(nut_arr.getJSONObject(i).getString("value")));
								unit_to_g = amount * measures_arr.getJSONObject(j).getDouble("eqv");
								nutritions.add(Double.valueOf(value_per_100_g));
								label_found = true;
							}
				}
		if (!label_found)
			throw new UnitsNotFoundExcpetion();
		return new Portion(t, food_name, unit_to_g, nutritions.get(0).doubleValue(), nutritions.get(1).doubleValue(),
				nutritions.get(2).doubleValue(), nutritions.get(3).doubleValue());
	}
	
	private static String readAll(final Reader rd) throws IOException {
		final StringBuilder sb = new StringBuilder();
		for (int cp = rd.read(); cp != -1;)
			sb.append((char) cp);
		return sb.toString();
	}

	public static boolean checkForLabel(ArrayList<Double> nutritions, JSONArray measures_arr, JSONArray nut_arr, int nut_arr_idx,
			double amount, String units) {
		boolean label_found = false;		
		/** if not, search the units as actual labels in the JSON, so it can be more precise**/
		
		for( int j=0;j<measures_arr.length() && !label_found ;++j)
			if (measures_arr.getJSONObject(j).getString("label")
					.contains(!"s".equals(units.substring(units.length() - 1)) ? units : units.substring(0, units.length() - 1))) {
				double value_per_100_g = Double.parseDouble(nut_arr.getJSONObject(nut_arr_idx).getString("value")),
						unit_to_g = (1. * (int) (1000.0 * amount * value_per_100_g * measures_arr.getJSONObject(j).getDouble("eqv") / 100)) / 1000.0;
				nutritions.add(Double.valueOf(unit_to_g));
				label_found = true;
			}
		return label_found;
	}
	
	
	/*
	 * creates and returns a JSON object from a url
	 */
	public static JSONObject readJsonFromUrl(final String url) throws IOException, JSONException {
		final InputStream is = new URL(url).openStream();
		try {
			@SuppressWarnings("resource")
			final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			return new JSONObject(readAll(rd));
		} finally {
			is.close();
		}
	}
}