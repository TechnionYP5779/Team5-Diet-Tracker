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

package Utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.*;
public class PortionRequestGen {
	
	/** list of nutritional values we want to inject and collect about the wanted food **/
	private static final String[] Nutritional_values = {"Energy","Protein","Carbohydrate, by difference","Total lipid (fat)"};
	
	public static Portion generatePortion(String portion_name, Portion.Type t) {
     try {
    	 return PortionRequestGen.generatePortionHandler(portion_name,t);
        } catch (Exception e) {
         e.printStackTrace();
       }
	return null;
     }
	   
	public static Portion generatePortionHandler(String food_name, Portion.Type t) throws Exception {
		
		String urlquery = "https://api.nal.usda.gov/ndb/search/?format=json&q="+food_name.replace(' ','_')+"&max=5&offset=0&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul";
		HttpURLConnection con = (HttpURLConnection) new URL(urlquery).openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
	    //add request header
	    con.setRequestProperty("User-Agent", "Mozilla/5.0");
	     
	    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();
	    while ((inputLine = in.readLine()) != null)
	    	response.append(inputLine);
	    in.close();
	    
	    /** <\> @code "ndbno"<\> contains the id_no of the product in the general USDA DB**/
	    return queryItem(new JSONObject(response.toString()).getJSONObject("list").getJSONArray("item").getJSONObject(0)
			.getString("ndbno"), food_name, t);   
   }

	public static Portion queryItem(String id,String food_name, Portion.Type t) throws Exception {
		String urlitem = "https://api.nal.usda.gov/ndb/reports/?ndbno="+id+"&type=b&format=json&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul";
	    HttpURLConnection con = (HttpURLConnection) new URL(urlitem).openConnection();
	    // optional default is GET
	    con.setRequestMethod("GET");
	    //add request header
	    con.setRequestProperty("User-Agent", "Mozilla/5.0");
	     
	    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();
	    while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
	    in.close();
	     
	    //Read JSON response and print
	    JSONObject myResponse = new JSONObject(response.toString());
	    ArrayList<Double> nutritions = new ArrayList<>();
	    /** get (from json) the array that stores the nutritional values we want**/
	    JSONArray nut_arr = myResponse.getJSONObject("report").getJSONObject("food").getJSONArray("nutrients");
	    /** for each value: look for it in the array and insert its numeric value to the list**/
   	    for(String nut : Nutritional_values)
   	    	for (int i = 0; i < nut_arr.length(); ++i)
				if (nut_arr.getJSONObject(i).getString("name").equals(nut))
					nutritions.add(Double.parseDouble(nut_arr.getJSONObject(i).getString("value")));
   	     
   	    /** right now, assignment is done in-order, according to the order of the values
   	     *  in "Nutritional_values" .
   	     *  later on, assignment will be using reflection. just wait for it :) **/
   	    return new Portion(t,food_name,100.0,nutritions.get(0),nutritions.get(1),nutritions.get(2),nutritions.get(3));
	}
}