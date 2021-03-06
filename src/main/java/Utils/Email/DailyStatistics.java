package Utils.Email;

import java.util.ArrayList;
import java.util.List;

import Utils.Portion.Portion;

public class DailyStatistics {
	public String cupsOfWater = "0";
	public List<Portion> foodPortions = new ArrayList<>();
	public String ciggaretesSmoked = "0";
	public String dailyCalories = "0.0";
	public String dailyProteins = "0.0";
	public String dailyCarbs = "0.0";
	public String dailyFats = "0.0";

	public void calculateDailyNutritions() {
		double calories = 0.0, proteins = 0.0, carbs = 0.0, fats = 0.0;
		for (Portion portion : foodPortions) {
			calories += portion.amount * portion.calories_per_100_grams;
			proteins += portion.amount * portion.proteins_per_100_grams;
			carbs += portion.amount * portion.carbs_per_100_grams;
			fats += portion.amount * portion.fats_per_100_grams;
		}
		this.dailyCalories = ""+calories;
		this.dailyProteins = ""+proteins;
		this.dailyCarbs = ""+carbs;
		this.dailyFats =""+ fats;
	}
}
