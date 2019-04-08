package Team5.Fitnesspeaker.AlexaCommunication.Handlers.EmailHandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.EmailSender;
public class WeightProgressIntentHandler implements RequestHandler {

	String UserMail;
	String UserName;
	private final int MAX_DAYS=14;

	private String[] getDates() {
		String[] dates = new String[MAX_DAYS];
		Calendar weekDay = Calendar.getInstance();
		weekDay.add(Calendar.DATE, -MAX_DAYS);
		for (int j = 0; j < MAX_DAYS; ++j) {
			weekDay.add(Calendar.DATE, 1);
			String[] date = weekDay.getTime().toString().split("\\s+");
			dates[j] = date[2] + "-" + date[1] + "-" + date[5];
		}
		return dates;
	}

	private void getUserInfo(HandlerInput i) {
		this.UserMail = i.getServiceClientFactory().getUpsService().getProfileEmail();
		this.UserName = i.getServiceClientFactory().getUpsService().getProfileGivenName();
	}

	private int getWeightByDate(String date,DBUtils u) {
		
		try {
			return (int) u.DBGetDateDailyInfo(date).getWeight();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			return -1;
		}
	}
		

	@Override
	public boolean canHandle(HandlerInput i) {
		return i.matches(intentName("weightProgressIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput i) {
		ArrayList<Calendar> dts=new ArrayList<>();
		ArrayList<Integer> wts=new ArrayList<>();
		String s="";
		try {
		getUserInfo(i);
		DBUtils db=new DBUtils(UserMail);
		String[] dates = getDates();
		
		int w=-1;
		for (int day = 0; day < MAX_DAYS; ++day) {
			w=getWeightByDate(dates[day],db);
			if(w!=-1) {
				wts.add(Integer.valueOf(w));
				Calendar c=Calendar.getInstance();
				c.add(Calendar.DAY_OF_YEAR, -MAX_DAYS+day);
				dts.add(c);
			}
			
		}
		try {
			if( wts.size()>=5) 
				(new EmailSender()).sendWeightStatistics("weight progess", this.UserMail, UserName, dts, wts);
		} catch (Exception e) {
			//s+=e.toString();
		}
		} catch(Exception e) {
			//s+=" "+e.toString();
		}
		//s+=" "+String.valueOf(dts.size())+" "+String.valueOf(wts.size());
		if( wts.size()>=5)
			return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", Utils.Strings.EmailStrings.WEIGHT_MAIL_SENT)
				.withSpeech(Utils.Strings.EmailStrings.WEIGHT_MAIL_SENT).withShouldEndSession(Boolean.FALSE).build();
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", "I don't have enough measurements"+s)
				.withSpeech(Utils.Strings.EmailStrings.WEIGHT_MAIL_NOT_SENT).withShouldEndSession(Boolean.FALSE).build();
	}

}
