/** this class represents a portion object - it can be either FOOD or DRINK.
 *  all nutritional values are saved as the amount is 100 grams of product.
 * @author Shaked Sapir
 * @since 2018-12-17*/
package Utils.Portion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Portion {
	public enum Type {
		FOOD, DRINK, MEAL
	}

	// todo: maybe add supper? and brunch?
	public enum Meal {
		BREAKFAST, LUNCH, DINNER, MIDNIGHT
	}

	public final Meal meal;
	public final Type type;
	public String name;

	/** if food - in grams, if liquid - in milliliters **/
	public double amount;
	public String units;

	/** nutritional values **/
	public double calories_per_100_grams;
	public double proteins_per_100_grams;
	public double carbs_per_100_grams;
	public double fats_per_100_grams;
	public double alchohol_by_volume;
	/** the date and time the portion has taken **/
	public Date time;

	public Portion(final Portion.Type type, final String name, final double amount, final double calories,
			final double proteins, final double carbs, final double fats) {
		this.type = type;
		this.name = name;
		this.amount = amount;
		this.calories_per_100_grams = calories;
		this.proteins_per_100_grams = proteins;
		this.carbs_per_100_grams = carbs;
		this.fats_per_100_grams = fats;
		this.alchohol_by_volume = 0;
		this.time = new Date();
		Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
		calendar.setTime(this.time); // assigns calendar to given date
		final int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
		this.meal = (hour > 7 && hour < 11 ? Portion.Meal.BREAKFAST
				: (hour >= 11 && hour <= 17 ? Portion.Meal.LUNCH
						: (hour >= 17 && hour <= 22 ? Portion.Meal.DINNER : Portion.Meal.MIDNIGHT)));
	}
	
	public Portion(final Portion.Type type, final String name, final double amount, final double calories,
			final double proteins, final double carbs, final double fats, final String units) {
		this.type = type;
		this.name = name;
		this.amount = amount;
		this.calories_per_100_grams = calories;
		this.proteins_per_100_grams = proteins;
		this.carbs_per_100_grams = carbs;
		this.fats_per_100_grams = fats;
		this.alchohol_by_volume = 0;
		this.units = units;
		this.time = new Date();
		Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
		calendar.setTime(this.time); // assigns calendar to given date
		final int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
		this.meal = (hour > 7 && hour < 11 ? Portion.Meal.BREAKFAST
				: (hour >= 11 && hour <= 17 ? Portion.Meal.LUNCH
						: (hour >= 17 && hour <= 22 ? Portion.Meal.DINNER : Portion.Meal.MIDNIGHT)));
	}

	public Portion(final Portion.Type type, final String name, final double amount, final double calories,
			final double proteins, final double carbs, final double fats, final double alchohol) {
		this.type = type;
		this.name = name;
		this.amount = amount;
		this.calories_per_100_grams = calories;
		this.proteins_per_100_grams = proteins;
		this.carbs_per_100_grams = carbs;
		this.fats_per_100_grams = fats;
		this.alchohol_by_volume = alchohol;
		this.time = new Date();
		Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
		calendar.setTime(this.time); // assigns calendar to given date
		final int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
		this.meal = (hour > 7 && hour < 11 ? Portion.Meal.BREAKFAST
				: (hour >= 11 && hour <= 17 ? Portion.Meal.LUNCH
						: (hour >= 17 && hour <= 22 ? Portion.Meal.DINNER : Portion.Meal.MIDNIGHT)));

	}

	public Portion(final Portion.Type type, final String name, final double amount, final double calories,
			final double proteins, final double carbs, final double fats, final double alchohol, final Date time) {
		this.type = type;
		this.name = name;
		this.amount = amount;
		this.calories_per_100_grams = calories;
		this.proteins_per_100_grams = proteins;
		this.carbs_per_100_grams = carbs;
		this.fats_per_100_grams = fats;
		this.alchohol_by_volume = alchohol;
		this.time = time;
		Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
		calendar.setTime(this.time); // assigns calendar to given date
		final int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
		this.meal = (hour > 7 && hour < 11 ? Portion.Meal.BREAKFAST
				: (hour >= 11 && hour <= 17 ? Portion.Meal.LUNCH
						: (hour >= 17 && hour <= 22 ? Portion.Meal.DINNER : Portion.Meal.MIDNIGHT)));

	}

	public Portion() {
		this.type = Type.FOOD;
		this.name = "garbage";
		this.amount = 0;
		this.calories_per_100_grams = 0;
		this.proteins_per_100_grams = 0;
		this.carbs_per_100_grams = 0;
		this.fats_per_100_grams = 0;
		this.alchohol_by_volume = 0;
		this.time = null;
		this.meal = Meal.BREAKFAST;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Portion portion = (Portion) o;
		return Double.compare(portion.amount, amount) == 0
				&& Double.compare(portion.calories_per_100_grams, calories_per_100_grams) == 0
				&& Double.compare(portion.proteins_per_100_grams, proteins_per_100_grams) == 0
				&& Double.compare(portion.carbs_per_100_grams, carbs_per_100_grams) == 0
				&& Double.compare(portion.fats_per_100_grams, fats_per_100_grams) == 0 && type == portion.type
				&& Double.compare(portion.alchohol_by_volume, alchohol_by_volume) == 0
				&& (portion.time == null ? time == null : portion.time.equals(time))
				&& Objects.equals(name, portion.name) && meal == portion.meal;
	}

	public double getAmount() {
		return amount;
	}

	public double getCalories_per_100_grams() {
		return calories_per_100_grams;
	}

	public double getCarbs_per_100_grams() {
		return carbs_per_100_grams;
	}

	public double getFats_per_100_grams() {
		return fats_per_100_grams;
	}

	public String getName() {
		return name;
	}

	public double getProteins_per_100_grams() {
		return proteins_per_100_grams;
	}

	public double getAlchohol_by_volume() {
		return alchohol_by_volume;
	}

	public Date getTime() {
		return time;
	}

	public Type getType() {
		return type;
	}

	public Meal getMeal() {
		return meal;
	}

	public void setAmount(final double amount) {
		this.amount = amount;
	}

	public void setCalories_per_100_grams(final double calories_per_100_grams) {
		this.calories_per_100_grams = calories_per_100_grams;
	}

	public void setCarbs_per_100_grams(final double carbs_per_100_grams) {
		this.carbs_per_100_grams = carbs_per_100_grams;
	}

	public void setFats_per_100_grams(final double fats_per_100_grams) {
		this.fats_per_100_grams = fats_per_100_grams;
	}

	public void setProteins_per_100_grams(final double proteins_per_100_grams) {
		this.proteins_per_100_grams = proteins_per_100_grams;
	}

	public void setAlchohol_by_volume(double alchohol_by_volume) {
		this.alchohol_by_volume = alchohol_by_volume;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	public String getUnits() {
		return this.units;
	}
	
	public void setUnits(String unit) {
		this.units = unit;
	}

	@Override
	public String toString() {
		final String units = this.type != Type.FOOD ? " ml" : " grams";
		return "Portion name: " + this.name + " , " + this.amount + units + "\nPortion type: " + this.type
				+ "\nPortion Meal: " + this.meal
				+ "\n----------------------------------\nNutritional Values per 100 grams:\nCalories: "
				+ this.calories_per_100_grams + "\nProteins: " + this.proteins_per_100_grams + "\nCarbohydrates: "
				+ this.carbs_per_100_grams + "\nFats: " + this.fats_per_100_grams + "\nAlchohol by volume: "
				+ this.alchohol_by_volume + "\nTime taken: "
				+ (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(time);
	}
}