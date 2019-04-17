package Utils;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

import com.amazon.ask.model.services.Pair;

import Utils.PortionSearchEngine.SearchResults;

@SuppressWarnings("static-method")
public  class PortionSearchEngineTest {

	@Test
	public void NumOfWordsInSentenceTest() {
		assertEquals(0, PortionSearchEngine.NumOfWordsInSentence(""));
		assertEquals(0, PortionSearchEngine.NumOfWordsInSentence(" "));
		assertEquals(0, PortionSearchEngine.NumOfWordsInSentence("     "));
		assertEquals(1, PortionSearchEngine.NumOfWordsInSentence("a"));
		assertEquals(1, PortionSearchEngine.NumOfWordsInSentence("a "));
		assertEquals(1, PortionSearchEngine.NumOfWordsInSentence(" a"));
		assertEquals(1, PortionSearchEngine.NumOfWordsInSentence(" a "));
		assertEquals(5, PortionSearchEngine.NumOfWordsInSentence(" a dog enters a   bar "));
		assertEquals(4, PortionSearchEngine.NumOfWordsInSentence("it isn't that bad"));
		assertEquals(3, PortionSearchEngine.NumOfWordsInSentence("instruction-based commands"));
	}
	
	@Test
	public void StringToCanonicalFormTest() {
		assertEquals("", PortionSearchEngine.StringToCanonicalForm(""));
		assertEquals("", PortionSearchEngine.StringToCanonicalForm("      "));
		assertEquals("a", PortionSearchEngine.StringToCanonicalForm("    a  "));
		assertEquals("a b", PortionSearchEngine.StringToCanonicalForm("    a   b "));
		assertEquals("instruction based commands", PortionSearchEngine.StringToCanonicalForm(" instruction-baSed    commands "));
		assertEquals("instruction based commands", PortionSearchEngine.StringToCanonicalForm(" instruction-baseD    commands(must be removed)"));
	}
	
	@Test
	public void ComputeRateTest() {
		assertEquals((double)1/4, PortionSearchEngine.ComputeRate("avocado", "abc", "one two avocado two",false),0.01);
		assertEquals((double)1/2, PortionSearchEngine.ComputeRate("two", "abc", "one two avocado two",false),0.01);
		assertEquals((double)2/5+(double)1/3, PortionSearchEngine.ComputeRate("two", "abc", "one two avocado two raw",false),0.01);
		assertEquals((double)2/5+(double)1/3, PortionSearchEngine.ComputeRate("two", "grams", "one two avocado two raw",false),0.01);
		assertEquals((double)2/5+(double)1/3, PortionSearchEngine.ComputeRate("two", "abc", "one two avocado two raw (two two)",false),0.01);
		assertEquals((double)2/5+(double)1/3, PortionSearchEngine.ComputeRate("two", "abc", "one two, avocado((two two) two raw",false),0.01);
		assertEquals((double)2/5+(double)1/3, PortionSearchEngine.ComputeRate("two", "abc", "one-two, avocado((two two) two raw",false),0.01);

	}
	
	@Test
	public void PortionSearchTest() {
		Pair<SearchResults, Portion> tempPair=PortionSearchEngine.PortionSearch("saadshgerh","small",Portion.Type.FOOD,1);
		assertEquals(SearchResults.SEARCH_NO_RESULTS, tempPair.getName());
		
		tempPair=PortionSearchEngine.PortionSearch("apPle","small",Portion.Type.FOOD,1);
		assertEquals(SearchResults.SEARCH_FULL_SUCCESS, tempPair.getName());
		assertEquals(149.0, tempPair.getValue().getAmount(),0.1);
		
		tempPair=PortionSearchEngine.PortionSearch("pizZA","slice",Portion.Type.FOOD,1);
		assertEquals(SearchResults.SEARCH_FULL_SUCCESS, tempPair.getName());
		assertEquals(280.0, tempPair.getValue().getCalories_per_100_grams(),0.1);
		
		tempPair=PortionSearchEngine.PortionSearch("nature VALLey","bar",Portion.Type.FOOD,1);
		assertEquals(SearchResults.SEARCH_FULL_SUCCESS, tempPair.getName());
		
		tempPair=PortionSearchEngine.PortionSearch("sprITe","can",Portion.Type.FOOD,1);
		assertEquals(SearchResults.SEARCH_GOOD_ESTIMATED_SUCCESS, tempPair.getName());
		assertEquals(40.0, tempPair.getValue().getCalories_per_100_grams(),0.1);

	}
	
	/** basic tests to check if the converter works 
	 * @throws Exception **/
	@Test 
	public void CheckConvertionsTest1() throws Exception {
		assertEquals((double)1, PortionSearchEngine.CheckConvertions("grams", 1.0),0.01);
		assertEquals((double)1000, PortionSearchEngine.CheckConvertions("kilograms", 1.0),0.01);
		assertEquals((double)0.001, PortionSearchEngine.CheckConvertions("milligrams", 1.0),0.01);
		assertEquals((double)28.3495, PortionSearchEngine.CheckConvertions("ounce", 1.0),0.01);
		assertEquals((double)28.3495, PortionSearchEngine.CheckConvertions("ounces", 1.0),0.01);
		assertEquals((double)-1, PortionSearchEngine.CheckConvertions("gallon", 1.0),0.01);

		
	}
	/** more advanced tests to check if the converter works,
	 * try to take the strings from a real JSON-object as we actually do 
	 * @throws Exception **/
	@Test
	public void CheckConvertionsTest2() throws Exception{
		
		/** an example for a pre-defined convertion in the converter,
		 * ounces convertion to grams is defined in our converter **/
		final JSONObject res1 = PortionRequestGen.readJsonFromUrl("https://api.nal.usda.gov/ndb/reports/?ndbno=45094075&type=b&format=json&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
		assertEquals(1+(double)2/8,PortionSearchEngine.ComputeRate("bacon","ounce",res1.getJSONObject("report").getJSONObject("food").getString("name"),
						PortionSearchEngine.CheckConvertions("ounce",2)>=0),0.01);
		
		/** an example for non-defined convertion in the converter:
		 *  a soda "can" is not defined in converter, neither towards liters nor grams **/
		final JSONObject res2 = PortionRequestGen.readJsonFromUrl("https://api.nal.usda.gov/ndb/reports/?ndbno=14145&type=b&format=json&api_key=Unjc2Z4luZu0sKFBGflwS7cnxEiU83YygiIU37Ul");
		assertEquals((double)1/7,PortionSearchEngine.ComputeRate("sprite","can",res2.getJSONObject("report").getJSONObject("food").getString("name"),
						PortionSearchEngine.CheckConvertions("can",2)>=0),0.01);
	}

}
