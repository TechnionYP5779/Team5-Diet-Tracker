/** this class represents a DailyInfo Object
 * @author Nir Chachamovitz
 * @since 2018-12-25*/
package Utils;

public class DailyInfo {	
	public double weight=-1;
	public double dailyCalories;
	public double dailyLitresOfWater;
	public double dailyProteinGrams;
	
	/*
	 * basic constructor with all the parameters
	 */
	public DailyInfo(double weight, double dailyCalories, double dailyLitresOfWater,
			double dailyProteinGrams) {
		this.weight=weight;
		this.dailyCalories = dailyCalories;
		this.dailyLitresOfWater = dailyLitresOfWater;
		this.dailyProteinGrams = dailyProteinGrams;
	}
	
	/*
	 * default constructor
	 */
	public DailyInfo() {
		
	}
	
	@Override
	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final User user = (User) o;
		return Double.compare(user.weight, weight) == 0
				&& Double.compare(user.dailyCalories, dailyCalories) == 0
				&& Double.compare(user.dailyLitresOfWater, dailyLitresOfWater) == 0
				&& Double.compare(user.dailyProteinGrams, dailyProteinGrams) == 0;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public double getDailyCalories() {
		return dailyCalories;
	}

	public void setDailyCalories(double dailyCalories) {
		this.dailyCalories = dailyCalories;
	}

	public double getDailyLitresOfWater() {
		return dailyLitresOfWater;
	}

	public void setDailyLitresOfWater(double dailyLitresOfWater) {
		this.dailyLitresOfWater = dailyLitresOfWater;
	}

	public double getDailyProteinGrams() {
		return dailyProteinGrams;
	}

	public void setDailyProteinGrams(double dailyProteinGrams) {
		this.dailyProteinGrams = dailyProteinGrams;
	}	
	
	
}
