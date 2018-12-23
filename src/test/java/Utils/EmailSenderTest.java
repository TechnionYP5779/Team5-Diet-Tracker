package Utils;

import org.junit.Test;
import Utils.EmailSender;

public class EmailSenderTest {

	// checking there are no exceptions and that the mail is sent
	@Test
	@SuppressWarnings("static-method")
	public void testMailSend() {
		(new EmailSender()).sendMail("Test", "test subject", "donotreplay.team5.fitnessspeaker@gmail.com");
	}
}
