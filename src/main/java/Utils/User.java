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
	public int age=-1;
	public int weight=-1;
	public int height=-1;
	public double dailyCalories;
	public double dailyCaloriesGoal=-1;
	public double dailyLitresOfWater;
	public double dailyLitresOfWaterGoal=-1;
	public double dailyProteinGrams;
	public double dailyProteinGramsGoal=-1;
	
	/*
	 * basic constructor with all the parameters
	 */
	public User(String name, Gender gender, int age, int weight, int height,
			double dailyCaloriesGoal,double dailyLitresOfWaterGoal,
			double dailyProteinGramsGoal, double dailyCalories, double dailyLitresOfWater,
			double dailyProteinGrams) {
		this.name=name;
		this.gender=gender;
		this.age=age;
		this.weight=weight;
		this.height=height;
		this.dailyCaloriesGoal=dailyCaloriesGoal;
		this.dailyLitresOfWaterGoal=dailyLitresOfWaterGoal;
		this.dailyProteinGramsGoal=dailyProteinGramsGoal;
		this.dailyCalories = dailyCalories;
		this.dailyLitresOfWater = dailyLitresOfWater;
		this.dailyProteinGrams = dailyProteinGrams;
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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender g) {
		this.gender = g;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getDailyCalories() {
		return dailyCalories;
	}

	public void setDailyCalories(double dailyCalories) {
		this.dailyCalories = dailyCalories;
	}

	public double getDailyCaloriesGoal() {
		return dailyCaloriesGoal;
	}

	public void setDailyCaloriesGoal(double dailyCaloriesGoal) {
		this.dailyCaloriesGoal = dailyCaloriesGoal;
	}

	public double getDailyLitresOfWater() {
		return dailyLitresOfWater;
	}

	public void setDailyLitresOfWater(double dailyLitresOfWater) {
		this.dailyLitresOfWater = dailyLitresOfWater;
	}

	public double getDailyLitresOfWaterGoal() {
		return dailyLitresOfWaterGoal;
	}

	public void setDailyLitresOfWaterGoal(double dailyLitresOfWaterGoal) {
		this.dailyLitresOfWaterGoal = dailyLitresOfWaterGoal;
	}

	public double getDailyProteinGrams() {
		return dailyProteinGrams;
	}

	public void setDailyProteinGrams(double dailyProteinGrams) {
		this.dailyProteinGrams = dailyProteinGrams;
	}

	public double getDailyProteinGramsGoal() {
		return dailyProteinGramsGoal;
	}

	public void setDailyProteinGramsGoal(double dailyProteinGramsGoal) {
		this.dailyProteinGramsGoal = dailyProteinGramsGoal;
	}
	
	
	
}
