package Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Utils.Portion.Portion;
import Utils.Portion.Portion.Type;

//For now doesn't have portion list - maybe should add

public class CustomMeal {
	public final String name;
	// public Date time;
	// public List<Portion> portions;
	public double calories;
	public double proteins;
	public double carbs;
	public double fats;

	public CustomMeal() {
		this.name = "garbage";
		// this.time = new Date();
		// this.portions = new ArrayList<>();
		this.calories = 0;
		this.proteins = 0;
		this.carbs = 0;
		this.fats = 0;
	}

	public CustomMeal(String name) {
		this.name = name;
		// this.time = new Date();
		// this.portions = new ArrayList<>();
		this.calories = 0;
		this.proteins = 0;
		this.carbs = 0;
		this.fats = 0;
	}

	public CustomMeal(String name, Date time, double calories, double proteins, double carbs, double fats) {
		super();
		this.name = name;
		// this.time = time;
		// this.portions = new ArrayList<>(portions);
		this.calories = calories;
		this.proteins = proteins;
		this.carbs = carbs;
		this.fats = fats;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final CustomMeal food = (CustomMeal) o;
		return this.name.equals(food.name) // && this.time.equals(food.time)
				&& Double.compare(this.calories, food.calories) == 0
				&& Double.compare(this.proteins, food.proteins) == 0 && Double.compare(this.carbs, food.carbs) == 0
				&& Double.compare(this.fats, food.fats) == 0;
	}

	@Override
	public String toString() {
		return "Portion name: " + this.name + "."
				+ "\n----------------------------------\nNutritional Values per 100 grams:\nCalories: " + this.calories
				+ "\nProteins: " + this.proteins + "\nCarbohydrates: " + this.carbs + "\nFats: " + this.fats;
	}

//	public Date getTime() {
//		return time;
//	}

//	public void setTime(Date time) {
//		this.time = time;
//	}

	public String getName() {
		return name;
	}

	public void addPortion(Portion p) {
//		this.portions.add(p);
		this.calories = p.getCalories_per_100_grams() * p.getAmount();
		this.fats = p.getFats_per_100_grams() * p.getAmount();
		this.carbs = p.getCarbs_per_100_grams() * p.getAmount();
		this.proteins = p.getProteins_per_100_grams() * p.getAmount();
	}
}
