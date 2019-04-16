package Utils;

import static org.junit.Assert.*;

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
		assertEquals((double)1/4, PortionSearchEngine.ComputeRate("avocado", "abc", "one two avocado two"),0.01);
		assertEquals((double)1/2, PortionSearchEngine.ComputeRate("two", "abc", "one two avocado two"),0.01);
		assertEquals((double)2/5+(double)1/3, PortionSearchEngine.ComputeRate("two", "abc", "one two avocado two raw"),0.01);
		assertEquals((double)2/5+1, PortionSearchEngine.ComputeRate("two", "grams", "one two avocado two raw"),0.01);
		assertEquals((double)2/5+(double)1/3, PortionSearchEngine.ComputeRate("two", "abc", "one two avocado two raw (two two)"),0.01);
		assertEquals((double)2/5+(double)1/3, PortionSearchEngine.ComputeRate("two", "abc", "one two, avocado((two two) two raw"),0.01);
		assertEquals((double)2/5+(double)1/3, PortionSearchEngine.ComputeRate("two", "abc", "one-two, avocado((two two) two raw"),0.01);

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
	

}
