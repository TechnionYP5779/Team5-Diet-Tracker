/** This class is allows to send designed email using existing "do not reply" mail address.
 * @author Or Feldman & Igor Bashmashnikov
 * @since 2018-12-21*/
package Utils.Email;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import Utils.Portion.Portion;

public class EmailSender {

	final String username;
	final String password;
	Properties props = new Properties();

	private final String dailyEmailTop = "<head>\r\n<style>\r\ntable {\r\n  font-family: arial, sans-serif;\r\n"
			+ "  border-collapse: collapse;\r\n  width: 100%s;\r\n}\r\n\r\ntd, th {\r\n"
			+ "  border: 1px solid #dddddd;\r\n  text-align: left;\r\n  padding: 8px;\r\n}\r\n"
			+ "\r\ntr:nth-child(even) {\r\n  background-color: #dddddd;\r\n}\r\n</style>\r\n"
			+ "</head>\r\n<body>\r\n<h3>Date: %s </h3></br>"
			+ "<center><h1><u>Daily statistics</u></h1></center>\r\n</br>\r\nHi %s ,</br>\r\n"
			+ "This is your current statistics of today:\r\n</br>\r\n</br>\r\n</br>\r\n"
			+ "</br>\r\n\r\n<table>\r\n  <tr>\r\n    <th>Food</th>\r\n"
			+ "    <th>Amount (in grams)</th>\r\n    <th>calories</th>\r\n    <th>proteins</th>\r\n"
			+ "    <th>Carbs</th>\r\n    <th>Fats</th>\r\n  </tr>\r\n  <tr>\r\n";

	private final String dailyEmailTableLine = "<td> %s </td>\r\n    <td> %.2f </td>\r\n    <td>%.2f </td>\r\n"
			+ "    <td> %.2f</td>\r\n    <td> %.2f</td>\r\n    <td> %.2f</td>\r\n  </tr>\r\n";

	private final String dailyEmailBottom = "  <tr style=\"outline: thin solid\">\r\n  <td  style=\"outline: thin solid\" >Total</td>\r\n"
			+ "  <td  style=\"outline: thin solid\" ></td>\r\n  <td  style=\"outline: thin solid\" >"
			+ "%.2f</td>\r\n  <td  style=\"outline: thin solid\" >"
			+ "%.2f </td>\r\n  <td  style=\"outline: thin solid\" >"
			+ "%.2f </td>\r\n  <td  style=\"outline: thin solid\" >"
			+ "%.2f </td>\r\n  </tr>\r\n</table>\r\n</br>\r\nYou drank "
			+ "%s cups of water and smoked %s cigarettes\r\n"
			+ "</br>\r\n</br>\r\n<Div>Have a nice day.</Div>\r\n\r\n</body>";

	private final String weeklyEmailTop = "<head>\r\n<style>\r\ntable {\r\n  font-family: arial, sans-serif;\r\n"
			+ "  border-collapse: collapse;\r\n  width: 100%s;\r\n}\r\n\r\ntd, th {\r\n"
			+ "  border: 1px solid #dddddd;\r\n  text-align: left;\r\n  padding: 8px;\r\n}\r\n"
			+ "\r\ntr:nth-child(even) {\r\n  background-color: #dddddd;\r\n}\r\n</style>\r\n"
			+ "</head>\r\n<body>\r\n<h3>Date: %s </h3></br>"
			+ "<center><h1><u>Weekly statistics</u></h1></center>\r\n</br>\r\nHi %s ,</br>\r\n"
			+ "This is your current statistics of the past seven days:\r\n</br>\r\n</br>\r\n</br>\r\n"
			+ "</br>\r\n\r\n<table>\r\n  <tr>\r\n    <th>Date</th>\r\n"
			+ "<th>calories</th>\r\n    <th>proteins</th>\r\n"
			+ "    <th>Carbs</th>\r\n    <th>Fats</th>\r\n  </tr>\r\n ";

	private final String weeklyEmailTableLine = "<tr><td> %s </td>\r\n   <td>%.2f </td>\r\n    <td>"
			+ "%.2f </td>\r\n    <td>%.2f  </td>\r\n    <td>%.2f </td>\r\n  </tr>\r\n";

	private final String weeklyEmailBottom = "  <tr style=\"outline: thin solid\">\r\n  <td  style=\"outline: thin solid\" >Total</td>\r\n"
			+ "%.2f </td>\r\n  <td  style=\"outline: thin solid\" >"
			+ "%.2f </td>\r\n  <td  style=\"outline: thin solid\" >"
			+ "%.2f </td>\r\n  <td  style=\"outline: thin solid\" >"
			+ "%.2f </td>\r\n  </tr>\r\n</table>\r\n</br>\r\nYou drank "
			+ "%s cups of water and smoked %s cigarettes\r\n"
			+ "</br>\r\n</br>\r\n<Div>Have a nice day.</Div>\r\n\r\n</body>";

	private String getDate() {
		final String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	public EmailSender() {
		this.username = "donotreplay.diettracker@gmail.com";
		this.password = "RwfBaNzbbCT69Ax";
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
	}

	public void sendMail(final String messegeText, final String subject, final String toMail) {
		final Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			final Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
			message.setSubject(subject);
			message.setText(messegeText);

			Transport.send(message);
		} catch (final MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void designedDailyStatisticsEmail(final String subject, final String toMail, final String name,
			final DailyStatistics s) {
		String messegeText = String.format(dailyEmailTop, "%", getDate(), name);
		for (final Portion p : s.foodPortions)
			messegeText += String.format(dailyEmailTableLine, p.getName(), p.getAmount(),
					p.getCalories_per_100_grams() * (p.getAmount() / 100),
					p.getProteins_per_100_grams() * (p.getAmount() / 100),
					p.getCarbs_per_100_grams() * (p.getAmount() / 100),
					p.getFats_per_100_grams() * (p.getAmount() / 100));
		messegeText += String.format(dailyEmailBottom, Double.valueOf(s.dailyCalories), Double.valueOf(s.dailyProteins),
				Double.valueOf(s.dailyCarbs), Double.valueOf(s.dailyFats), s.cupsOfWater, s.ciggaretesSmoked);
		final Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			final Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
			message.setSubject(subject);
			message.setContent(messegeText, "text/html");

			Transport.send(message);

		} catch (final MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private String[] getDates() {
		final String[] dates = new String[7];
		final Calendar weekDay = Calendar.getInstance();
		weekDay.add(Calendar.DATE, -7);
		for (int j = 0; j < 7; ++j) {
			weekDay.add(Calendar.DATE, 1);
			final String[] date = weekDay.getTime().toString().split("\\s+");
			dates[j] = date[2] + "-" + date[1] + "-" + date[5];
		}
		return dates;
	}

	public void designedWeeklyStatisticsEmail(final String subject, final String toMail, final String name,
			final WeeklyStatistics s) {
		final String[] dates = getDates();
		s.calculateWeeklyData();
		String messegeText = String.format(weeklyEmailTop, "%", getDate(), name);
		int dayIndex = 0;
		for (final DailyStatistics ds : s.dailyStatistics)
			messegeText += String.format(weeklyEmailTableLine, dates[dayIndex++], Double.valueOf(ds.dailyCalories),
					Double.valueOf(ds.dailyProteins), Double.valueOf(ds.dailyCarbs), Double.valueOf(ds.dailyFats));
		messegeText += String.format(weeklyEmailBottom, Double.valueOf(s.weeklyCalories),
				Double.valueOf(s.weeklyProteins), Double.valueOf(s.weeklyCarbs),
				Double.valueOf(s.weeklyFats), s.weeklyWaterCups, s.weeklyCiggaretsSmoked);
		final Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			final Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
			message.setSubject(subject);
			message.setContent(messegeText, "text/html");

			Transport.send(message);

		} catch (final MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendWeightStatistics(final String subject, final String toMail, final String name,
			final ArrayList<Calendar> dates, final ArrayList<Integer> weights) throws MessagingException {
		final simpleGraph g = new simpleGraph();
		final String graphName = String.valueOf(toMail).replace(".", "_dot_");
		final byte[] byteImage = g.setDates(dates).setWeights(weights).make().save(800, 300, graphName);
		final Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		final Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(username));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
			msg.setSubject(subject);

			final Multipart multipart = new MimeMultipart();
			final MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText("A graph describing your weight progress is attached.");

			final MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			final ByteArrayDataSource source = new ByteArrayDataSource(byteImage, "image/jpg");
			attachmentBodyPart.setDataHandler(new DataHandler(source));
			attachmentBodyPart.setFileName("weightProgress.jpg");

			multipart.addBodyPart(textBodyPart); // add the text part
			multipart.addBodyPart(attachmentBodyPart); // add the attachement part

			msg.setContent(multipart);

			Transport.send(msg);
		} catch (final MessagingException e) {
			throw e;
		}
	}
	
	/**
	 * from now will be the implementation of feeling reports
	 */
	
	public void designedDailyFeelingsEmail(final String subject, final String toMail, final String name,
			final List<Portion> f) {
		String messegeText = String.format(dailyEmailTop, "%", getDate(), name);
		for (final Portion p : f)
			messegeText += String.format(dailyEmailTableLine, p.getName(), p.getAmount(),
					p.getCalories_per_100_grams() * (p.getAmount() / 100),
					p.getProteins_per_100_grams() * (p.getAmount() / 100),
					p.getCarbs_per_100_grams() * (p.getAmount() / 100),
					p.getFats_per_100_grams() * (p.getAmount() / 100));
		messegeText += String.format(dailyEmailBottom, 0.0, 0.0,
				0.0, 0.0, 0, 0);
		final Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			final Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
			message.setSubject(subject);
			message.setContent(messegeText, "text/html");

			Transport.send(message);

		} catch (final MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
