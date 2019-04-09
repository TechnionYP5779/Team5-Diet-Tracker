package Utils;

import java.util.ArrayList;
import java.util.List;

public class DailyStatistics {
	public String cupsOfWater = "0";
	public List<Portion> foodPortions = new ArrayList<>();
	public String ciggaretesSmoked = "0";
	public String dailyCalories = "0.0";
	public String dailyProteins = "0.0";
	public String dailyCarbs = "0.0";
	public String dailyFats = "0.0";

	public void calculateDailyNutritions() {
		Double calories = 0.0, proteins = 0.0, carbs = 0.0, fats = 0.0;
		for (Portion portion : foodPortions) {
			calories += portion.amount * portion.calories_per_100_grams / 100;
			proteins += portion.amount * portion.proteins_per_100_grams / 100;
			carbs += portion.amount * portion.carbs_per_100_grams / 100;
			fats += portion.amount * portion.fats_per_100_grams / 100;
		}
		this.dailyCalories = calories.toString();
		this.dailyProteins = proteins.toString();
		this.dailyCarbs = carbs.toString();
		this.dailyFats = fats.toString();
	}
}
