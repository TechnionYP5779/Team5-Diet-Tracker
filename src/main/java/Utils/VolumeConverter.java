/**This interface is used as a converter between different types of 
 * volume units, such as liters, gallons, etc.
 * @author Shaked Sapir
 * @since 2018-12-07*/
package Utils;


public interface VolumeConverter {
	/**
	 * this section handles conversion between same-scale units (e.g. liter-->milliliters).
	 */
	public static final double liters_const = 1000;
	
	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in milliliters if ml >=0 , -1 otherwise
	 */
	static double literToMilliliter(double liters) {
		return liters < 0 ? -1 : liters * liters_const;
	}
	
	/**
	 * @param ml : amount of given item in milliliters
	 * @return amount of same item in liters if ml >=0 , -1 otherwise
	 */
	static double milliliterToLiter(double ml) {
		return ml < 0 ? -1 : ml / liters_const;
	}
	
	
}
