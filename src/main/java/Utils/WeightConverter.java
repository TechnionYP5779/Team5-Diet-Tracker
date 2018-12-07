/**This interface is used as a converter between different types of 
 * weight units, such as gr, pounds, etc.
 * @author Shaked Sapir
 * @since 2018-12-07*/
package Utils;




public interface WeightConverter {
	
	/**
	 * this section handles conversion between same-scale units (e.g. grams-->kilograms).
	 */
	public static final double grams_const = 1000;
	
		
	/**
	 * @param gr : amount of given item in grams
	 * @return amount of same item in milligrams if gr >=0 , -1 otherwise
	 */
	static double grToMg(double gr) {
		return gr < 0 ? -1 : gr * grams_const;
	}
	
	/**
	 * @param gr : amount of given item in grams
	 * @return amount of same item in kilograms if gr >=0 , -1 otherwise
	 */
	static double grToKg(double gr) {
		return gr < 0 ? -1 : gr / grams_const;
	}
	
	/**
	 * @param kg : amount of given item in kilograms
	 * @return amount of same item in milligrams if kg >=0 , -1 otherwise
	 */
	static double kgToMg(double kg) {
		return kg < 0 ? -1 : kg * (Math.pow(grams_const, 2));
	}
	
	/**
	 * @param kg : amount of given item in kilograms
	 * @return amount of same item in grams if kg >=0 , -1 otherwise
	 */
	static double kgToGr(double kg) {
		return kg < 0 ? -1 : kg * grams_const;
	}
	
	/**
	 * @param mg : amount of given item in milligrams
	 * @return amount of same item in grams if mg >=0 , -1 otherwise
	 */
	static double mgToGr(double mg) {
		return mg < 0 ? -1 : mg / grams_const;
	} 
	
	/**
	 * @param mg : amount of given item in milligrams
	 * @return amount of same item in kilograms if mg >=0 , -1 otherwise
	 */
	static double mgToKg(double mg) {
		return mg < 0 ? -1 : mg / (Math.pow(grams_const, 2));
	}
}
