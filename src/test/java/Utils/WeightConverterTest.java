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
	public static final double kilogram_pound_const = 2.2046244202;

	@Test
	public void grToMg() {
		assertEquals(grams_const,WeightConverter.gramsToMilligrams(1),DELTA);
		assertEquals(0., WeightConverter.gramsToMilligrams(0),DELTA);
		assertEquals(-1, WeightConverter.gramsToMilligrams(-10.2),DELTA);
	}
	
	@Test
	public void grToKg() {
		assertEquals(1/grams_const,WeightConverter.gramsToKilograms(1),DELTA);
		assertEquals(0., WeightConverter.gramsToKilograms(0),DELTA);
		assertEquals(-1, WeightConverter.gramsToKilograms(-10.2),DELTA);
	}
	
	@Test
	public void kgToMg() {
		assertEquals(Math.pow(grams_const,2),WeightConverter.kilogramsToMilligrams(1),DELTA);
		assertEquals(0., WeightConverter.kilogramsToMilligrams(0),DELTA);
		assertEquals(-1, WeightConverter.kilogramsToMilligrams(-10.2),DELTA);
	}
	
	@Test
	public void kgTogr() {
		assertEquals(grams_const,WeightConverter.kilogramsToGrams(1),DELTA);
		assertEquals(0., WeightConverter.kilogramsToGrams(0),DELTA);
		assertEquals(-1, WeightConverter.kilogramsToGrams(-10.2),DELTA);
	}
	
	@Test
	public void mgToGr() {
		assertEquals(1/grams_const,WeightConverter.milligramsToGrams(1),DELTA);
		assertEquals(0., WeightConverter.milligramsToGrams(0),DELTA);
		assertEquals(-1, WeightConverter.milligramsToGrams(-10.2),DELTA);
	}
	
	@Test
	public void mgToKg() {
		assertEquals(Math.pow(1/grams_const,2),WeightConverter.milligramsToKilograms(1),DELTA);
		assertEquals(0., WeightConverter.milligramsToKilograms(0),DELTA);
		assertEquals(-1, WeightConverter.milligramsToKilograms(-10.2),DELTA);
	}
	
	@Test
	public void gramsToPounds() {
		assertEquals(0.0044092488,WeightConverter.gramsToPounds(2),DELTA);
		assertEquals(0., WeightConverter.gramsToPounds(0),DELTA);
		assertEquals(-1, WeightConverter.gramsToPounds(-10.2),DELTA);
	}
	
	@Test
	public void kilogramsToPounds() {
		assertEquals(4.4092488404,WeightConverter.kilogramsToPounds(2),DELTA);
		assertEquals(0., WeightConverter.kilogramsToPounds(0),DELTA);
		assertEquals(-1, WeightConverter.kilogramsToPounds(-10.2),DELTA);
	}
	
	@Test
	public void milligramsToPounds() {
		assertEquals(0.0000044092,WeightConverter.milligramsToPounds(2),DELTA);
		assertEquals(0., WeightConverter.milligramsToPounds(0),DELTA);
		assertEquals(-1, WeightConverter.milligramsToPounds(-10.2),DELTA);
	}
	
	@Test
	public void poundsToGrams() {
		assertEquals(907.184,WeightConverter.poundsToGrams(2),DELTA);
		assertEquals(0., WeightConverter.poundsToGrams(0),DELTA);
		assertEquals(-1, WeightConverter.poundsToGrams(-10.2),DELTA);
	}
	
	@Test
	public void poundsToKilograms() {
		assertEquals(0.907184,WeightConverter.poundsToKilograms(2),DELTA);
		assertEquals(0., WeightConverter.poundsToKilograms(0),DELTA);
		assertEquals(-1, WeightConverter.poundsToKilograms(-10.2),DELTA);
	}
	
	@Test
	public void poundsToMilligrams() {
		assertEquals(907184,WeightConverter.poundsToMilligrams(2),DELTA);
		assertEquals(0., WeightConverter.poundsToMilligrams(0),DELTA);
		assertEquals(-1, WeightConverter.poundsToMilligrams(-10.2),DELTA);
	}
	
	@Test
	public void milligramsToOunces() {
		assertEquals(0.000070548,WeightConverter.milligramsToOunces(2),DELTA);
		assertEquals(0., WeightConverter.milligramsToOunces(0),DELTA);
		assertEquals(-1, WeightConverter.milligramsToOunces(-10.2),DELTA);
	}
	
	@Test
	public void gramsToOunces() {
		assertEquals(0.0705479814,WeightConverter.gramsToOunces(2),DELTA);
		assertEquals(0., WeightConverter.gramsToOunces(0),DELTA);
		assertEquals(-1, WeightConverter.gramsToOunces(-10.2),DELTA);
	}
	
	@Test
	public void kilogramsToOunces() {
		assertEquals(70.547981446,WeightConverter.kilogramsToOunces(2),DELTA);
		assertEquals(0., WeightConverter.kilogramsToOunces(0),DELTA);
		assertEquals(-1, WeightConverter.kilogramsToOunces(-10.2),DELTA);
	}
	
	@Test
	public void poundsToOunces() {
		assertEquals(32,WeightConverter.poundsToOunces(2),DELTA);
		assertEquals(0., WeightConverter.poundsToOunces(0),DELTA);
		assertEquals(-1, WeightConverter.poundsToOunces(-10.2),DELTA);
	}
	
	@Test
	public void OuncesToPounds() {
		assertEquals(0.125,WeightConverter.ouncesToPounds(2),DELTA);
		assertEquals(0., WeightConverter.ouncesToPounds(0),DELTA);
		assertEquals(-1, WeightConverter.ouncesToPounds(-10.2),DELTA);
	}
	
	@Test
	public void OuncesToKilograms() {
		assertEquals(0.056699,WeightConverter.ouncesToKilograms(2),DELTA);
		assertEquals(0., WeightConverter.ouncesToKilograms(0),DELTA);
		assertEquals(-1, WeightConverter.ouncesToKilograms(-10.2),DELTA);
	}
	
	@Test
	public void OuncesToMilligrams() {
		assertEquals(56699,WeightConverter.ouncesToMilligrams(2),DELTA);
		assertEquals(0., WeightConverter.ouncesToMilligrams(0),DELTA);
		assertEquals(-1, WeightConverter.ouncesToMilligrams(-10.2),DELTA);
	}
	
	@Test
	public void OuncesToGrams() {
		assertEquals(56.699,WeightConverter.ouncesTograms(2),DELTA);
		assertEquals(0., WeightConverter.ouncesTograms(0),DELTA);
		assertEquals(-1, WeightConverter.ouncesTograms(-10.2),DELTA);
	}
}
