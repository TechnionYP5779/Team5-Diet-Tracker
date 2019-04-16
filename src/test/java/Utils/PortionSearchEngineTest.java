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

}
