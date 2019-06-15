package Utils.Email;

import static org.junit.Assert.*;

import org.junit.Test;

import Utils.Email.DailyStatistics;
import Utils.Email.WeeklyStatistics;

public class WeeklyStatisticsTest {

	@SuppressWarnings("static-method")
	@Test
	public void testCalculateWeeklyData() {
		DailyStatistics ds1 = new DailyStatistics();
		ds1.ciggaretesSmoked = "1";
		ds1.cupsOfWater = "1";
		ds1.dailyCalories = "1";
		ds1.dailyCarbs = "1";
		ds1.dailyFats = "1";
		ds1.dailyProteins = "1";
		DailyStatistics ds2 = new DailyStatistics();
		ds2.ciggaretesSmoked = "2";
		ds2.cupsOfWater = "2";
		ds2.dailyCalories = "2";
		ds2.dailyCarbs = "2";
		ds2.dailyFats = "2";
		ds2.dailyProteins = "2";
		DailyStatistics ds3 = new DailyStatistics();
		ds3.ciggaretesSmoked = "3";
		ds3.cupsOfWater = "3";
		ds3.dailyCalories = "3";
		ds3.dailyCarbs = "3";
		ds3.dailyFats = "3";
		ds3.dailyProteins = "3";
		WeeklyStatistics ws = new WeeklyStatistics();
		ws.dailyStatistics.add(ds1);
		ws.dailyStatistics.add(ds2);
		ws.dailyStatistics.add(ds3);

		ws.calculateWeeklyData();

		assertEquals("6.00", ws.weeklyCalories);
		assertEquals("6.00", ws.weeklyCarbs);
		assertEquals("6.00", ws.weeklyProteins);
		assertEquals("6.00", ws.weeklyFats);
		assertEquals("6", ws.weeklyWaterCups);
		assertEquals("6", ws.weeklyCiggaretsSmoked);
	}
}
