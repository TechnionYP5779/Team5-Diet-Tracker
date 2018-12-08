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
	
	/********************************************************************/
	
	/**
	 * this section handles conversion between different-scales units (e.g. liters-->cups).
	 */
	public static final double liter_cup_const = 4.2267548297;
	
	/**
	 * @param cups : amount of given item in cups
	 * @return amount of same item in liters if cups >=0 , -1 otherwise
	 */
	static double cupsToLiters(double cups) {
		return cups < 0 ? -1 : cups / liter_cup_const;
	}
	
	/**
	 * @param cups : amount of given item in cups
	 * @return amount of same item in milliliters if cups >=0 , -1 otherwise
	 */
	static double cupsToMilliliters(double cups) {
		return cups < 0 ? -1 : liters_const * cupsToLiters(cups);
	}
	
	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in cups if liters >=0 , -1 otherwise
	 */
	static double litersToCups(double liters) {
		return liters < 0 ? -1 : liters * liter_cup_const;
	}
	
	/**
	 * @param ml : amount of given item in milliliters
	 * @return amount of same item in cups if ml >=0 , -1 otherwise
	 */
	static double millilitersToCups(double ml) {
		return ml < 0 ? -1 : litersToCups(ml) / liters_const;
	}
}
