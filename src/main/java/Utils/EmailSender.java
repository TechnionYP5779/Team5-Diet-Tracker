package Utils;

import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
	
	final String username;
	final String password;
	Properties props = new Properties();
	
	private String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	public EmailSender() {
		this.username = "donotreplay.team5.fitnessspeaker@gmail.com";
		this.password = "Team5.FitnessSpeaker";
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
	}

	public void sendMail(final String messegeText, final String subject, final String toMail) {
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
			message.setSubject(subject);
			message.setText(messegeText);

			Transport.send(message);

			// System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void designedEmail(final String subject, final String toMail, final String name, String waterCups,
			List<Portion> foods, String calories, String proteins, String carbs, String fats,String cigarettes) {
		String messegeText = "<head>\r\n" + 
				"<style>\r\n" + 
				"table {\r\n" + 
				"  font-family: arial, sans-serif;\r\n" + 
				"  border-collapse: collapse;\r\n" + 
				"  width: 100%;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"td, th {\r\n" + 
				"  border: 1px solid #dddddd;\r\n" + 
				"  text-align: left;\r\n" + 
				"  padding: 8px;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"tr:nth-child(even) {\r\n" + 
				"  background-color: #dddddd;\r\n" + 
				"}\r\n" + 
				"</style>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"<h3>Date: "+getDate()+"</h3></br>"+
				"<center><h1><u>Daily statistics</u></h1></center>\r\n" + 
				"</br>\r\n" + 
				"Hi "+name+",</br>\r\n" + 
				"This is your current statistics of today:\r\n" + 
				"</br>\r\n" + 
				"</br>\r\n" + 
				"</br>\r\n" + 
				"</br>\r\n" + 
				"\r\n" + 
				"<table>\r\n" + 
				"  <tr>\r\n" + 
				"    <th>Food</th>\r\n" + 
				"    <th>Amount (in grams)</th>\r\n" + 
				"    <th>calories</th>\r\n" + 
				"    <th>proteins</th>\r\n" + 
				"    <th>Carbs</th>\r\n" + 
				"    <th>Fats</th>\r\n" + 
				"  </tr>\r\n" + 
				"  <tr>\r\n";
				for(Portion p : foods) {
					messegeText+="<td>"+p.getName()+"</td>\r\n" + 
							"    <td>"+p.getAmount()+"</td>\r\n" + 
							"    <td>"+String.format("%.2f",p.getCalories_per_100_grams()*(p.getAmount()/100))+"</td>\r\n" + 
							"    <td>"+String.format("%.2f",p.getProteins_per_100_grams()*(p.getAmount()/100))+"</td>\r\n" + 
							"    <td>"+String.format("%.2f",p.getCarbs_per_100_grams()*(p.getAmount()/100))+"</td>\r\n" + 
							"    <td>"+String.format("%.2f",p.getFats_per_100_grams()*(p.getAmount()/100))+"</td>\r\n" + 
							"  </tr>\r\n";
				}  
				messegeText+="  <tr style=\"outline: thin solid\">\r\n" + 
				"  <td  style=\"outline: thin solid\" >Total</td>\r\n" + 
				"  <td  style=\"outline: thin solid\" ></td>\r\n" +
				"  <td  style=\"outline: thin solid\" >"+calories+"</td>\r\n" +
				"  <td  style=\"outline: thin solid\" >"+proteins+"</td>\r\n" +
				"  <td  style=\"outline: thin solid\" >"+carbs+"</td>\r\n" +
				"  <td  style=\"outline: thin solid\" >"+fats+"</td>\r\n" +
				"  </tr>\r\n" + 
				"</table>\r\n" + 
				"</br>\r\n" + 
				"You drank "+waterCups+" cups of water and smoked "+cigarettes+" cigarettes\r\n" + 
				"</br>\r\n" + 
				"</br>\r\n" + 
				"<Div>Have a nice day.</Div>\r\n" + 
				"\r\n" + 
				"</body>";
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
			message.setSubject(subject);
			message.setContent(messegeText, "text/html");
			// message.setText(messegeText);

			Transport.send(message);

			// System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
