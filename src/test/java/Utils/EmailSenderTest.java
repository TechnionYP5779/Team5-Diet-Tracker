package Utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import Utils.EmailSender;

public class EmailSenderTest {

	// checking there are no exceptions and that the mail is sent
	@Test
	public void testMailSend() {
		(new EmailSender()).sendMail(String.format("You ate %s.", "pasta and potatoes"), "test subject",
				"donotreplay.team5.fitnessspeaker@gmail.com");
	}

	@Test
	public void designedEmail() {
		List<Portion> ps = new ArrayList<>();
		Portion p = new Portion();
		p.setAmount(50);
		p.setCalories_per_100_grams(100);
		p.setCarbs_per_100_grams(100);
		p.setFats_per_100_grams(100);
		p.setProteins_per_100_grams(100);
		ps.add(p);
		DailyStatistics ds = new DailyStatistics();
		ds.foodPortions = ps;
		ds.dailyCalories = "50.0000001";
		ds.dailyCarbs = "50.0000001";
		ds.dailyProteins = "50.0000001";
		ds.dailyFats = "50.0000001";
		new EmailSender().designedDailyStatisticsEmail("test2", "team5.yearlyproject@gmail.com", "Team", ds);
	}

}
