package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Calendar;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

public class WeeklyMailReportHandler implements RequestHandler {
	private String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("WeeklyMailReport"));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		// TODO Auto-generated method stub
		return null;
	}

}
