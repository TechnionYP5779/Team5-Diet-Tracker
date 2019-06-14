/**add here document
 * @author Fname Sname
 * @since year-month-day*/
package Utils.Portion;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import Utils.Portion.Portion;
import Utils.Portion.Portion.Type;

/**
 * @author Shaked Sapir
 *
 */
@SuppressWarnings("unlikely-arg-type")
public class PortionTest {

	double DELTA = 0.01;

	@Test
	public void testConstr() {
		final Portion bacon = new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3);
		assertEquals(Type.FOOD, bacon.getType());
		assertEquals("bacon", bacon.getName());
		assertEquals(100, bacon.getAmount(), DELTA);
		assertEquals(700, bacon.getCalories_per_100_grams(), DELTA);
		assertEquals(13, bacon.getProteins_per_100_grams(), DELTA);
		assertEquals(302, bacon.getCarbs_per_100_grams(), DELTA);
		assertEquals(30.3, bacon.getFats_per_100_grams(), DELTA);
	}

	@Test
	public void testSetters() {
		final Portion bacon = new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3);
		bacon.setAmount(200);
		bacon.setCalories_per_100_grams(200);
		bacon.setCarbs_per_100_grams(200);
		bacon.setFats_per_100_grams(200);
		bacon.setProteins_per_100_grams(200);

		assertEquals(Type.FOOD, bacon.getType());
		assertEquals("bacon", bacon.getName());
		assertEquals(200, bacon.getAmount(), DELTA);
		assertEquals(200, bacon.getCalories_per_100_grams(), DELTA);
		assertEquals(200, bacon.getProteins_per_100_grams(), DELTA);
		assertEquals(200, bacon.getCarbs_per_100_grams(), DELTA);
		assertEquals(200, bacon.getFats_per_100_grams(), DELTA);
	}

	@Test
	public void testToString() {
		Date time = new Date();
		Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
		calendar.setTime(time); // assigns calendar to given date
		final int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
		assertEquals(
				"Portion name: bacon , 100.0 grams\nPortion type: FOOD\nPortion Meal: "
						+ (hour > 7 && hour < 11 ? Portion.Meal.BREAKFAST
								: (hour >= 11 && hour <= 17 ? Portion.Meal.LUNCH
										: (hour >= 17 && hour <= 22 ? Portion.Meal.DINNER : Portion.Meal.MIDNIGHT)))
												.name()
						+ "\n----------------------------------\n"
						+ "Nutritional Values per 100 grams:\nCalories: 700.0\nProteins: 13.0\n"
						+ "Carbohydrates: 302.0\nFats: 30.3\nAlchohol by volume: 0.0\nTime taken: "
						+ (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(time) + "\nIt felt: " + "",
				new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3, 0, time).toString());
	}

	@Test
	public void testToString2() {
		Date time = new Date();
		Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
		calendar.setTime(time); // assigns calendar to given date
		final int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
		assertEquals(
				"Portion name: fuzetea , 100.0 ml\nPortion type: DRINK\nPortion Meal: "
						+ (hour > 7 && hour < 11 ? Portion.Meal.BREAKFAST
								: (hour >= 11 && hour <= 17 ? Portion.Meal.LUNCH
										: (hour >= 17 && hour <= 22 ? Portion.Meal.DINNER : Portion.Meal.MIDNIGHT)))
												.name()
						+ "\n----------------------------------\n"
						+ "Nutritional Values per 100 grams:\nCalories: 700.0\nProteins: 13.0\n"
						+ "Carbohydrates: 302.0\nFats: 30.3\nAlchohol by volume: 0.0\nTime taken: "
						+ (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(time) + "\nIt felt: " + "",
				new Portion(Type.DRINK, "fuzetea", 100, 700, 13, 302, 30.3, 0, time).toString());
	}

	@Test
	public void testEquals() {
		Date time = new Date();
		assert new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3, 0, time)
				.equals(new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3, 0, time));
		assert !new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3, 0, time)
				.equals(new Portion(Type.FOOD, "bacon", 100, 700, 13, 301, 30.3, 0, time));
		assert !new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3, 0, time)
				.equals(new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.1, 0, time));
		assert !new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3, 0, time)
				.equals(new Portion(Type.FOOD, "bacon", 100, 700, 14, 302, 30.3, 0, time));
		assert !new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3, 0, time)
				.equals(new Portion(Type.FOOD, "bacon", 100, 701, 13, 302, 30.3, 0, time));
		assert !new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3, 0, time)
				.equals(new Portion(Type.FOOD, "bacon", 101, 700, 13, 302, 30.3, 0, time));
		assert !new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3, 0, time)
				.equals(new Portion(Type.FOOD, "baconn", 100, 700, 13, 302, 30.3, 0, time));
		assert !new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3, 0, time)
				.equals(new Portion(Type.DRINK, "bacon", 100, 700, 13, 302, 30.3, 0, time));
		final Portion a = new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3, 0, time);
		assert a.equals(a);
		assert !a.equals(null);
		assert !a.equals(String.valueOf("b"));
	}

}