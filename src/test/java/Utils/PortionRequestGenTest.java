/** a test module for class PortionRequestGen.
 * @author Shaked Sapir
 * @since 2018-12-17*/
package Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.amazon.ask.model.services.Pair;

public class PortionRequestGenTest {

	@Test
	/** a simple test to check a simple conversion between units**/
	public void test_grams() {
		Portion p = PortionRequestGen.generatePortionWithAmount("banana", Portion.Type.FOOD, 50, "milligrams");
		String s1 = p.toString();

		assert s1.contains("Nutritional Values per 100 grams:\nCalories: 89.0\nProteins: 1.09\n"
				+ "Carbohydrates: 22.84\nFats: 0.33\n");
		assert p.getAmount() == 0.05;
		
	}
	
	@Test
	/** a test to check we can get portion in units other than grams**/
	public void test_cup() {
		assert PortionRequestGen.generatePortionWithAmount("banana", Portion.Type.FOOD, 2, "cups").toString()
				.contains("Nutritional Values per 100 grams:\nCalories: 89.0\nProteins: 1.09\n"
						+ "Carbohydrates: 22.84\nFats: 0.33\n");

	}
	
	@Test
	public void test_cup2() {
		/**this should return the value for cup of rice, as appears in the usda JSON*/
		Portion p = PortionRequestGen.generatePortionWithAmount("rice", Portion.Type.FOOD, 2, "cups");
		String s = p.toString();
		assert s.contains("Nutritional Values per 100 grams:\nCalories: 357.0\nProteins: 14.73\n"
				+ "Carbohydrates: 74.9\nFats: 1.08\n");
		assert p.getAmount() == 320.0;

	}
	@Test
	/** a test to check that we can search for food in "raw" version. such as eggs, fruits..**/
	public void check_raw_egg_suffix() {
		assert PortionRequestGen.generatePortionWithAmount("egg", Portion.Type.FOOD, 1, "large").toString()
				.contains("Nutritional Values per 100 grams:\nCalories: 143.0\nProteins: 12.56\n"
						+ "Carbohydrates: 0.72\nFats: 9.51\n");

	}
	
	@Test
	/** a test to check that we can search for food in "raw" version. such as eggs, fruits..**/
	public void new_algorithm() {
		String input_name="banana";
		String unit="small";
		try {
			final JSONObject myResponseClean = PortionRequestGen.readJsonFromUrl("https://api.nal.usda.gov/ndb/search/?format=json&q="+input_name+"&ds=Standard%20Reference&sort=r&max=5&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
			final JSONObject myResponseRaw = PortionRequestGen.readJsonFromUrl("https://api.nal.usda.gov/ndb/search/?format=json&q="+input_name+"%20raw"+"&ds=Standard%20Reference&sort=r&max=2&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
			List<Pair<String, String>> portion_list=new ArrayList<>();
			//TODO: handle search error
			int sizeClean=myResponseClean.getJSONObject("list").getInt("end");
			for(int i=0;i<sizeClean;i++) {
				portion_list.add(new Pair<String, String>(myResponseClean.getJSONObject("list").getJSONArray("item").getJSONObject(i).getString("name"), 
						myResponseClean.getJSONObject("list").getJSONArray("item").getJSONObject(i).getString("ndbno")));
			}
			int sizeRaw=myResponseRaw.getJSONObject("list").getInt("end");
			for(int i=0;i<sizeRaw;i++) {
				portion_list.add(new Pair<String, String>(myResponseRaw.getJSONObject("list").getJSONArray("item").getJSONObject(i).getString("name"), 
						myResponseRaw.getJSONObject("list").getJSONArray("item").getJSONObject(i).getString("ndbno")));
			}
			portion_list.sort(new Comparator<Pair<String, String>>() { 
				@Override public int compare(Pair<String, String> p1, Pair<String, String> p2) {
					double res= computeRate(input_name,p1.getName())-computeRate(input_name,p2.getName());
					if(res>0) return -1;
					if(res<0) return 1;
					return 0;
					}
			}
			);
			
			/*for(Pair<String, String> p : portion_list) {
				System.out.println("the res of "+p.getName().toLowerCase()+" is :"+computeRate(input_name,p.getName()));
			}*/
			//now the list is sorted
			
			for(Pair<String, String> p : portion_list) {
				final JSONObject myResponse = PortionRequestGen.readJsonFromUrl("https://api.nal.usda.gov/ndb/reports/?ndbno=" + p.getValue()
						+ "&type=b&format=json&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
				/** get (from json) the array that stores the nutritional values we want **/
				final JSONArray measures_arr = myResponse.getJSONObject("report").getJSONObject("food").getJSONArray("nutrients").getJSONObject(0).getJSONArray("measures");
				for (int i = 0; i < measures_arr.length(); ++i) {
					if(measures_arr.getJSONObject(i).getString("label").contains(unit)) {
						System.out.println("The ndbno:");
						System.out.println(p.getValue());
						System.out.println("The name:");
						System.out.println(p.getName());
						System.out.println("The selected measure:");
						System.out.println(measures_arr.getJSONObject(i).toString());
						return;
					}
				}
			}
			
			//no desired quantity unit
			double rateFirst=computeRate(input_name,portion_list.get(0).getName());
			if(rateFirst<1/3) {
				//notify error to user
			}else {
				final JSONObject myResponse = PortionRequestGen.readJsonFromUrl("https://api.nal.usda.gov/ndb/reports/?ndbno=" + portion_list.get(0).getValue()
				+ "&type=b&format=json&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
		/** get (from json) the array that stores the nutritional values we want **/
		final JSONArray measures_arr = myResponse.getJSONObject("report").getJSONObject("food").getJSONArray("nutrients").getJSONObject(0).getJSONArray("measures");
		
				System.out.println("The ndbno:");
				System.out.println(portion_list.get(0).getValue());
				System.out.println("The name:");
				System.out.println(portion_list.get(0).getName());
				return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/** 
	 * This function computes the "good/bad" words rate in the portion's name
	 * in USDA , i.e. the following rate:
	 * Portion_rate = (number of matching words)/(number of total words, without parenthesis content) 
	 * @param search the substring to look for inside @code str 
	 * @param str the original string to look in
	 * @return Portion_rate, as explained above
	 */
	static double computeRate(String search,String str) {
		
		String clean_string=str.toLowerCase();

		/** get rid of parenthesis **/
		int par_index=str.indexOf(" (");
		if(par_index!=-1) {
			clean_string=clean_string.substring(0, par_index);
		}
		
		//TODO: handle when search is more than one word
		
		int count_occurrances = (clean_string.split(search, -1).length)-1;

		/** splits the original @code clean_string with delimiter,
		 * currently it is a blank space, we should consider the option
		 * for a comma (',')
		 */
		int num_of_words = clean_string.split(" ",-1).length;
		
		double rate=count_occurrances/((double)num_of_words);
				
		//TODO for now we assume we cannot use our converter,
		//handle situation that we can and treat "raw" accordingly
		
		/** currently, a constant of 1/3 is added to the result
		 *  rate for "raw" appearance.
		 */
		
		if(clean_string.contains("raw")) rate+=((double)1)/3;
		
		return rate;
	}
	
//	 public static int count_Words(String str)
//	    {
//	       int count = 0;
//	        if (!(" ".equals(str.substring(0, 1))) || !(" ".equals(str.substring(str.length() - 1))))
//	        {
//	            for (int i = 0; i < str.length(); i++)
//	            {
//	                if (str.charAt(i) == ' ')
//	                {
//	                    count++;
//	                }
//	            }
//	            count = count + 1; 
//	        }
//	        return count; // returns 0 if string starts or ends with space " ".
//	    }
//	 
	
}