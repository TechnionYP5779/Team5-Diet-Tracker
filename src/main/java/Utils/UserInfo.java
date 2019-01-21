package Utils;

public class UserInfo {
	public enum Gender {
		MALE, FEMALE
	}	
	public Gender gender=Gender.MALE;
	public int age=-1;
	public int height=-1;
	public double dailyCaloriesGoal=-1;
	public double dailyProteinGramsGoal=-1;
	public double dailyCarbsGoal = -1;
	public double dailyFatsGoal = -1;
	public int dailyLimitCigarettes = -1;
	public int weightGoal=-1;
	
	/*
	 * basic constructor with all the parameters
	 */
	public UserInfo(Gender gender, int age, int height,
			double dailyCaloriesGoal,
			double dailyProteinGramsGoal, double dailyCarbsGoal,
			double dailyFatsGoal, int dailyLimitCigarettes) {
		this.gender=gender;
		this.age=age;
		this.height=height;
		this.dailyCaloriesGoal=dailyCaloriesGoal;
		this.dailyProteinGramsGoal=dailyProteinGramsGoal;
		this.dailyCarbsGoal = dailyCarbsGoal;
		this.dailyFatsGoal = dailyFatsGoal;
		this.dailyLimitCigarettes = dailyLimitCigarettes;
	}
	
	public UserInfo(Gender gender, int age, int height,
			double dailyCaloriesGoal,
			double dailyProteinGramsGoal, double dailyCarbsGoal,
			double dailyFatsGoal, int dailyLimitCigarettes,int wg) {
		this.gender=gender;
		this.age=age;
		this.height=height;
		this.dailyCaloriesGoal=dailyCaloriesGoal;
		this.dailyProteinGramsGoal=dailyProteinGramsGoal;
		this.dailyCarbsGoal = dailyCarbsGoal;
		this.dailyFatsGoal = dailyFatsGoal;
		this.dailyLimitCigarettes = dailyLimitCigarettes;
		this.weightGoal=wg;
	}
	
	/*
	 * default constructor
	 */
	public UserInfo() {
	}
	
	@Override
	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final UserInfo user = (UserInfo) o;
		return Double.compare(user.age, age) == 0
				&& Double.compare(user.height, height) == 0
				&& Double.compare(user.dailyCaloriesGoal, dailyCaloriesGoal) == 0
				&& Double.compare(user.dailyProteinGramsGoal, dailyProteinGramsGoal) == 0 &&
				gender == user.gender
				&& this.weightGoal==user.weightGoal;
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

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getDailyCaloriesGoal() {
		return dailyCaloriesGoal;
	}

	public void setDailyCaloriesGoal(double dailyCaloriesGoal) {
		this.dailyCaloriesGoal = dailyCaloriesGoal;
	}

	public double getDailyProteinGramsGoal() {
		return dailyProteinGramsGoal;
	}

	public void setDailyProteinGramsGoal(double dailyProteinGramsGoal) {
		this.dailyProteinGramsGoal = dailyProteinGramsGoal;
	}
	
	public double getDailyCarbsGoal() {
		return dailyCarbsGoal;
	}

	public void setDailyCarbsGoal(double dailyCarbsGoal) {
		this.dailyCarbsGoal = dailyCarbsGoal;
	}
	
	public double getDailyFatsGoal() {
		return dailyFatsGoal;
	}

	public void setDailyFatsGoal(double dailyFatsGoal) {
		this.dailyFatsGoal = dailyFatsGoal;
	}
	
	public int getDailyLimitCigarettes() {
		return dailyLimitCigarettes;
	}
	
	public void setDailyLimitCigarettes(int dailyLimitCigarettes) {
		this.dailyLimitCigarettes = dailyLimitCigarettes;
	}
	
	public int getWeightGoal() {
		return weightGoal;
	}
	
	public void setWeightGoal(int wg) {
		this.weightGoal=wg;
	}
}
