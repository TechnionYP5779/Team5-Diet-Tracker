/**add here document
 * @author Fname Sname
 * @since year-month-day*/
package Utils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Shaked Sapir
 *
 */
public class WeightConverterTest {

	public static final double DELTA = 0.01;
	public static final double grams_const = 1000;
	
	@Test
	public void grToMg() {
		assertEquals(grams_const,WeightConverter.grToMg(1),DELTA);
		assertEquals(0., WeightConverter.mgToGr(0),DELTA);
		assertEquals(-1, WeightConverter.mgToGr(-10.2),DELTA);
	}
	
	@Test
	public void grToKg() {
		assertEquals(1/grams_const,WeightConverter.grToKg(1),DELTA);
		assertEquals(0., WeightConverter.mgToGr(0),DELTA);
		assertEquals(-1, WeightConverter.mgToGr(-10.2),DELTA);
	}
	
	@Test
	public void kgToMg() {
		assertEquals(Math.pow(grams_const,2),WeightConverter.kgToMg(1),DELTA);
		assertEquals(0., WeightConverter.mgToGr(0),DELTA);
		assertEquals(-1, WeightConverter.mgToGr(-10.2),DELTA);
	}
	
	@Test
	public void kgTogr() {
		assertEquals(grams_const,WeightConverter.kgToGr(1),DELTA);
		assertEquals(0., WeightConverter.mgToGr(0),DELTA);
		assertEquals(-1, WeightConverter.mgToGr(-10.2),DELTA);
	}
	
	@Test
	public void mgToGr() {
		assertEquals(1/grams_const,WeightConverter.mgToGr(1),DELTA);
		assertEquals(0., WeightConverter.mgToGr(0),DELTA);
		assertEquals(-1, WeightConverter.mgToGr(-10.2),DELTA);
	}
	
	@Test
	public void mgToKg() {
		assertEquals(Math.pow(1/grams_const,2),WeightConverter.mgToKg(1),DELTA);
		assertEquals(0., WeightConverter.mgToKg(0),DELTA);
		assertEquals(-1, WeightConverter.mgToKg(-10.2),DELTA);
	}
}
