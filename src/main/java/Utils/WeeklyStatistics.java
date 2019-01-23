package Utils;

import java.util.ArrayList;
import java.util.List;

public class WeeklyStatistics {
	public List<DailyStatistics> dailyStatistics = new ArrayList<>();
	public String weeklyCalories = "0.0";
	public String weeklyProteins = "0.0";
	public String weeklyCarbs = "0.0";
	public String weeklyFats = "0.0";
	public String weeklyWaterCups = "0";
	public String weeklyCiggaretsSmoked = "0";
	public void calculateWeeklyData() {
		
		Double calories = 0.0;
		Double proteins = 0.0;
		Double carbs = 0.0;
		Double fats = 0.0;
		Integer waterCups = 0;
		Integer ciggaretsSmoked = 0;
		for (DailyStatistics ds : dailyStatistics) {
			calories += Double.valueOf(ds.dailyCalories);
			proteins += Double.valueOf(ds.dailyProteins);
			carbs += Double.valueOf(ds.dailyCarbs);
			fats += Double.valueOf(ds.dailyFats);
			waterCups += Integer.valueOf(ds.cupsOfWater);
			ciggaretsSmoked += Integer.valueOf(ds.ciggaretesSmoked);
		}
		weeklyCalories = String.format("%.2f", Double.valueOf(calories.toString()));
		weeklyProteins = String.format("%.2f", Double.valueOf(proteins.toString()));
		weeklyCarbs = String.format("%.2f", Double.valueOf(carbs.toString()));
		weeklyFats = String.format("%.2f", Double.valueOf(fats.toString()));
		weeklyWaterCups = waterCups.toString();
		weeklyCiggaretsSmoked = ciggaretsSmoked.toString();
	}
}
