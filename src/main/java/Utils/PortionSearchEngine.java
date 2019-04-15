package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazon.ask.model.services.Pair;

public class PortionSearchEngine {

	private static final String MAX_ELEMENTS_WITHOUT_RAW = "5";
	private static final String MAX_ELEMENTS_WITH_RAW = "2";
	private static final String[] Nutritional_values = { "Energy", "Protein", "Carbohydrate, by difference",
			"Total lipid (fat)" };

	public enum SearchResults {
		SEARCH_FULL_SUCCESS, SEARCH_GOOD_ESTIMATED_SUCCESS,SEARCH_BAD_ESTIMATED_SUCCESS, SEARCH_NO_RESULTS, SEARCH_ERROR
	}

	/**
	 * @author ShalevKuba
	 * @param str - sentence to count its number of words NOTE - assuming the string
	 *            is without parenthesis
	 * @return number of words in the given sentence
	 */
	private static int NumOfWordsInSentence(String str) {
		int count = 0;
		boolean spareSpacesFlag = false;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != ' ')
				spareSpacesFlag = true;
			else if (spareSpacesFlag && i != str.length() - 1) {
				count++;
				spareSpacesFlag = false;
			}
		}
		if (count > 0 && str.charAt(str.length() - 1) == ' ')
			count--;
		return count;
	}

	/**
	 * @author ShalevKuba
	 * @param str - a string
	 * @return a canonical form string of the given string
	 */
	private static String StringToCanonicalForm(String str) {
		// rule 1: No parenthesis
		String cleanStr = str.replaceAll("\\(.*\\)", "");
		// rule 2: no "-"
		cleanStr = cleanStr.replaceAll("-", "");
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
	private static int CountOccurrencesOfSubstring(String str, String substr) {
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
	static double ComputeRate(String searchStr, String unit, String str) {
		// turns string to canonical form
		String cleanSearchStr = StringToCanonicalForm(searchStr);
		String cleanStr = StringToCanonicalForm(str);

		int countOccurrences = 0, numOfWords = NumOfWordsInSentence(cleanStr);
		String[] words = cleanSearchStr.split(" ");

		// count occurrences
		for (String word : words)
			countOccurrences += CountOccurrencesOfSubstring(cleanStr, word);

		double rate = countOccurrences / ((double) numOfWords);

		// TODO: to expand it more than just gram
		if (unit.contains("gram"))
			rate += 1;
		else if (cleanStr.contains("raw"))
			rate += ((double) 1) / 3;

		return rate;
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
	 * @return type is SearchResults. This function returns value which represents
	 *         the search result.
	 * @return pair of searching result and the desired portion if the searching was
	 *         successful.
	 */
	public static Pair<SearchResults, Portion> PortionSearch(String freeText, String unit, final Portion.Type t,
			double amount) {
		try {
			String freeTextToReq=freeText.replaceAll(" ", "%20");
			// Do the requests
			final JSONObject responseWithoutRaw = readJsonFromUrl("https://api.nal.usda.gov/ndb/search/?format=json&q="
					+ freeTextToReq + "&ds=Standard%20Reference&sort=r&max=" + MAX_ELEMENTS_WITHOUT_RAW
					+ "&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
			final JSONObject responseWithRaw = readJsonFromUrl("https://api.nal.usda.gov/ndb/search/?format=json&q="
					+ freeTextToReq + "%20raw" + "&ds=Standard%20Reference&sort=r&max=" + MAX_ELEMENTS_WITH_RAW
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

			if (portion_list.isEmpty())
				return new Pair<SearchResults, Portion>(SearchResults.SEARCH_NO_RESULTS, null);

			portion_list.sort(new Comparator<Pair<String, String>>() {
				@Override
				public int compare(Pair<String, String> p1, Pair<String, String> p2) {
					double res = ComputeRate(freeText, unit, p1.getName()) - ComputeRate(freeText, unit, p2.getName());
					if (res > 0)
						return -1;
					if (res < 0)
						return 1;
					return 0;
				}
			});

			for (Pair<String, String> p : portion_list) {
				final JSONObject nutrientsResponse = PortionRequestGen
						.readJsonFromUrl("https://api.nal.usda.gov/ndb/reports/?ndbno=" + p.getValue()
								+ "&type=b&format=json&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");

				final JSONArray nut_arr = nutrientsResponse.getJSONObject("report").getJSONObject("food")
						.getJSONArray("nutrients");

				final JSONArray measures_arr = nut_arr.getJSONObject(0).getJSONArray("measures");
				for (int i = 0; i < measures_arr.length(); ++i) {
					if (measures_arr.getJSONObject(i).getString("label").contains(unit))
						return new Pair<SearchResults, Portion>(SearchResults.SEARCH_FULL_SUCCESS,
								GetPortionFromNutrientsResponse(nut_arr, t, p.getName(),
										measures_arr.getJSONObject(i).getDouble("eqv")));

				}
			}

			final JSONObject nutrientsResponse = PortionRequestGen
					.readJsonFromUrl("https://api.nal.usda.gov/ndb/reports/?ndbno=" + portion_list.get(0).getValue()
							+ "&type=b&format=json&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");

			final JSONArray nut_arr = nutrientsResponse.getJSONObject("report").getJSONObject("food")
					.getJSONArray("nutrients");
			
			Portion portion=GetPortionFromNutrientsResponse(nut_arr, t, portion_list.get(0).getName(),1);
			
			// no full success
			double rateFirst = ComputeRate(freeText, unit, portion_list.get(0).getName());
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

		for (final String nut : Nutritional_values)
			for (int i = 0; i < nut_arr.length(); ++i)
				if (nut_arr.getJSONObject(i).getString("name").equals(nut)) {
					nutritions.add(Double.valueOf(Double.parseDouble(nut_arr.getJSONObject(i).getString("value"))));
					break;
				}
		return new Portion(t, name, unit_to_g, nutritions.get(0).doubleValue(), nutritions.get(1).doubleValue(),
				nutritions.get(2).doubleValue(), nutritions.get(3).doubleValue());
	}
}
