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
public class VolumeConverterTest {

	public static final double DELTA = 0.01;
	public static final double liters_const = 1000;
	
	@Test
	public void literToMl() {
		assertEquals(liters_const,VolumeConverter.literToMilliliter(1),DELTA);
		assertEquals(0., VolumeConverter.literToMilliliter(0),DELTA);
		assertEquals(-1, VolumeConverter.literToMilliliter(-10.2),DELTA);
	}
	
	@Test
	public void mlToLiter() {
		assertEquals(1/liters_const,VolumeConverter.milliliterToLiter(1),DELTA);
		assertEquals(0., VolumeConverter.milliliterToLiter(0),DELTA);
		assertEquals(-1, VolumeConverter.milliliterToLiter(-10.2),DELTA);
	}
	
	@Test
	public void mlToCups() {
		assertEquals(0.0084535097,VolumeConverter.millilitersToCups(2),DELTA);
		assertEquals(0., VolumeConverter.millilitersToCups(0),DELTA);
		assertEquals(-1, VolumeConverter.millilitersToCups(-10.2),DELTA);
	}
	
	@Test
	public void litersToCups() {
		assertEquals(8.4535096595,VolumeConverter.litersToCups(2),DELTA);
		assertEquals(0., VolumeConverter.litersToCups(0),DELTA);
		assertEquals(-1, VolumeConverter.litersToCups(-10.2),DELTA);
	}
	
	@Test
	public void cupsToLiters() {
		assertEquals(0.47317625,VolumeConverter.cupsToLiters(2),DELTA);
		assertEquals(0., VolumeConverter.cupsToLiters(0),DELTA);
		assertEquals(-1, VolumeConverter.cupsToLiters(-10.2),DELTA);
	}
	
	@Test
	public void cupsToMilliliters() {
		assertEquals(473.17625,VolumeConverter.cupsToMilliliters(2),DELTA);
		assertEquals(0., VolumeConverter.cupsToMilliliters(0),DELTA);
		assertEquals(-1, VolumeConverter.cupsToMilliliters(-10.2),DELTA);
	}

	@Test
	public void tablespoonToTeaspoon() {
		assertEquals(6,VolumeConverter.tablespoonsToTeaspoons(2),DELTA);
		assertEquals(0,VolumeConverter.tablespoonsToTeaspoons(0),DELTA);
		assertEquals(-1,VolumeConverter.tablespoonsToTeaspoons(-2),DELTA);
	}
	
	@Test
	public void teaspoonToTablespoon() {
		assertEquals(0.6666666667,VolumeConverter.teaspoonsToTablespoons(2),DELTA);
		assertEquals(0,VolumeConverter.teaspoonsToTablespoons(0),DELTA);
		assertEquals(-1,VolumeConverter.teaspoonsToTablespoons(-2),DELTA);
	}
	
	@Test
	public void tablespoonToLiters() {
		assertEquals(0.0295735156,VolumeConverter.tablespoonsToLiters(2),DELTA);
		assertEquals(0,VolumeConverter.tablespoonsToLiters(0),DELTA);
		assertEquals(-1,VolumeConverter.tablespoonsToLiters(-2),DELTA);
	}
	
	@Test
	public void tablespoonsToMilliliters() {
		assertEquals(29.573515625,VolumeConverter.tablespoonsToMilliliters(2),DELTA);
		assertEquals(0,VolumeConverter.tablespoonsToMilliliters(0),DELTA);
		assertEquals(-1,VolumeConverter.tablespoonsToMilliliters(-2),DELTA);
	}
	
	@Test
	public void litersToTablespoons() {
		assertEquals(135.25615455,VolumeConverter.litersToTablespoons(2),DELTA);
		assertEquals(0,VolumeConverter.litersToTablespoons(0),DELTA);
		assertEquals(-1,VolumeConverter.litersToTablespoons(-2),DELTA);
	}
	
	@Test
	public void millilitersToTablespoons() {
		assertEquals(0.1352561546,VolumeConverter.millilitersToTablespoons(2),DELTA);
		assertEquals(0,VolumeConverter.millilitersToTablespoons(0),DELTA);
		assertEquals(-1,VolumeConverter.millilitersToTablespoons(-2),DELTA);
	}
	
	@Test
	public void teaspoonsToLiters() {
		assertEquals(0.0098578385,VolumeConverter.teaspoonsToLiters(2),DELTA);
		assertEquals(0,VolumeConverter.teaspoonsToLiters(0),DELTA);
		assertEquals(-1,VolumeConverter.teaspoonsToLiters(-2),DELTA);
	}
	
	@Test
	public void teaspoonsToMilliliters() {
		assertEquals(9.8578385417,VolumeConverter.teaspoonsToMilliliters(2),DELTA);
		assertEquals(0,VolumeConverter.teaspoonsToMilliliters(0),DELTA);
		assertEquals(-1,VolumeConverter.teaspoonsToMilliliters(-2),DELTA);
	}
	
	@Test
	public void litersToTeaspoons 	() {
		assertEquals(405.76846365,VolumeConverter.litersToTeaspoons(2),DELTA);
		assertEquals(0,VolumeConverter.litersToTeaspoons(0),DELTA);
		assertEquals(-1,VolumeConverter.litersToTeaspoons(-2),DELTA);
	}
	
	@Test
	public void millilitersToTeaspoons 	() {
		assertEquals(0.4057684637,VolumeConverter.millilitersToTeaspoons(2),DELTA);
		assertEquals(0,VolumeConverter.millilitersToTeaspoons(0),DELTA);
		assertEquals(-1,VolumeConverter.millilitersToTeaspoons(-2),DELTA);
	}
	
	@Test
	public void cupsToTablespoons() {
		assertEquals(32,VolumeConverter.cupsToTablespoons(2),DELTA);
		assertEquals(0., VolumeConverter.cupsToTablespoons(0),DELTA);
		assertEquals(-1, VolumeConverter.cupsToTablespoons(-10.2),DELTA);
	}
	
	@Test
	public void tablespoonsToCups() {
		assertEquals(0.125,VolumeConverter.tablespoonsToCups(2),DELTA);
		assertEquals(0,VolumeConverter.tablespoonsToCups(0),DELTA);
		assertEquals(-1,VolumeConverter.tablespoonsToCups(-2),DELTA);
	}
	
	@Test
	public void teaspoonsToCups() {
		assertEquals(0.0416666667,VolumeConverter.teaspoonsToCups(2),DELTA);
		assertEquals(0,VolumeConverter.teaspoonsToCups(0),DELTA);
		assertEquals(-1,VolumeConverter.teaspoonsToCups(-2),DELTA);
	}

	@Test
	public void cupsToTeaspoons() {
		assertEquals(96,VolumeConverter.cupsToTeaspoons(2),DELTA);
		assertEquals(0., VolumeConverter.cupsToTeaspoons(0),DELTA);
		assertEquals(-1, VolumeConverter.cupsToTeaspoons(-10.2),DELTA);
	}
	
	@Test
	public void litersToGallons() {
		assertEquals(0.5283443537,VolumeConverter.litersToGallons(2),DELTA);
		assertEquals(0,VolumeConverter.litersToGallons(0),DELTA);
		assertEquals(-1,VolumeConverter.litersToGallons(-2),DELTA);
	}
	
	@Test
	public void gallonsToLiters() {
		assertEquals(7.57082,VolumeConverter.gallonsToLiters(2),DELTA);
		assertEquals(0,VolumeConverter.gallonsToLiters(0),DELTA);
		assertEquals(-1,VolumeConverter.gallonsToLiters(-2),DELTA);
	}
	
	@Test
	public void millilitersToGallons() {
		assertEquals(0.0005283444,VolumeConverter.millilitersToGallons(2),DELTA);
		assertEquals(0,VolumeConverter.millilitersToGallons(0),DELTA);
		assertEquals(-1,VolumeConverter.millilitersToGallons(-2),DELTA);
	}
	
	@Test
	public void gallonsToMilliliters() {
		assertEquals(7570.82,VolumeConverter.gallonsToMilliliters(2),DELTA);
		assertEquals(0,VolumeConverter.gallonsToMilliliters(0),DELTA);
		assertEquals(-1,VolumeConverter.gallonsToMilliliters(-2),DELTA);
	}
	
	@Test
	public void litersToPints() {
		assertEquals(4.2267548297,VolumeConverter.litersToPints(2),DELTA);
		assertEquals(0,VolumeConverter.litersToPints(0),DELTA);
		assertEquals(-1,VolumeConverter.litersToPints(-2),DELTA);
	}
	
	@Test
	public void pintsToLiters() {
		assertEquals(0.9463525,VolumeConverter.pintsToLiters(2),DELTA);
		assertEquals(0,VolumeConverter.pintsToLiters(0),DELTA);
		assertEquals(-1,VolumeConverter.pintsToLiters(-2),DELTA);
	}
	
	@Test
	public void pintsToMilliliters() {
		assertEquals(946.3525,VolumeConverter.pintsToMilliliters(2),DELTA);
		assertEquals(0,VolumeConverter.pintsToMilliliters(0),DELTA);
		assertEquals(-1,VolumeConverter.pintsToMilliliters(-2),DELTA);
	}
	
	@Test
	public void millilitersToPints() {
		assertEquals(0.0042267548,VolumeConverter.millilitersToPints(2),DELTA);
		assertEquals(0,VolumeConverter.millilitersToPints(0),DELTA);
		assertEquals(-1,VolumeConverter.millilitersToPints(-2),DELTA);
	}
	
	@Test
	public void pintsToGallons() {
		assertEquals(0.25,VolumeConverter.pintsToGallons(2),DELTA);
		assertEquals(0,VolumeConverter.pintsToGallons(0),DELTA);
		assertEquals(-1,VolumeConverter.pintsToGallons(-2),DELTA);
	}
	
	@Test
	public void gallonsToPints() {
		assertEquals(16,VolumeConverter.gallonsToPints(2),DELTA);
		assertEquals(0,VolumeConverter.gallonsToPints(0),DELTA);
		assertEquals(-1,VolumeConverter.gallonsToPints(-2),DELTA);
	}
	
	@Test
	public void cupsToGallons() {
		assertEquals(0.125,VolumeConverter.cupsToGallons(2),DELTA);
		assertEquals(0,VolumeConverter.cupsToGallons(0),DELTA);
		assertEquals(-1,VolumeConverter.cupsToGallons(-2),DELTA);
	}
	
	@Test
	public void gallonsToCups() {
		assertEquals(32,VolumeConverter.gallonsToCups(2),DELTA);
		assertEquals(0,VolumeConverter.gallonsToCups(0),DELTA);
		assertEquals(-1,VolumeConverter.gallonsToCups(-2),DELTA);
	}
	
	@Test
	public void cupsToPints() {
		assertEquals(1,VolumeConverter.cupsToPints(2),DELTA);
		assertEquals(0,VolumeConverter.cupsToPints(0),DELTA);
		assertEquals(-1,VolumeConverter.cupsToPints(-2),DELTA);
	}
	
	@Test
	public void pintsToCups() {
		assertEquals(4,VolumeConverter.pintsToCups(2),DELTA);
		assertEquals(0,VolumeConverter.pintsToCups(0),DELTA);
		assertEquals(-1,VolumeConverter.pintsToCups(-2),DELTA);
	}
}
