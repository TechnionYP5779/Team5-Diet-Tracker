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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazon.ask.model.services.Pair;
import com.google.gson.JsonObject;

import Utils.DB.DBUtils;
import Utils.Portion.Portion;
import Utils.Portion.PortionRequestGen;

public class PortionSearchEngine {

	private static final String MAX_ELEMENTS_WITHOUT_RAW = "5";
	private static final String MAX_ELEMENTS_WITH_RAW = "2";
	private static final String MAX_ELEMENTS_WITH_BRANDED = "2";
	private static final String[] Nutritional_values = { "Energy", "Protein", "Carbohydrate, by difference",
			"Total lipid (fat)" };

	public enum SearchResults {
		SEARCH_FULL_SUCCESS, SEARCH_GOOD_ESTIMATED_SUCCESS, SEARCH_BAD_ESTIMATED_SUCCESS, SEARCH_NO_RESULTS,
		SEARCH_ERROR
	}

	/**
	 * @author ShalevKuba
	 * @param str - sentence to count its number of words NOTE - assuming the string
	 *            is without parenthesis
	 * @return number of words in the given sentence
	 */
	static int NumOfWordsInSentence(String str) {

		String cleanStr = str.replaceAll("-", " ");
		cleanStr = cleanStr.trim().replaceAll(" +", " ");
		boolean spareSpacesFlag = false;
		int count = cleanStr.isEmpty() ? 0 : 1;
		for (int i = 0; i < cleanStr.length(); i++) {
			if (cleanStr.charAt(i) != ' ')
				spareSpacesFlag = true;
			else if (spareSpacesFlag && i != cleanStr.length() - 1) {
				count++;
				spareSpacesFlag = false;
			}
		}
		return count;
	}

	/**
	 * @author ShalevKuba
	 * @param str - a string
	 * @return a canonical form string of the given string
	 */
	static String StringToCanonicalForm(String str) {
		// rule 1: no "-"
		String cleanStr = str.replaceAll("-", " ");
		// rule 2: No parenthesis
		cleanStr = cleanStr.replaceAll("\\(.*\\)", " ");

		// rule 3: No spare spaces
		// rule 4: No leading or trailing whitespace
		cleanStr = cleanStr.trim().replaceAll(" +", " ");
		// rule 5: Lower case
		cleanStr = cleanStr.toLowerCase();

		return cleanStr;
	}

	/**
	 * @author ShalevKuba
	 * @param str - a given string
	 * @param str - a given substring
	 * @return number of occurrences of substr in str
	 */
	static int CountOccurrencesOfSubstring(String str, String substr) {
		int count = 0, fromIndex = 0;

		while ((fromIndex = str.indexOf(substr, fromIndex)) != -1) {
			count++;
			fromIndex++;

		}
		return count;
	}

	/**
	 * @author ShalevKuba
	 * @param searchStr - a search string
	 * @param str       - a string of portion from USDADB
	 * @param unit      - the measuring unit
	 * @return the rate of str according to the search string searchStr
	 */
	static double ComputeRate(String searchStr, String unit, String str, final boolean convertionExists) {
		String cleanSearchStr = StringToCanonicalForm(searchStr);
		String cleanStr = StringToCanonicalForm(str);

		int countOccurrences = 0, numOfWords = NumOfWordsInSentence(cleanStr);
		String[] words = cleanSearchStr.split(" ");

		// count occurrences
		for (String word : words)
			countOccurrences += CountOccurrencesOfSubstring(cleanStr, word);

		double rate = countOccurrences / ((double) numOfWords);
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Now it handles any kind of conversion
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		if (convertionExists)
			rate += 1;
		else if (cleanStr.contains("raw"))
			rate += ((double) 1) / 3;

		return rate;
	}

	/**
	 * @author Shaked Sapir
	 * @param units  - the portion units provided by user
	 * @param amount - the portion quantity / amount provided by user
	 * @return the amount of portion after the conversion if a proper conversion
	 *         exists, otherwise -1
	 * @throws NoSuchMethodException if the converter has no proper method for @code
	 *                               units
	 */

	static double CheckConvertions(final String units, final double amount) throws Exception {
		String post_processed_units = "s".equals(units.substring(units.length() - 1)) ? units : units + "s";
		double unit_to_g = 0.0, unit_to_l = 0.0;
		try {
			/**
			 * now we can find the right converter-method to invoke for solids, using
			 * reflection
			 */
			String converter_to_grams_func_name = post_processed_units + "ToGrams";
			Method grams_converter_method = Class.forName(UnitsConverter.class.getName())
					.getDeclaredMethod(converter_to_grams_func_name, double.class);

			/** calculate the amount in grams of the original unit **/
			unit_to_g = (double) grams_converter_method.invoke(UnitsConverter.class, amount);
			/**
			 * probably we dont have to calc the exact amount as it is calculated in
			 * DailyInfo.java
			 **/

		} catch (NoSuchMethodException e) {
			/**
			 * TODO if reached here, then we have a lack in conversion methods, we should
			 * add one and notify the user about this
			 */
			unit_to_g = -1;
		}
		try {
			/**
			 * now we can find the right converter-method to invoke for liquids, using
			 * reflection
			 */
			String converter_to_liters_func_name = post_processed_units + "ToLiters";
			Method liters_converter_method = Class.forName(UnitsConverter.class.getName())
					.getDeclaredMethod(converter_to_liters_func_name, double.class);

			/** calculate the amount in grams of the original unit **/
			unit_to_l = (double) liters_converter_method.invoke(UnitsConverter.class, amount);
			/**
			 * probably we dont have to calc the exact amount as it is calculated in
			 * DailyInfo.java
			 **/

		} catch (NoSuchMethodException e) {
			/**
			 * TODO if reached here, then we have a lack in conversion methods, we should
			 * add one and notify the user about this
			 */
			unit_to_l = -1;
		}
		return Math.max(unit_to_g, unit_to_l);
	}

	// -------------------------------------------------------------------------
	// ---------------------------JSON Functions Start---------------------------
	// -------------------------------------------------------------------------

	private static String readAll(final Reader rd) throws IOException {
		final StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1)
			sb.append((char) cp);
		return sb.toString();
	}

	private static JSONObject readJsonFromUrl(final String url) throws IOException, JSONException {
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

	// -------------------------------------------------------------------------
	// ---------------------------JSON Functions End-----------------------------
	// -------------------------------------------------------------------------

	/**
	 * @author ShalevKuba The main function of the search engine
	 * @param freeText - the text we got from the user
	 * @param unit     - the measuring unit we got from the user
	 * @param t        - type of the portion
	 * @param amount   - amount (according to the unit given)
	 * @param userEmail   - the mail of the user
	 * @return type is SearchResults. This function returns value which represents
	 *         the search result.
	 * @return pair of searching result and the desired portion if the searching was
	 *         successful.
	 */
	public static Pair<SearchResults, Portion> PortionSearch(String freeText, String unit, final Portion.Type t,
			double amount,String userEmail) {
		try {
			String freeTextToReq = freeText.replaceAll(" ", "%20");
			freeTextToReq = freeTextToReq.toLowerCase();
			//check the cache
			Optional<Portion> optCachePortion=GetPortionFromCache(userEmail,freeTextToReq, unit,t);
			if(optCachePortion.isPresent()) 
				return new Pair<SearchResults, Portion>(SearchResults.SEARCH_FULL_SUCCESS,
						optCachePortion.get()); 
			
			// Do the requests - the cache didnt help
			final JSONObject responseWithoutRaw = readJsonFromUrl("https://api.nal.usda.gov/ndb/search/?format=json&q="
					+ freeTextToReq + "&ds=Standard%20Reference&sort=r&max=" + MAX_ELEMENTS_WITHOUT_RAW
					+ "&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
			final JSONObject responseWithRaw = readJsonFromUrl("https://api.nal.usda.gov/ndb/search/?format=json&q="
					+ freeTextToReq + "%20raw" + "&0&max=" + MAX_ELEMENTS_WITH_RAW
					+ "&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
			final JSONObject responseWithBranded = readJsonFromUrl("https://api.nal.usda.gov/ndb/search/?format=json&q="
					+ freeTextToReq +"&ds=Branded%20Food%20Products&sort=r&max=" + MAX_ELEMENTS_WITH_BRANDED
					+ "&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");

			List<Pair<String, String>> portion_list = new ArrayList<>();
			// check for error in the "WithoutRaw" request
			if (responseWithoutRaw.has("list")) {

				int sizeNoRaw = responseWithoutRaw.getJSONObject("list").getInt("end");
				for (int i = 0; i < sizeNoRaw; i++)
					portion_list.add(new Pair<String, String>(
							responseWithoutRaw.getJSONObject("list").getJSONArray("item").getJSONObject(i)
									.getString("name"),
							responseWithoutRaw.getJSONObject("list").getJSONArray("item").getJSONObject(i)
									.getString("ndbno")));

			}

			// check for error in the "WithRaw" request
			if (responseWithRaw.has("list")) {

				int sizeRaw = responseWithRaw.getJSONObject("list").getInt("end");
				for (int i = 0; i < sizeRaw; i++)
					portion_list.add(new Pair<String, String>(
							responseWithRaw.getJSONObject("list").getJSONArray("item").getJSONObject(i)
									.getString("name"),
							responseWithRaw.getJSONObject("list").getJSONArray("item").getJSONObject(i)
									.getString("ndbno")));

			}

			// check for error in the "WithBranded" request
			if (responseWithBranded.has("list")) {
				int sizeBranded = responseWithBranded.getJSONObject("list").getInt("end");
				for (int ¢ = 0; ¢ < sizeBranded; ++¢)
					portion_list.add(new Pair<String, String>(
							responseWithBranded.getJSONObject("list").getJSONArray("item").getJSONObject(¢)
									.getString("name"),
							responseWithBranded.getJSONObject("list").getJSONArray("item").getJSONObject(¢)
									.getString("ndbno")));

			}

			if (portion_list.isEmpty())
				return new Pair<SearchResults, Portion>(SearchResults.SEARCH_NO_RESULTS, null);

			portion_list.sort(new Comparator<Pair<String, String>>() {
				@Override
				public int compare(Pair<String, String> p1, Pair<String, String> p2) {
					double res = ComputeRate(freeText, unit, p1.getName(), false)
							- ComputeRate(freeText, unit, p2.getName(), false);
					if (res > 0)
						return -1;
					if (res < 0)
						return 1;
					return 0;
				}
			});

//			for(Pair<String,String> p : portion_list)
//				System.out.println(p.getValue() + " , "+p.getName());
			for (Pair<String, String> p : portion_list) {

				final JSONObject nutrientsResponse = PortionRequestGen
						.readJsonFromUrl("https://api.nal.usda.gov/ndb/reports/?ndbno=" + p.getValue()
								+ "&type=b&format=json&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");

				final JSONArray nut_arr = nutrientsResponse.getJSONObject("report").getJSONObject("food")
						.getJSONArray("nutrients");

				final JSONArray measures_arr = nut_arr.getJSONObject(0).getJSONArray("measures");

				/**
				 * we have to validate that there are values in measures_arr, and if so - we
				 * ensure that it doesn't contain only "null"
				 */
				int measures_arr_len = measures_arr.length();
				if (measures_arr_len > 0 && !(measures_arr.get(0).equals(null))) {
					for (int i = 0; i < measures_arr_len; ++i) {
						if (measures_arr.getJSONObject(i).getString("label").contains(unit)) {
							
							//cache since it was full success
							AddResponseToCache(userEmail, freeTextToReq, nutrientsResponse);
							
							return new Pair<SearchResults, Portion>(SearchResults.SEARCH_FULL_SUCCESS,
									GetPortionFromNutrientsResponse(nut_arr, t, p.getName(),
											measures_arr.getJSONObject(i).getDouble("eqv")));
						}

					}
				}

			}

			// no full success
			final JSONObject nutrientsResponse = PortionRequestGen
					.readJsonFromUrl("https://api.nal.usda.gov/ndb/reports/?ndbno=" + portion_list.get(0).getValue()
							+ "&type=b&format=json&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");

			final JSONArray nut_arr = nutrientsResponse.getJSONObject("report").getJSONObject("food")
					.getJSONArray("nutrients");

			// TODO: change the '-1' to either the proper conversion, if exists, or :
			// TODO: the default USDA's gram units for some label that we need to choose
			Portion portion = GetPortionFromNutrientsResponse(nut_arr, t, portion_list.get(0).getName(), -1);

			/** set the boolean flag to "true" if the conversion exists **/
			final boolean convertionExists = CheckConvertions(unit, amount) > -1;
			double rateFirst = ComputeRate(freeText, unit, portion_list.get(0).getName(), convertionExists);
			if (rateFirst < 1 / 3)
				return new Pair<SearchResults, Portion>(SearchResults.SEARCH_BAD_ESTIMATED_SUCCESS, portion);
			else
				return new Pair<SearchResults, Portion>(SearchResults.SEARCH_GOOD_ESTIMATED_SUCCESS, portion);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new Pair<SearchResults, Portion>(SearchResults.SEARCH_ERROR, null);
		}
	}

	/**
	 * @author ShalevKuba
	 * @param nut_arr   - the Json response
	 * @param t         - type of the portion
	 * @param name      - name of the portion
	 * @param unit_to_g - convertion between unit to grams
	 * @return portion object according to the given Json, null if there is a
	 *         problem
	 */
	static Portion GetPortionFromNutrientsResponse(JSONArray nut_arr, final Portion.Type t, String name,
			double unit_to_g) {
		final ArrayList<Double> nutritions = new ArrayList<>();
		double unit_to_g_tmp=unit_to_g;
		JSONArray measures_json=nut_arr.getJSONObject(0).getJSONArray("measures");
		if(unit_to_g<0) {
			if(measures_json.length()>0) {
				for (int i = 0; i < measures_json.length(); ++i) {
					if (measures_json.getJSONObject(0).getString("label").contains("serving")) {
						
						unit_to_g_tmp=measures_json.getJSONObject(0).getDouble("value");
						break;
					}
				}
			unit_to_g_tmp=1;
			}
		}
		
		for (final String nut : Nutritional_values)
			for (int i = 0; i < nut_arr.length(); ++i)
				if (nut_arr.getJSONObject(i).getString("name").equals(nut)) {
					nutritions.add(Double.valueOf(Double.parseDouble(nut_arr.getJSONObject(i).getString("value"))));
					break;
				}
		return new Portion(t, name, unit_to_g_tmp, nutritions.get(0).doubleValue(), nutritions.get(1).doubleValue(),
				nutritions.get(2).doubleValue(), nutritions.get(3).doubleValue());
	}
	
	
	/**
	 * @author ShalevKuba
	 * 
	 * add the response to the cache of the user
	 * 
	 * @param response   - the Json response
	 * @param userText        - the text the user said
	 * @param userEmail      - the user mail
	 */
	static void AddResponseToCache(String userEmail,String userText, JSONObject response) {
		final DBUtils db = new DBUtils(userEmail);
		try {
			db.DBAddPortionToCache(userText, response);
		} catch (DBUtils.DBException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * @author ShalevKuba
	 * 
	 *
	 * 
	 * @param unit   - the unit the user used
	 * @param t - the type of the portion
	 * @param userText        - the text the user said
	 * @param userEmail      - the user mail
	 * @return optional of portion if the unit is consistent with the cache, otherwise returns empty optional
	 */
	static Optional<Portion> GetPortionFromCache(String userEmail,String userText, String unit,Portion.Type t) {
		final DBUtils db = new DBUtils(userEmail);
		try {
			Optional<JSONObject> optJsonResponse=db.DBGetPortionFromCache(userText);
			if(!optJsonResponse.isPresent()) return Optional.empty();
			JSONObject jsonResponse=optJsonResponse.get();
			
			
			final JSONArray nut_arr = jsonResponse.getJSONObject("report").getJSONObject("food")
					.getJSONArray("nutrients");

			final JSONArray measures_arr = nut_arr.getJSONObject(0).getJSONArray("measures");

			// searching for the unit
			int measures_arr_len = measures_arr.length();
			if (measures_arr_len > 0 && !(measures_arr.get(0).equals(null))) {
				for (int i = 0; i < measures_arr_len; ++i) {
					if (measures_arr.getJSONObject(i).getString("label").contains(unit))
						
						return Optional.ofNullable(GetPortionFromNutrientsResponse(nut_arr, t, userText,
										measures_arr.getJSONObject(i).getDouble("eqv")));
				}
			}
		} catch (DBUtils.DBException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return Optional.empty();
	}
	
}
