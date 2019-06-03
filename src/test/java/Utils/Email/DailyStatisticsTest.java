package Utils.Email;

import static org.junit.Assert.*;

import org.junit.Test;

import Utils.Email.DailyStatistics;
import Utils.Portion.Portion;
import Utils.Portion.Portion.Type;

public class DailyStatisticsTest {

	@Test
	public void testCalculateDailyNutritions() {
		DailyStatistics ds = new DailyStatistics();
		ds.foodPortions.add(new Portion(Type.FOOD, "bacon", 10, 10, 5, 5, 5));
		ds.foodPortions.add(new Portion(Type.FOOD, "bacon", 20, 10, 5, 5, 5));
		ds.calculateDailyNutritions();
		assertEquals("3.0", ds.dailyCalories);
		assertEquals("1.5", ds.dailyCarbs);
		assertEquals("1.5", ds.dailyProteins);
		assertEquals("1.5", ds.dailyFats);
	}
}
