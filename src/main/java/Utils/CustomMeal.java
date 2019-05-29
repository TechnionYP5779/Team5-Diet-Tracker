package Utils;

import java.util.Date;
import Utils.Portion.Portion;

//For now doesn't have portion list - maybe should add

public class CustomMeal {
	public final String name;
	public double calories;
	public double proteins;
	public double carbs;
	public double fats;

	public CustomMeal() {
		this.name = "garbage";
		this.calories = 0;
		this.proteins = 0;
		this.carbs = 0;
		this.fats = 0;
	}

	public CustomMeal(String name) {
		this.name = name;
		this.calories = 0;
		this.proteins = 0;
		this.carbs = 0;
		this.fats = 0;
	}

	public CustomMeal(String name, Date time, double calories, double proteins, double carbs, double fats) {
		super();
		this.name = name;
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
		return this.name.equals(food.name) && Double.compare(this.calories, food.calories) == 0
				&& Double.compare(this.proteins, food.proteins) == 0 && Double.compare(this.carbs, food.carbs) == 0
				&& Double.compare(this.fats, food.fats) == 0;
	}

	@Override
	public String toString() {
		return "Portion name: " + this.name + "."
				+ "\n----------------------------------\nNutritional Values per 100 grams:\nCalories: " + this.calories
				+ "\nProteins: " + this.proteins + "\nCarbohydrates: " + this.carbs + "\nFats: " + this.fats;
	}

	public String getName() {
		return name;
	}

	public void addPortion(Portion p) {
		this.calories = p.getCalories_per_100_grams() * p.getAmount() / 100;
		this.fats = p.getFats_per_100_grams() * p.getAmount() / 100;
		this.carbs = p.getCarbs_per_100_grams() * p.getAmount() / 100;
		this.proteins = p.getProteins_per_100_grams() * p.getAmount() / 100;
	}

	public Portion toPortion(int amount) {
		return new Portion(Portion.Type.MEAL, this.name, 100 * amount, this.calories, this.proteins, this.carbs,
				this.fats);
	}
}
