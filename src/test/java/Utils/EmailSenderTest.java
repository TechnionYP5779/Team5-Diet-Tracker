package Utils;

import org.junit.Test;
import Utils.EmailSender;

public class EmailSenderTest {

	// checking there are no exceptions and that the mail is sent
	@Test
	@SuppressWarnings("static-method")
	public void testMailSend() {
		(new EmailSender()).sendMail(String.format("You ate %s.", "pasta and potatoes"), "test subject",
				"donotreplay.team5.fitnessspeaker@gmail.com");
	}
	@Test
	public void designedEmail() {
		new EmailSender().designedEmail("test", "ororfeldman4@gmail.com",null,null,null, null, null, null, null);
	}
	
}
