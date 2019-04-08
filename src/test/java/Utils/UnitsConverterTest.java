/**this module is meant to test UnitsConverter module.
 * @author Shaked Sapir
 * @since 2019-01-21*/
package Utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnitsConverterTest {

	public static final double DELTA = 0.01;
	public static final double liters_const = 1000;
	public static final double grams_const = 1000;
	public static final double kilogram_pound_const = 2.2046244202;

	@Test
	public void literToMl() {
		assertEquals(liters_const, UnitsConverter.literToMilliliter(1), DELTA);
		assertEquals(0., UnitsConverter.literToMilliliter(0), DELTA);
		assertEquals(-1, UnitsConverter.literToMilliliter(-10.2), DELTA);
	}

	@Test
	public void mlToLiter() {
		assertEquals(1 / liters_const, UnitsConverter.milliliterToLiter(1), DELTA);
		assertEquals(0., UnitsConverter.milliliterToLiter(0), DELTA);
		assertEquals(-1, UnitsConverter.milliliterToLiter(-10.2), DELTA);
	}

	@Test
	public void mlToCups() {
		assertEquals(0.0084535097, UnitsConverter.millilitersToCups(2), DELTA);
		assertEquals(0., UnitsConverter.millilitersToCups(0), DELTA);
		assertEquals(-1, UnitsConverter.millilitersToCups(-10.2), DELTA);
	}

	@Test
	public void litersToCups() {
		assertEquals(8.4535096595, UnitsConverter.litersToCups(2), DELTA);
		assertEquals(0., UnitsConverter.litersToCups(0), DELTA);
		assertEquals(-1, UnitsConverter.litersToCups(-10.2), DELTA);
	}

	@Test
	public void cupsToLiters() {
		assertEquals(0.47317625, UnitsConverter.cupsToLiters(2), DELTA);
		assertEquals(0., UnitsConverter.cupsToLiters(0), DELTA);
		assertEquals(-1, UnitsConverter.cupsToLiters(-10.2), DELTA);
	}

	@Test
	public void cupsToMilliliters() {
		assertEquals(473.17625, UnitsConverter.cupsToMilliliters(2), DELTA);
		assertEquals(0., UnitsConverter.cupsToMilliliters(0), DELTA);
		assertEquals(-1, UnitsConverter.cupsToMilliliters(-10.2), DELTA);
	}

	@Test
	public void tablespoonToTeaspoon() {
		assertEquals(6, UnitsConverter.tablespoonsToTeaspoons(2), DELTA);
		assertEquals(0, UnitsConverter.tablespoonsToTeaspoons(0), DELTA);
		assertEquals(-1, UnitsConverter.tablespoonsToTeaspoons(-2), DELTA);
	}

	@Test
	public void teaspoonToTablespoon() {
		assertEquals(0.6666666667, UnitsConverter.teaspoonsToTablespoons(2), DELTA);
		assertEquals(0, UnitsConverter.teaspoonsToTablespoons(0), DELTA);
		assertEquals(-1, UnitsConverter.teaspoonsToTablespoons(-2), DELTA);
	}

	@Test
	public void tablespoonToLiters() {
		assertEquals(0.0295735156, UnitsConverter.tablespoonsToLiters(2), DELTA);
		assertEquals(0, UnitsConverter.tablespoonsToLiters(0), DELTA);
		assertEquals(-1, UnitsConverter.tablespoonsToLiters(-2), DELTA);
	}

	@Test
	public void tablespoonsToMilliliters() {
		assertEquals(29.573515625, UnitsConverter.tablespoonsToMilliliters(2), DELTA);
		assertEquals(0, UnitsConverter.tablespoonsToMilliliters(0), DELTA);
		assertEquals(-1, UnitsConverter.tablespoonsToMilliliters(-2), DELTA);
	}

	@Test
	public void litersToTablespoons() {
		assertEquals(135.25615455, UnitsConverter.litersToTablespoons(2), DELTA);
		assertEquals(0, UnitsConverter.litersToTablespoons(0), DELTA);
		assertEquals(-1, UnitsConverter.litersToTablespoons(-2), DELTA);
	}

	@Test
	public void millilitersToTablespoons() {
		assertEquals(0.1352561546, UnitsConverter.millilitersToTablespoons(2), DELTA);
		assertEquals(0, UnitsConverter.millilitersToTablespoons(0), DELTA);
		assertEquals(-1, UnitsConverter.millilitersToTablespoons(-2), DELTA);
	}

	@Test
	public void teaspoonsToLiters() {
		assertEquals(0.0098578385, UnitsConverter.teaspoonsToLiters(2), DELTA);
		assertEquals(0, UnitsConverter.teaspoonsToLiters(0), DELTA);
		assertEquals(-1, UnitsConverter.teaspoonsToLiters(-2), DELTA);
	}

	@Test
	public void teaspoonsToMilliliters() {
		assertEquals(9.8578385417, UnitsConverter.teaspoonsToMilliliters(2), DELTA);
		assertEquals(0, UnitsConverter.teaspoonsToMilliliters(0), DELTA);
		assertEquals(-1, UnitsConverter.teaspoonsToMilliliters(-2), DELTA);
	}

	@Test
	public void litersToTeaspoons() {
		assertEquals(405.76846365, UnitsConverter.litersToTeaspoons(2), DELTA);
		assertEquals(0, UnitsConverter.litersToTeaspoons(0), DELTA);
		assertEquals(-1, UnitsConverter.litersToTeaspoons(-2), DELTA);
	}

	@Test
	public void millilitersToTeaspoons() {
		assertEquals(0.4057684637, UnitsConverter.millilitersToTeaspoons(2), DELTA);
		assertEquals(0, UnitsConverter.millilitersToTeaspoons(0), DELTA);
		assertEquals(-1, UnitsConverter.millilitersToTeaspoons(-2), DELTA);
	}

	@Test
	public void cupsToTablespoons() {
		assertEquals(32, UnitsConverter.cupsToTablespoons(2), DELTA);
		assertEquals(0., UnitsConverter.cupsToTablespoons(0), DELTA);
		assertEquals(-1, UnitsConverter.cupsToTablespoons(-10.2), DELTA);
	}

	@Test
	public void tablespoonsToCups() {
		assertEquals(0.125, UnitsConverter.tablespoonsToCups(2), DELTA);
		assertEquals(0, UnitsConverter.tablespoonsToCups(0), DELTA);
		assertEquals(-1, UnitsConverter.tablespoonsToCups(-2), DELTA);
	}

	@Test
	public void teaspoonsToCups() {
		assertEquals(0.0416666667, UnitsConverter.teaspoonsToCups(2), DELTA);
		assertEquals(0, UnitsConverter.teaspoonsToCups(0), DELTA);
		assertEquals(-1, UnitsConverter.teaspoonsToCups(-2), DELTA);
	}

	@Test
	public void cupsToTeaspoons() {
		assertEquals(96, UnitsConverter.cupsToTeaspoons(2), DELTA);
		assertEquals(0., UnitsConverter.cupsToTeaspoons(0), DELTA);
		assertEquals(-1, UnitsConverter.cupsToTeaspoons(-10.2), DELTA);
	}

	@Test
	public void litersToGallons() {
		assertEquals(0.5283443537, UnitsConverter.litersToGallons(2), DELTA);
		assertEquals(0, UnitsConverter.litersToGallons(0), DELTA);
		assertEquals(-1, UnitsConverter.litersToGallons(-2), DELTA);
	}

	@Test
	public void gallonsToLiters() {
		assertEquals(7.57082, UnitsConverter.gallonsToLiters(2), DELTA);
		assertEquals(0, UnitsConverter.gallonsToLiters(0), DELTA);
		assertEquals(-1, UnitsConverter.gallonsToLiters(-2), DELTA);
	}

	@Test
	public void millilitersToGallons() {
		assertEquals(0.0005283444, UnitsConverter.millilitersToGallons(2), DELTA);
		assertEquals(0, UnitsConverter.millilitersToGallons(0), DELTA);
		assertEquals(-1, UnitsConverter.millilitersToGallons(-2), DELTA);
	}

	@Test
	public void gallonsToMilliliters() {
		assertEquals(7570.82, UnitsConverter.gallonsToMilliliters(2), DELTA);
		assertEquals(0, UnitsConverter.gallonsToMilliliters(0), DELTA);
		assertEquals(-1, UnitsConverter.gallonsToMilliliters(-2), DELTA);
	}

	@Test
	public void litersToPints() {
		assertEquals(4.2267548297, UnitsConverter.litersToPints(2), DELTA);
		assertEquals(0, UnitsConverter.litersToPints(0), DELTA);
		assertEquals(-1, UnitsConverter.litersToPints(-2), DELTA);
	}

	@Test
	public void pintsToLiters() {
		assertEquals(0.9463525, UnitsConverter.pintsToLiters(2), DELTA);
		assertEquals(0, UnitsConverter.pintsToLiters(0), DELTA);
		assertEquals(-1, UnitsConverter.pintsToLiters(-2), DELTA);
	}

	@Test
	public void pintsToMilliliters() {
		assertEquals(946.3525, UnitsConverter.pintsToMilliliters(2), DELTA);
		assertEquals(0, UnitsConverter.pintsToMilliliters(0), DELTA);
		assertEquals(-1, UnitsConverter.pintsToMilliliters(-2), DELTA);
	}

	@Test
	public void millilitersToPints() {
		assertEquals(0.0042267548, UnitsConverter.millilitersToPints(2), DELTA);
		assertEquals(0, UnitsConverter.millilitersToPints(0), DELTA);
		assertEquals(-1, UnitsConverter.millilitersToPints(-2), DELTA);
	}

	@Test
	public void pintsToGallons() {
		assertEquals(0.25, UnitsConverter.pintsToGallons(2), DELTA);
		assertEquals(0, UnitsConverter.pintsToGallons(0), DELTA);
		assertEquals(-1, UnitsConverter.pintsToGallons(-2), DELTA);
	}

	@Test
	public void gallonsToPints() {
		assertEquals(16, UnitsConverter.gallonsToPints(2), DELTA);
		assertEquals(0, UnitsConverter.gallonsToPints(0), DELTA);
		assertEquals(-1, UnitsConverter.gallonsToPints(-2), DELTA);
	}

	@Test
	public void cupsToGallons() {
		assertEquals(0.125, UnitsConverter.cupsToGallons(2), DELTA);
		assertEquals(0, UnitsConverter.cupsToGallons(0), DELTA);
		assertEquals(-1, UnitsConverter.cupsToGallons(-2), DELTA);
	}

	@Test
	public void gallonsToCups() {
		assertEquals(32, UnitsConverter.gallonsToCups(2), DELTA);
		assertEquals(0, UnitsConverter.gallonsToCups(0), DELTA);
		assertEquals(-1, UnitsConverter.gallonsToCups(-2), DELTA);
	}

	@Test
	public void cupsToPints() {
		assertEquals(1, UnitsConverter.cupsToPints(2), DELTA);
		assertEquals(0, UnitsConverter.cupsToPints(0), DELTA);
		assertEquals(-1, UnitsConverter.cupsToPints(-2), DELTA);
	}

	@Test
	public void pintsToCups() {
		assertEquals(4, UnitsConverter.pintsToCups(2), DELTA);
		assertEquals(0, UnitsConverter.pintsToCups(0), DELTA);
		assertEquals(-1, UnitsConverter.pintsToCups(-2), DELTA);
	}

	@Test
	public void grToMg() {
		assertEquals(grams_const, UnitsConverter.gramsToMilligrams(1), DELTA);
		assertEquals(0., UnitsConverter.gramsToMilligrams(0), DELTA);
		assertEquals(-1, UnitsConverter.gramsToMilligrams(-10.2), DELTA);
	}

	@Test
	public void grToKg() {
		assertEquals(1 / grams_const, UnitsConverter.gramsToKilograms(1), DELTA);
		assertEquals(0., UnitsConverter.gramsToKilograms(0), DELTA);
		assertEquals(-1, UnitsConverter.gramsToKilograms(-10.2), DELTA);
	}

	@Test
	public void kgToMg() {
		assertEquals(Math.pow(grams_const, 2), UnitsConverter.kilogramsToMilligrams(1), DELTA);
		assertEquals(0., UnitsConverter.kilogramsToMilligrams(0), DELTA);
		assertEquals(-1, UnitsConverter.kilogramsToMilligrams(-10.2), DELTA);
	}

	@Test
	public void kgTogr() {
		assertEquals(grams_const, UnitsConverter.kilogramsToGrams(1), DELTA);
		assertEquals(0., UnitsConverter.kilogramsToGrams(0), DELTA);
		assertEquals(-1, UnitsConverter.kilogramsToGrams(-10.2), DELTA);
	}

	@Test
	public void mgToGr() {
		assertEquals(1 / grams_const, UnitsConverter.milligramsToGrams(1), DELTA);
		assertEquals(0., UnitsConverter.milligramsToGrams(0), DELTA);
		assertEquals(-1, UnitsConverter.milligramsToGrams(-10.2), DELTA);
	}

	@Test
	public void mgToKg() {
		assertEquals(Math.pow(1 / grams_const, 2), UnitsConverter.milligramsToKilograms(1), DELTA);
		assertEquals(0., UnitsConverter.milligramsToKilograms(0), DELTA);
		assertEquals(-1, UnitsConverter.milligramsToKilograms(-10.2), DELTA);
	}

	@Test
	public void gramsToPounds() {
		assertEquals(0.0044092488, UnitsConverter.gramsToPounds(2), DELTA);
		assertEquals(0., UnitsConverter.gramsToPounds(0), DELTA);
		assertEquals(-1, UnitsConverter.gramsToPounds(-10.2), DELTA);
	}

	@Test
	public void kilogramsToPounds() {
		assertEquals(4.4092488404, UnitsConverter.kilogramsToPounds(2), DELTA);
		assertEquals(0., UnitsConverter.kilogramsToPounds(0), DELTA);
		assertEquals(-1, UnitsConverter.kilogramsToPounds(-10.2), DELTA);
	}

	@Test
	public void milligramsToPounds() {
		assertEquals(0.0000044092, UnitsConverter.milligramsToPounds(2), DELTA);
		assertEquals(0., UnitsConverter.milligramsToPounds(0), DELTA);
		assertEquals(-1, UnitsConverter.milligramsToPounds(-10.2), DELTA);
	}

	@Test
	public void poundsToGrams() {
		assertEquals(907.184, UnitsConverter.poundsToGrams(2), DELTA);
		assertEquals(0., UnitsConverter.poundsToGrams(0), DELTA);
		assertEquals(-1, UnitsConverter.poundsToGrams(-10.2), DELTA);
	}

	@Test
	public void poundsToKilograms() {
		assertEquals(0.907184, UnitsConverter.poundsToKilograms(2), DELTA);
		assertEquals(0., UnitsConverter.poundsToKilograms(0), DELTA);
		assertEquals(-1, UnitsConverter.poundsToKilograms(-10.2), DELTA);
	}

	@Test
	public void poundsToMilligrams() {
		assertEquals(907184, UnitsConverter.poundsToMilligrams(2), DELTA);
		assertEquals(0., UnitsConverter.poundsToMilligrams(0), DELTA);
		assertEquals(-1, UnitsConverter.poundsToMilligrams(-10.2), DELTA);
	}

	@Test
	public void milligramsToOunces() {
		assertEquals(0.000070548, UnitsConverter.milligramsToOunces(2), DELTA);
		assertEquals(0., UnitsConverter.milligramsToOunces(0), DELTA);
		assertEquals(-1, UnitsConverter.milligramsToOunces(-10.2), DELTA);
	}

	@Test
	public void gramsToOunces() {
		assertEquals(0.0705479814, UnitsConverter.gramsToOunces(2), DELTA);
		assertEquals(0., UnitsConverter.gramsToOunces(0), DELTA);
		assertEquals(-1, UnitsConverter.gramsToOunces(-10.2), DELTA);
	}

	@Test
	public void kilogramsToOunces() {
		assertEquals(70.547981446, UnitsConverter.kilogramsToOunces(2), DELTA);
		assertEquals(0., UnitsConverter.kilogramsToOunces(0), DELTA);
		assertEquals(-1, UnitsConverter.kilogramsToOunces(-10.2), DELTA);
	}

	@Test
	public void poundsToOunces() {
		assertEquals(32, UnitsConverter.poundsToOunces(2), DELTA);
		assertEquals(0., UnitsConverter.poundsToOunces(0), DELTA);
		assertEquals(-1, UnitsConverter.poundsToOunces(-10.2), DELTA);
	}

	@Test
	public void OuncesToPounds() {
		assertEquals(0.125, UnitsConverter.ouncesToPounds(2), DELTA);
		assertEquals(0., UnitsConverter.ouncesToPounds(0), DELTA);
		assertEquals(-1, UnitsConverter.ouncesToPounds(-10.2), DELTA);
	}

	@Test
	public void OuncesToKilograms() {
		assertEquals(0.056699, UnitsConverter.ouncesToKilograms(2), DELTA);
		assertEquals(0., UnitsConverter.ouncesToKilograms(0), DELTA);
		assertEquals(-1, UnitsConverter.ouncesToKilograms(-10.2), DELTA);
	}

	@Test
	public void OuncesToMilligrams() {
		assertEquals(56699, UnitsConverter.ouncesToMilligrams(2), DELTA);
		assertEquals(0., UnitsConverter.ouncesToMilligrams(0), DELTA);
		assertEquals(-1, UnitsConverter.ouncesToMilligrams(-10.2), DELTA);
	}

	@Test
	public void OuncesToGrams() {
		assertEquals(56.699, UnitsConverter.ouncesToGrams(2), DELTA);
		assertEquals(0., UnitsConverter.ouncesToGrams(0), DELTA);
		assertEquals(-1, UnitsConverter.ouncesToGrams(-10.2), DELTA);
	}
}
