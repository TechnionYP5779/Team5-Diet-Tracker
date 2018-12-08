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

}
