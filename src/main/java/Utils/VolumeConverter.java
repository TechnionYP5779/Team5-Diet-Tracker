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
	public static final double cup_tablespoon_const = 16.;
	public static final double cup_teaspoon_const = 48.;
	public static final double gallon_cup_const = 16.;
	public static final double gallon_liter_const = 3.78541;
	public static final double gallon_pint_const = 8.;
	public static final double tablespoon_teaspoon_const = 3.;
	public static final double liter_cup_const = 4.2267548297;
	public static final double liter_pint_const = 2.1133774149;
	public static final double liter_tablespoon_const = 67.628077276;
	public static final double liter_teaspoon_const = 202.88423183;
	public static final double pint_cup_const = 2.;
	
	/**
	 * @param cups : amount of given item in cups
	 * @return amount of same item in gallons if cups >=0 , -1 otherwise
	 */
	static double cupsToGallons(double cups) {
		return cups < 0 ? -1 : cups / gallon_cup_const;
	}
	
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
	 * @param cups : amount of given item in cups
	 * @return amount of same item in pints if cups >=0 , -1 otherwise
	 */
	static double cupsToPints(double cups) {
		return cups < 0 ? -1 : cups / pint_cup_const;
	}
	
	/**
	 * @param cups : amount of given item in cups
	 * @return amount of same item in tablespoons if cups >=0 , -1 otherwise
	 */
	static double cupsToTablespoons(double cups) {
		return cups < 0 ? -1 : cups * cup_tablespoon_const;
	}
	
	/**
	 * @param cups : amount of given item in cups
	 * @return amount of same item in teaspoons if cups >=0 , -1 otherwise
	 */
	static double cupsToTeaspoons(double cups) {
		return cups < 0 ? -1 : cups * cup_teaspoon_const;
	}
	
	/**
	 * @param gallons : amount of given item in gallons
	 * @return amount of same item in cups if gallons >=0 , -1 otherwise
	 */
	static double gallonsToCups(double gallons) {
		return gallons < 0 ? -1 : gallons * gallon_cup_const;
	}
	
	/**
	 * @param gallons : amount of given item in gallons
	 * @return amount of same item in liters if gallons >=0 , -1 otherwise
	 */
	static double gallonsToLiters(double gallons) {
		return gallons < 0 ? -1 : gallons * gallon_liter_const;
	}
	
	/**
	 * @param gallons : amount of given item in gallons
	 * @return amount of same item in milliliters if gallons >=0 , -1 otherwise
	 */
	static double gallonsToMilliliters(double gallons) {
		return gallons < 0 ? -1 : liters_const * gallonsToLiters(gallons);
	}
	
	/**
	 * @param gallons : amount of given item in gallons
	 * @return amount of same item in pints if gallons >=0 , -1 otherwise
	 */
	static double gallonsToPints(double gallons) {
		return gallons < 0 ? -1 : gallons * gallon_pint_const;
	}
	
	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in cups if liters >=0 , -1 otherwise
	 */
	static double litersToCups(double liters) {
		return liters < 0 ? -1 : liters * liter_cup_const;
	}
	
	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in gallons if liters >=0 , -1 otherwise
	 */
	static double litersToGallons(double liters) {
		return liters < 0 ? -1 : liters / gallon_liter_const;
	}

	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in pints if liters >=0 , -1 otherwise
	 */
	static double litersToPints(double liters) {
		return liters < 0 ? -1 : liters * liter_pint_const;
	}
	
	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in tablespoons if liters >=0 , -1 otherwise
	 */
	static double litersToTablespoons(double liters) {
		return liters < 0 ? -1 : liters * liter_tablespoon_const;
	}
	
	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in teaspoons if liters >=0 , -1 otherwise
	 */
	static double litersToTeaspoons(double liters) {
		return liters < 0 ? -1 : liters * liter_teaspoon_const;
	}
	
	/**
	 * @param ml : amount of given item in milliliters
	 * @return amount of same item in cups if ml >=0 , -1 otherwise
	 */
	static double millilitersToCups(double ml) {
		return ml < 0 ? -1 : litersToCups(ml) / liters_const;
	}
	
	/**
	 * @param milliliters : amount of given item in milliliters
	 * @return amount of same item in gallons if milliliters >=0 , -1 otherwise
	 */
	static double millilitersToGallons(double milliliters) {
		return milliliters < 0 ? -1 : litersToGallons(milliliters) / liters_const;
	}
	
	/**
	 * @param milliliters : amount of given item in milliliters
	 * @return amount of same item in pints if milliliters >=0 , -1 otherwise
	 */
	static double millilitersToPints(double milliliters) {
		return milliliters < 0 ? -1 : litersToPints(milliliters) / liters_const;
	}
	
	/**
	 * @param ml : amount of given item in milliliters
	 * @return amount of same item in tablespoons if ml >=0 , -1 otherwise
	 */
	static double millilitersToTablespoons(double ml) {
		return ml < 0 ? -1 : litersToTablespoons(ml) / liters_const;
	}
	
	/**
	 * @param ml : amount of given item in milliliters
	 * @return amount of same item in teaspoons if ml >=0 , -1 otherwise
	 */
	static double millilitersToTeaspoons(double ml) {
		return ml < 0 ? -1 : litersToTeaspoons(ml) / liters_const;
	}
	
	/**
	 * @param pints : amount of given item in pints
	 * @return amount of same item in cups if pints >=0 , -1 otherwise
	 */
	static double pintsToCups(double pints) {
		return pints < 0 ? -1 : pints * pint_cup_const;
	}
	
	/**
	 * @param pints : amount of given item in pints
	 * @return amount of same item in gallons if pints >=0 , -1 otherwise
	 */
	static double pintsToGallons(double pints) {
		return pints < 0 ? -1 : pints / gallon_pint_const;
	}
	
	/**
	 * @param pints : amount of given item in pints
	 * @return amount of same item in liters if pints >=0 , -1 otherwise
	 */
	static double pintsToLiters(double pints) {
		return pints < 0 ? -1 : pints / liter_pint_const;
	}
	
	/**
	 * @param pints : amount of given item in pints
	 * @return amount of same item in milliliters if pints >=0 , -1 otherwise
	 */
	static double pintsToMilliliters(double pints) {
		return pints < 0 ? -1 : liters_const * pintsToLiters(pints);
	}
	
	/**
	 * @param t : amount of given item in tablespoons
	 * @return amount of same item in cups if t >=0 , -1 otherwise
	 */
	static double tablespoonsToCups(double t) {
		return t < 0 ? -1 : t / cup_tablespoon_const;
	}
	
	/**
	 * @param t : amount of given item in tablespoons
	 * @return amount of same item in liters if t >=0 , -1 otherwise
	 */
	static double tablespoonsToLiters(double t) {
		return t < 0 ? -1 : t / liter_tablespoon_const;
	}
	
	/**
	 * @param t : amount of given item in tablespoons
	 * @return amount of same item in milliliters if t >=0 , -1 otherwise
	 */
	static double tablespoonsToMilliliters(double t) {
		return t < 0 ? -1 : liters_const * tablespoonsToLiters(t);
	}
	
	/**
	 * @param t : amount of given item in tablespoons
	 * @return amount of same item in teaspoons if t >=0 , -1 otherwise
	 */
	static double tablespoonsToTeaspoons(double t) {
		return t < 0 ? -1 : t * tablespoon_teaspoon_const;
	}
	
	/**
	 * @param t : amount of given item in teaspoons
	 * @return amount of same item in cups if t >=0 , -1 otherwise
	 */
	static double teaspoonsToCups(double t) {
		return t < 0 ? -1 : t / cup_teaspoon_const;
	}
	
	/**
	 * @param t : amount of given item in teaspoons
	 * @return amount of same item in liters if t >=0 , -1 otherwise
	 */
	static double teaspoonsToLiters(double t) {
		return t < 0 ? -1 : t / liter_teaspoon_const;
	}
	
	/**
	 * @param t : amount of given item in teaspoons
	 * @return amount of same item in milliliters if t >=0 , -1 otherwise
	 */
	static double teaspoonsToMilliliters(double t) {
		return t < 0 ? -1 : liters_const * teaspoonsToLiters(t);
	}
	
	/**
	 * @param t : amount of given item in teaspoons
	 * @return amount of same item in tablespoons if t >=0 , -1 otherwise
	 */
	static double teaspoonsToTablespoons(double t) {
		return t < 0 ? -1 : t / tablespoon_teaspoon_const;
	}
}
