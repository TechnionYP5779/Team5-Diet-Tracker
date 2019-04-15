package Utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.amazon.ask.model.services.Pair;

import Utils.Portion.Type;
import Utils.PortionSearchEngine.SearchResults;

public class PortionSearchEngineTest {

	@Test
	public void test() {
		Pair<SearchResults, Portion> p=PortionSearchEngine.PortionSearch("coca cola", "can",Type.DRINK, 2);
		System.out.println("status:" + p.getName().toString());
		System.out.println("portion:" + p.getValue().toString());
	}

}
