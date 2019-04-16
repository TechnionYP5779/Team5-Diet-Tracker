package Utils;

import static org.junit.Assert.*;

import org.junit.Test;

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
		assertEquals("instruction based commands", PortionSearchEngine.StringToCanonicalForm(" instruction-based    commands "));
		assertEquals("instruction based commands", PortionSearchEngine.StringToCanonicalForm(" instruction-based    commands(must be removed)"));
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
	

}
