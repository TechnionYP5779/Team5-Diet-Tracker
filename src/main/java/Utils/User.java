/** this class represents a User Object
 * @author Lior Ben Yamin, Nir Chachamovitz
 * @since 2018-12-23*/
package Utils;

import java.util.Objects;

public class User {
	public enum Gender {
		MALE, FEMALE
	}
	
	public String name="";
	public Gender gender=Gender.MALE;
	public double age=-1;
	public double weight=-1;
	public double height=-1;
	public double dailyCalories;
	public double dailyCaloriesGoal=-1;
	public double dailyLitresOfWater;
	public double dailyLitresOfWaterGoal=-1;
	public double dailyProteinGrams;
	public double dailyProteinGramsGoal=-1;
	
	/*
	 * basic constructor with all the parameters
	 */
	public User(String name, Gender gender, double age, double weight, double height,
			double dailyCaloriesGoal,double dailyLitresOfWaterGoal,
			double dailyProteinGramsGoal) {
		this.name=name;
		this.gender=gender;
		this.age=age;
		this.weight=weight;
		this.height=height;
		this.dailyCaloriesGoal=dailyCaloriesGoal;
		this.dailyLitresOfWaterGoal=dailyLitresOfWaterGoal;
		this.dailyProteinGramsGoal=dailyProteinGramsGoal;
	}
	
	/*
	 * default constructor
	 */
	public User() {
		
	}
	
	@Override
	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final User user = (User) o;
		return Double.compare(user.age, age) == 0
				&& Double.compare(user.weight, weight) == 0
				&& Double.compare(user.height, height) == 0
				&& Double.compare(user.dailyCalories, dailyCalories) == 0
				&& Double.compare(user.dailyCaloriesGoal, dailyCaloriesGoal) == 0
				&& Double.compare(user.dailyLitresOfWater, dailyLitresOfWater) == 0
				&& Double.compare(user.dailyLitresOfWaterGoal, dailyLitresOfWaterGoal) == 0
				&& Double.compare(user.dailyProteinGrams, dailyProteinGrams) == 0
				&& Double.compare(user.dailyProteinGramsGoal, dailyProteinGramsGoal) == 0 && gender == user.gender
				&& Objects.equals(name, user.name);
	}
	
	
	
	
}
