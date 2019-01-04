package Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;

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
	
	@Test
	public void designedWeeklyEmailTest() {
		WeeklyStatistics w=new WeeklyStatistics();
		DailyStatistics ds=new DailyStatistics();
		w.dailyStatistics.add(ds);
		w.dailyStatistics.add(ds);
		w.dailyStatistics.add(ds);
		w.dailyStatistics.add(ds);
		w.dailyStatistics.add(ds);
		w.dailyStatistics.add(ds);
		w.dailyStatistics.add(ds);
		new EmailSender().designedWeeklyStatisticsEmail("test3", "team5.yearlyproject@gmail.com", "Team", w);	
	}
	
	@Test
	public void sendWeightStatistics() {
		
		ArrayList<Calendar> dates=new ArrayList();
		ArrayList<Integer> weights=new ArrayList();
		
		for(int i=0;i<7;i++) {
			Calendar c=Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -i);
			dates.add(c);
			weights.add(70-i);
		}
		
		
			try {
				new EmailSender().sendWeightStatistics("test4", "team5.yearlyproject@gmail.com", "team", dates, weights);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
