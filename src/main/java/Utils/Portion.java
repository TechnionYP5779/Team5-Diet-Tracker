/** this class represents a portion object - it can be either FOOD or DRINK.
 *  all nutritional values are saved as the amount is 100 grams of product.
 * @author Shaked Sapir
 * @since 2018-12-17*/
package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Portion {
	public enum Type {
		FOOD, DRINK
	}

	public final Type type;
	public final String name;

	/** if food - in grams, if liquid - in milliliters **/
	public double amount;

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
				&& Objects.equals(name, portion.name);
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

	@Override
	public String toString() {
		final String units = this.type != Type.FOOD ? " ml" : " grams";
		return "Portion name: " + this.name + " , " + this.amount + units + "\nPortion type: " + this.type + "\n"
				+ "----------------------------------\nNutritional Values per 100 grams:\nCalories: "
				+ this.calories_per_100_grams + "\nProteins: " + this.proteins_per_100_grams + "\nCarbohydrates: "
				+ this.carbs_per_100_grams + "\nFats: " + this.fats_per_100_grams + "\nAlchohol by volume: "
				+ this.alchohol_by_volume + "\nTime taken: "
				+ (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(time);
	}
}