/**This interface is used as a converter between different types of
 * volume units, such as liters, gallons, etc.
 * @author Shaked Sapir
 * @since 2018-12-07*/
package Utils;

public interface VolumeConverter {
	/**
	 * this section handles conversion between same-scale units (e.g.
	 * liter-->milliliters).
	 */
	double liters_const = 1000;

	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in milliliters if ml >=0 , -1 otherwise
	 */
	static double literToMilliliter(final double liters) {
		return liters < 0 ? -1 : liters * liters_const;
	}

	/**
	 * @param ml : amount of given item in milliliters
	 * @return amount of same item in liters if ml >=0 , -1 otherwise
	 */
	static double milliliterToLiter(final double ml) {
		return ml < 0 ? -1 : ml / liters_const;
	}

	/********************************************************************/

	/**
	 * this section handles conversion between different-scales units (e.g.
	 * liters-->cups).
	 */
	double cup_tablespoon_const = 16.;
	double cup_teaspoon_const = 48.;
	double gallon_cup_const = 16.;
	double gallon_liter_const = 3.78541;
	double gallon_pint_const = 8.;
	double tablespoon_teaspoon_const = 3.;
	double liter_cup_const = 4.2267548297;
	double liter_pint_const = 2.1133774149;
	double liter_tablespoon_const = 67.628077276;
	double liter_teaspoon_const = 202.88423183;
	double pint_cup_const = 2.;

	/**
	 * @param cups : amount of given item in cups
	 * @return amount of same item in gallons if cups >=0 , -1 otherwise
	 */
	static double cupsToGallons(final double cups) {
		return cups < 0 ? -1 : cups / gallon_cup_const;
	}

	/**
	 * @param cups : amount of given item in cups
	 * @return amount of same item in liters if cups >=0 , -1 otherwise
	 */
	static double cupsToLiters(final double cups) {
		return cups < 0 ? -1 : cups / liter_cup_const;
	}

	/**
	 * @param cups : amount of given item in cups
	 * @return amount of same item in milliliters if cups >=0 , -1 otherwise
	 */
	static double cupsToMilliliters(final double cups) {
		return cups < 0 ? -1 : liters_const * cupsToLiters(cups);
	}

	/**
	 * @param cups : amount of given item in cups
	 * @return amount of same item in pints if cups >=0 , -1 otherwise
	 */
	static double cupsToPints(final double cups) {
		return cups < 0 ? -1 : cups / pint_cup_const;
	}

	/**
	 * @param cups : amount of given item in cups
	 * @return amount of same item in tablespoons if cups >=0 , -1 otherwise
	 */
	static double cupsToTablespoons(final double cups) {
		return cups < 0 ? -1 : cups * cup_tablespoon_const;
	}

	/**
	 * @param cups : amount of given item in cups
	 * @return amount of same item in teaspoons if cups >=0 , -1 otherwise
	 */
	static double cupsToTeaspoons(final double cups) {
		return cups < 0 ? -1 : cups * cup_teaspoon_const;
	}

	/**
	 * @param gallons : amount of given item in gallons
	 * @return amount of same item in cups if gallons >=0 , -1 otherwise
	 */
	static double gallonsToCups(final double gallons) {
		return gallons < 0 ? -1 : gallons * gallon_cup_const;
	}

	/**
	 * @param gallons : amount of given item in gallons
	 * @return amount of same item in liters if gallons >=0 , -1 otherwise
	 */
	static double gallonsToLiters(final double gallons) {
		return gallons < 0 ? -1 : gallons * gallon_liter_const;
	}

	/**
	 * @param gallons : amount of given item in gallons
	 * @return amount of same item in milliliters if gallons >=0 , -1 otherwise
	 */
	static double gallonsToMilliliters(final double gallons) {
		return gallons < 0 ? -1 : liters_const * gallonsToLiters(gallons);
	}

	/**
	 * @param gallons : amount of given item in gallons
	 * @return amount of same item in pints if gallons >=0 , -1 otherwise
	 */
	static double gallonsToPints(final double gallons) {
		return gallons < 0 ? -1 : gallons * gallon_pint_const;
	}

	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in cups if liters >=0 , -1 otherwise
	 */
	static double litersToCups(final double liters) {
		return liters < 0 ? -1 : liters * liter_cup_const;
	}

	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in gallons if liters >=0 , -1 otherwise
	 */
	static double litersToGallons(final double liters) {
		return liters < 0 ? -1 : liters / gallon_liter_const;
	}

	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in pints if liters >=0 , -1 otherwise
	 */
	static double litersToPints(final double liters) {
		return liters < 0 ? -1 : liters * liter_pint_const;
	}

	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in tablespoons if liters >=0 , -1 otherwise
	 */
	static double litersToTablespoons(final double liters) {
		return liters < 0 ? -1 : liters * liter_tablespoon_const;
	}

	/**
	 * @param liters : amount of given item in liters
	 * @return amount of same item in teaspoons if liters >=0 , -1 otherwise
	 */
	static double litersToTeaspoons(final double liters) {
		return liters < 0 ? -1 : liters * liter_teaspoon_const;
	}

	/**
	 * @param ml : amount of given item in milliliters
	 * @return amount of same item in cups if ml >=0 , -1 otherwise
	 */
	static double millilitersToCups(final double ml) {
		return ml < 0 ? -1 : litersToCups(ml) / liters_const;
	}

	/**
	 * @param milliliters : amount of given item in milliliters
	 * @return amount of same item in gallons if milliliters >=0 , -1 otherwise
	 */
	static double millilitersToGallons(final double milliliters) {
		return milliliters < 0 ? -1 : litersToGallons(milliliters) / liters_const;
	}

	/**
	 * @param milliliters : amount of given item in milliliters
	 * @return amount of same item in pints if milliliters >=0 , -1 otherwise
	 */
	static double millilitersToPints(final double milliliters) {
		return milliliters < 0 ? -1 : litersToPints(milliliters) / liters_const;
	}

	/**
	 * @param ml : amount of given item in milliliters
	 * @return amount of same item in tablespoons if ml >=0 , -1 otherwise
	 */
	static double millilitersToTablespoons(final double ml) {
		return ml < 0 ? -1 : litersToTablespoons(ml) / liters_const;
	}

	/**
	 * @param ml : amount of given item in milliliters
	 * @return amount of same item in teaspoons if ml >=0 , -1 otherwise
	 */
	static double millilitersToTeaspoons(final double ml) {
		return ml < 0 ? -1 : litersToTeaspoons(ml) / liters_const;
	}

	/**
	 * @param pints : amount of given item in pints
	 * @return amount of same item in cups if pints >=0 , -1 otherwise
	 */
	static double pintsToCups(final double pints) {
		return pints < 0 ? -1 : pints * pint_cup_const;
	}

	/**
	 * @param pints : amount of given item in pints
	 * @return amount of same item in gallons if pints >=0 , -1 otherwise
	 */
	static double pintsToGallons(final double pints) {
		return pints < 0 ? -1 : pints / gallon_pint_const;
	}

	/**
	 * @param pints : amount of given item in pints
	 * @return amount of same item in liters if pints >=0 , -1 otherwise
	 */
	static double pintsToLiters(final double pints) {
		return pints < 0 ? -1 : pints / liter_pint_const;
	}

	/**
	 * @param pints : amount of given item in pints
	 * @return amount of same item in milliliters if pints >=0 , -1 otherwise
	 */
	static double pintsToMilliliters(final double pints) {
		return pints < 0 ? -1 : liters_const * pintsToLiters(pints);
	}

	/**
	 * @param t : amount of given item in tablespoons
	 * @return amount of same item in cups if t >=0 , -1 otherwise
	 */
	static double tablespoonsToCups(final double t) {
		return t < 0 ? -1 : t / cup_tablespoon_const;
	}

	/**
	 * @param t : amount of given item in tablespoons
	 * @return amount of same item in liters if t >=0 , -1 otherwise
	 */
	static double tablespoonsToLiters(final double t) {
		return t < 0 ? -1 : t / liter_tablespoon_const;
	}

	/**
	 * @param t : amount of given item in tablespoons
	 * @return amount of same item in milliliters if t >=0 , -1 otherwise
	 */
	static double tablespoonsToMilliliters(final double t) {
		return t < 0 ? -1 : liters_const * tablespoonsToLiters(t);
	}

	/**
	 * @param t : amount of given item in tablespoons
	 * @return amount of same item in teaspoons if t >=0 , -1 otherwise
	 */
	static double tablespoonsToTeaspoons(final double t) {
		return t < 0 ? -1 : t * tablespoon_teaspoon_const;
	}

	/**
	 * @param t : amount of given item in teaspoons
	 * @return amount of same item in cups if t >=0 , -1 otherwise
	 */
	static double teaspoonsToCups(final double t) {
		return t < 0 ? -1 : t / cup_teaspoon_const;
	}

	/**
	 * @param t : amount of given item in teaspoons
	 * @return amount of same item in liters if t >=0 , -1 otherwise
	 */
	static double teaspoonsToLiters(final double t) {
		return t < 0 ? -1 : t / liter_teaspoon_const;
	}

	/**
	 * @param t : amount of given item in teaspoons
	 * @return amount of same item in milliliters if t >=0 , -1 otherwise
	 */
	static double teaspoonsToMilliliters(final double t) {
		return t < 0 ? -1 : liters_const * teaspoonsToLiters(t);
	}

	/**
	 * @param t : amount of given item in teaspoons
	 * @return amount of same item in tablespoons if t >=0 , -1 otherwise
	 */
	static double teaspoonsToTablespoons(final double t) {
		return t < 0 ? -1 : t / tablespoon_teaspoon_const;
	}
}
