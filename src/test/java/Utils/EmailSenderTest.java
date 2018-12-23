package Utils;

import org.junit.Test;
import Utils.EmailSender;

public class EmailSenderTest {

	// checking there are no exceptions and that the mail is sent
	@Test
	@SuppressWarnings("static-method")
	public void testMailSend() {
		String foods_eaten = "pasta and potatoes";
		String speechText = String.format("You ate %s.", foods_eaten);
		(new EmailSender()).sendMail(speechText, "test subject", "donotreplay.team5.fitnessspeaker@gmail.com");
	}
}
