package Team5.Fitnesspeaker.AlexaCommunication.Handlers.Feeling;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.services.Pair;
import static com.amazon.ask.request.Predicates.intentName;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Email.EmailSender;
import Utils.Portion.Portion;
import Utils.Strings.SlotString;

public class FeelingReportHandler implements RequestHandler {
	
	String UserMail;
	String UserName;
	List<Portion> foodPortions = new ArrayList<>();
	DBUtils db;

	private void getUserInfo(HandlerInput i) {
		this.UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		this.UserName = i.getServiceClientFactory().getUpsService().getProfileGivenName();
	}

	private void openDatabase() {
		db = new DBUtils(this.UserMail);
	}
	
	private static String[] getDates(int period) {
		String[] dates = new String[period];
		Calendar day = Calendar.getInstance();
		day.add(Calendar.DATE, -period);
		for (int j = 0; j < period; ++j) {
			day.add(Calendar.DATE, 1);
			String[] date = day.getTime().toString().split("\\s+");
			dates[j] = date[2] + "-" + date[1] + "-" + date[5];
		}
		return dates;
	}

	private void getFoodInfo(final String period) {
		int periodNum = 1;
		if (period.equals("weekly"))
			periodNum = 7;
		if (period.equals("monthly"))
			periodNum = 30;
		List<Pair<String, Portion>> foods = new ArrayList<Pair<String, Portion>>();
		for(String date : getDates(periodNum)) {
			try {
				//foods = this.db.DBGetTodayFoodList();
				foods.addAll(this.db.DBGetDateFoodList(date));
			} catch (DBException e1) {
				// e1.printStackTrace();
			}
		}
		foodPortions = foods.stream().map(p -> p.getValue()).collect(Collectors.toList());
	}

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName(Utils.Strings.IntentsNames.FEELINGS_REPORT_INTENT));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		final Slot timeSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.TIME_UNIT_SLOT);
		final String period = timeSlot.getValue();
		getUserInfo(i);
		openDatabase();
		getFoodInfo(period);

		try {
			(new EmailSender()).designedFeelingsEmail("Daily Feelings Report", this.UserMail.replace("_dot_", "."),
					UserName, foodPortions,period);
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return i.getResponseBuilder()
				.withSimpleCard("FitnessSpeakerSession", Utils.Strings.EmailStrings.DAILY_MAIL_SENT)
				.withSpeech(Utils.Strings.EmailStrings.DAILY_MAIL_SENT).withShouldEndSession(Boolean.FALSE).build();
	}

}
