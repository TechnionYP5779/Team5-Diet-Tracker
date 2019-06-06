package Team5.Fitnesspeaker.AlexaCommunication.Handlers.Feeling;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.Pair;
import static com.amazon.ask.request.Predicates.intentName;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Email.DailyStatistics;
import Utils.Email.EmailSender;
import Utils.Portion.Portion;

public class FeelingReportHandler implements RequestHandler {
	
	String UserMail;
	String UserName;
	//DailyStatistics dailyStatistics = new DailyStatistics();
	List<Portion> foodPortions = new ArrayList<>();
	DBUtils db;

	private void getUserInfo(HandlerInput i) {
		this.UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		this.UserName = i.getServiceClientFactory().getUpsService().getProfileGivenName();
	}

	private void openDatabase() {
		db = new DBUtils(this.UserMail);
	}

	private void getFoodInfo() {
		List<Pair<String, Portion>> foods = new ArrayList<Pair<String, Portion>>();
		try {
			foods = this.db.DBGetTodayFoodList();
		} catch (DBException e1) {
			// e1.printStackTrace();
		}
		//this.dailyStatistics.foodPortions = foods.stream().map(p -> p.getValue()).collect(Collectors.toList());
		foodPortions = foods.stream().map(p -> p.getValue()).collect(Collectors.toList());
	}

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName(Utils.Strings.IntentsNames.FEELINGS_REPORT_INTENT));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		getUserInfo(i);
		openDatabase();
		getDrinkInfo();
		getFoodInfo();
		getCiggaretsInfo();
		dailyStatistics.calculateDailyNutritions();

		try {
			(new EmailSender()).designedDailyStatisticsEmail("Daily Statistics", this.UserMail.replace("_dot_", "."),
					UserName, dailyStatistics);
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return i.getResponseBuilder()
				.withSimpleCard("FitnessSpeakerSession", Utils.Strings.EmailStrings.DAILY_MAIL_SENT)
				.withSpeech(Utils.Strings.EmailStrings.DAILY_MAIL_SENT).withShouldEndSession(Boolean.FALSE).build();
	}

}
