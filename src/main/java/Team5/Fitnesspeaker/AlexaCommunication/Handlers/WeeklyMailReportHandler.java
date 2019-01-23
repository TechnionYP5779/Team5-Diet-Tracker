package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.Pair;
import Utils.DBUtils;
import Utils.DailyStatistics;
import Utils.EmailSender;
import Utils.Portion;
import Utils.WeeklyStatistics;
import Utils.DBUtils.DBException;

public class WeeklyMailReportHandler implements RequestHandler {

	String UserMail;
	String UserName;
	WeeklyStatistics weeklyStatistics = new WeeklyStatistics();
	DBUtils db;

	private String[] getDates() {
		String[] dates = new String[7];
		Calendar weekDay = Calendar.getInstance();
		weekDay.add(Calendar.DATE, -7);
		for (int j = 0; j < 7; ++j) {
			weekDay.add(Calendar.DATE, 1);
			String[] date = weekDay.getTime().toString().split("\\s+");
			dates[j] = date[2] + "-" + date[1] + "-" + date[5];
		}
		return dates;
	}

	private void getUserInfo(HandlerInput i) {
		this.UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		this.UserName = i.getServiceClientFactory().getUpsService().getProfileGivenName();
	}

	private void openDatabase() {
		db = new DBUtils(this.UserMail);
	}

	private String getDrinkInfo(String date) {
		Optional<Integer> drinks = Optional.empty();
		try {
			drinks = this.db.DBGetDateWaterCups(date);
		} catch (DBException e1) {
			// e1.printStackTrace();
		}

		return !drinks.isPresent() ? "0" : drinks.get().toString();
	}

	private List<Portion> getFoodInfo(String date) {
		List<Pair<String, Portion>> foods = new ArrayList<Pair<String, Portion>>();
		try {
			foods = this.db.DBGetDateFoodList(date);
		} catch (DBException e1) {
			// e1.printStackTrace();
		}
		return foods.stream().map(p -> p.getValue()).collect(Collectors.toList());
	}

	private String getCiggaretsInfo(String date) {
		Optional<Integer> cigs = Optional.empty();
		try {
			cigs = this.db.DBGetDateCigarettesCount(date);
		} catch (DBException e1) {
			// e1.printStackTrace();
		}

		return !cigs.isPresent() ? "0" : cigs.get().toString();
	}

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("WeeklyMailReport"));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		getUserInfo(i);
		openDatabase();
		String[] dates = getDates();
		for (int day = 0; day < 7; ++day) {
			DailyStatistics dailyStatistics = new DailyStatistics();
			dailyStatistics.cupsOfWater = getDrinkInfo(dates[day]);
			dailyStatistics.foodPortions = getFoodInfo(dates[day]);
			dailyStatistics.ciggaretesSmoked = getCiggaretsInfo(dates[day]);
			dailyStatistics.calculateDailyNutritions();
			this.weeklyStatistics.dailyStatistics.add(dailyStatistics);
		}
		this.weeklyStatistics.calculateWeeklyData();
		try {
			(new EmailSender()).designedWeeklyStatisticsEmail("Weekly Statistics", this.UserMail.replace("_dot_", "."),
					UserName, this.weeklyStatistics);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		weeklyStatistics = new WeeklyStatistics();
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", "Mail with a weekly report has been sent")
				.withSpeech("Mail with a weekly report has been sent ").withShouldEndSession(Boolean.FALSE).build();
	}

}
