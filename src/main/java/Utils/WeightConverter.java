/**This interface is used as a converter between different types of
 * weight units, such as gr, pounds, etc.
 * @author Shaked Sapir
 * @since 2018-12-07*/
package Utils;

public interface WeightConverter {

	/**
	 * this section handles conversion between same-scale units (e.g.
	 * grams-->kilograms).
	 */
	double grams_const = 1000;

	/**
	 * @param gr : amount of given item in grams
	 * @return amount of same item in gr if gr >=0 , -1 otherwise
	 */
	static double gramsToGrams(final double gr) {
		return gr < 0 ? -1 : gr ;
	}
	
	/**
	 * @param gr : amount of given item in grams
	 * @return amount of same item in milligrams if gr >=0 , -1 otherwise
	 */
	static double gramsToMilligrams(final double gr) {
		return gr < 0 ? -1 : gr * grams_const;
	}

	/**
	 * @param gr : amount of given item in grams
	 * @return amount of same item in kilograms if gr >=0 , -1 otherwise
	 */
	static double gramsToKilograms(final double gr) {
		return gr < 0 ? -1 : gr / grams_const;
	}

	/**
	 * @param kg : amount of given item in kilograms
	 * @return amount of same item in milligrams if kg >=0 , -1 otherwise
	 */
	static double kilogramsToMilligrams(final double kg) {
		return kg < 0 ? -1 : kg * Math.pow(grams_const, 2);
	}

	/**
	 * @param kg : amount of given item in kilograms
	 * @return amount of same item in grams if kg >=0 , -1 otherwise
	 */
	static double kilogramsToGrams(final double kg) {
		return kg < 0 ? -1 : kg * grams_const;
	}

	/**
	 * @param mg : amount of given item in milligrams
	 * @return amount of same item in grams if mg >=0 , -1 otherwise
	 */
	static double milligramsToGrams(final double mg) {
		return mg < 0 ? -1 : mg / grams_const;
	}

	/**
	 * @param mg : amount of given item in milligrams
	 * @return amount of same item in kilograms if mg >=0 , -1 otherwise
	 */
	static double milligramsToKilograms(final double mg) {
		return mg < 0 ? -1 : mg / Math.pow(grams_const, 2);
	}

	/********************************************************************/

	/**
	 * this section handles conversion between different-scales units (e.g.
	 * grams-->pounds).
	 */
	double kilogram_ounce_const = 35.273990723;
	double kilogram_pound_const = 2.2046244202;
	double pound_ounce_const = 16.;

	/**
	 * @param grams : amount of given item in grams
	 * @return amount of same item in ounces if grams >= 0, -1 otherwise
	 */
	static double gramsToOunces(final double grams) {
		return grams < 0 ? -1 : kilogramsToOunces(grams) / grams_const;
	}

	/**
	 * @param grams : amount of given item in grams
	 * @return amount of same item in pounds if grams >= 0, -1 otherwise
	 */
	static double gramsToPounds(final double grams) {
		return grams < 0 ? -1 : kilogramsToPounds(grams) / grams_const;
	}

	/**
	 * @param kilograms : amount of given item in kilograms
	 * @return amount of same item in ounces if kilograms >= 0, -1 otherwise
	 */
	static double kilogramsToOunces(final double kilograms) {
		return kilograms < 0 ? -1 : kilograms * kilogram_ounce_const;
	}

	/**
	 * @param kilograms : amount of given item in kilograms
	 * @return amount of same item in pounds if kilograms >= 0, -1 otherwise
	 */
	static double kilogramsToPounds(final double kilograms) {
		return kilograms < 0 ? -1 : kilograms * kilogram_pound_const;
	}

	/**
	 * @param milligrams : amount of given item in milligrams
	 * @return amount of same item in ounces if milligrams >= 0, -1 otherwise
	 */
	static double milligramsToOunces(final double milligrams) {
		return milligrams < 0 ? -1 : kilogramsToPounds(milligrams) / Math.pow(grams_const, 2);
	}

	/**
	 * @param milligrams : amount of given item in milligrams
	 * @return amount of same item in pounds if milligrams >= 0, -1 otherwise
	 */
	static double milligramsToPounds(final double milligrams) {
		return milligrams < 0 ? -1 : kilogramsToPounds(milligrams) / Math.pow(grams_const, 2);
	}

	/**
	 * @param ounces : amount of given item in ounces
	 * @return amount of same item in grams if ounces >= 0, -1 otherwise
	 */
	static double ouncesToGrams(final double ounces) {
		return ounces < 0 ? -1 : grams_const * ouncesToKilograms(ounces);
	}

	/**
	 * @param ounces : amount of given item in ounces
	 * @return amount of same item in kilograms if ounces >= 0, -1 otherwise
	 */
	static double ouncesToKilograms(final double ounces) {
		return ounces < 0 ? -1 : ounces / kilogram_ounce_const;
	}

	/**
	 * @param ounces : amount of given item in ounces
	 * @return amount of same item in milligrams if ounces >= 0, -1 otherwise
	 */
	static double ouncesToMilligrams(final double ounces) {
		return ounces < 0 ? -1 : ouncesToKilograms(ounces) * Math.pow(grams_const, 2);
	}

	/**
	 * @param ounces : amount of given item in ounces
	 * @return amount of same item in pounds if ounces >= 0, -1 otherwise
	 */
	static double ouncesToPounds(final double ounces) {
		return ounces < 0 ? -1 : ounces / pound_ounce_const;
	}

	/**
	 * @param pounds : amount of given item in pounds
	 * @return amount of same item in grams if pounds >= 0, -1 otherwise
	 */
	static double poundsToGrams(final double pounds) {
		return pounds < 0 ? -1 : grams_const * poundsToKilograms(pounds);
	}

	/**
	 * @param pounds : amount of given item in pounds
	 * @return amount of same item in kilograms if pounds >= 0, -1 otherwise
	 */
	static double poundsToKilograms(final double pounds) {
		return pounds < 0 ? -1 : pounds / kilogram_pound_const;
	}

	/**
	 * @param pounds : amount of given item in pounds
	 * @return amount of same item in milligrams if pounds >= 0, -1 otherwise
	 */
	static double poundsToMilligrams(final double pounds) {
		return pounds < 0 ? -1 : poundsToKilograms(pounds) * Math.pow(grams_const, 2);
	}

	/**
	 * @param pounds : amount of given item in pounds
	 * @return amount of same item in ounces if pounds >= 0, -1 otherwise
	 */
	static double poundsToOunces(final double pounds) {
		return pounds < 0 ? -1 : pounds * pound_ounce_const;
	}

}
