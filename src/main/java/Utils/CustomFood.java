package Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Utils.Portion.Portion;

public class CustomFood {
	public final String name;
	public Date time;
	public List<Portion> portions;
	public double calories;
	public double proteins;
	public double carbs;
	public double fats;

	public CustomFood(String name) {
		this.name = name;
		this.time = new Date();
		this.portions = new ArrayList<>();
		this.calories = 0;
		this.proteins = 0;
		this.carbs = 0;
		this.fats = 0;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final CustomFood food = (CustomFood) o;
		return this.name.equals(food.name) && this.time.equals(food.time) && this.portions.equals(food.portions)
				&& Double.compare(this.calories, food.calories) == 0
				&& Double.compare(this.proteins, food.proteins) == 0 && Double.compare(this.carbs, food.carbs) == 0
				&& Double.compare(this.fats, food.fats) == 0;
	}

	@Override
	public String toString() {
		return name + " taken in " + time + ".\nhas:\n" + portions.toString();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public List<Portion> getPortions() {
		return portions;
	}

	public void setPortions(List<Portion> portions) {
		this.portions = portions;
	}

	public String getName() {
		return name;
	}

	public void addPortion(Portion p) {
		this.portions.add(p);
		this.calories = p.getCalories_per_100_grams() * p.getAmount();
		this.fats = p.getFats_per_100_grams() * p.getAmount();
		this.carbs = p.getCarbs_per_100_grams() * p.getAmount();
		this.proteins = p.getProteins_per_100_grams() * p.getAmount();
	}
}
