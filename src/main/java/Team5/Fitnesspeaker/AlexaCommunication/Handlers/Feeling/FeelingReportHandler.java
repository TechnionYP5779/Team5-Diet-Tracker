package Team5.Fitnesspeaker.AlexaCommunication.Handlers.Feeling;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.services.Pair;
import static com.amazon.ask.request.Predicates.intentName;
import java.util.ArrayList;
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

	private void getFoodInfo() {
		List<Pair<String, Portion>> foods = new ArrayList<Pair<String, Portion>>();
		try {
			foods = this.db.DBGetTodayFoodList();
		} catch (DBException e1) {
			// e1.printStackTrace();
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
		getFoodInfo();

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
