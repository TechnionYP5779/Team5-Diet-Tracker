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



}
