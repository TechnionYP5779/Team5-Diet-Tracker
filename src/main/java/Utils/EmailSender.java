package Utils;

import java.util.ArrayList;
import java.util.Calendar;
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

import GraphsMaker.simpleGraph;
public class EmailSender {

	final String username;
	final String password;
	Properties props = new Properties();

	private String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}

	public EmailSender() {
		this.username = "donotreplay.nutracker@gmail.com";
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

	public void designedDailyStatisticsEmail(final String subject, final String toMail, final String name,
			DailyStatistics s) {
		String messegeText = "<head>\r\n<style>\r\ntable {\r\n  font-family: arial, sans-serif;\r\n"
				+ "  border-collapse: collapse;\r\n  width: 100%;\r\n}\r\n\r\ntd, th {\r\n"
				+ "  border: 1px solid #dddddd;\r\n  text-align: left;\r\n  padding: 8px;\r\n}\r\n"
				+ "\r\ntr:nth-child(even) {\r\n  background-color: #dddddd;\r\n}\r\n</style>\r\n"
				+ "</head>\r\n<body>\r\n<h3>Date: " + getDate() + "</h3></br>"
				+ "<center><h1><u>Daily statistics</u></h1></center>\r\n</br>\r\nHi " + name + ",</br>\r\n"
				+ "This is your current statistics of today:\r\n</br>\r\n</br>\r\n</br>\r\n"
				+ "</br>\r\n\r\n<table>\r\n  <tr>\r\n    <th>Food</th>\r\n"
				+ "    <th>Amount (in grams)</th>\r\n    <th>calories</th>\r\n    <th>proteins</th>\r\n"
				+ "    <th>Carbs</th>\r\n    <th>Fats</th>\r\n  </tr>\r\n  <tr>\r\n";
		for (Portion p : s.foodPortions)
			messegeText += "<td>" + p.getName() + "</td>\r\n    <td>" + p.getAmount() + "</td>\r\n    <td>"
					+ String.format("%.2f", p.getCalories_per_100_grams() * (p.getAmount() / 100)) + "</td>\r\n"
					+ "    <td>" + String.format("%.2f", p.getProteins_per_100_grams() * (p.getAmount() / 100))
					+ "</td>\r\n    <td>" + String.format("%.2f", p.getCarbs_per_100_grams() * (p.getAmount() / 100))
					+ "</td>\r\n    <td>" + String.format("%.2f", p.getFats_per_100_grams() * (p.getAmount() / 100))
					+ "</td>\r\n  </tr>\r\n";
		messegeText += "  <tr style=\"outline: thin solid\">\r\n  <td  style=\"outline: thin solid\" >Total</td>\r\n"
				+ "  <td  style=\"outline: thin solid\" ></td>\r\n  <td  style=\"outline: thin solid\" >"
				+ String.format("%.2f", Double.valueOf(s.dailyCalories)) + "</td>\r\n  <td  style=\"outline: thin solid\" >"
				+ String.format("%.2f", Double.valueOf(s.dailyProteins)) + "</td>\r\n  <td  style=\"outline: thin solid\" >"
				+ String.format("%.2f", Double.valueOf(s.dailyCarbs)) + "</td>\r\n  <td  style=\"outline: thin solid\" >"
				+ String.format("%.2f", Double.valueOf(s.dailyFats)) + "</td>\r\n  </tr>\r\n</table>\r\n</br>\r\nYou drank "
				+ s.cupsOfWater + " cups of water and smoked " + s.ciggaretesSmoked + " cigarettes\r\n"
				+ "</br>\r\n</br>\r\n<Div>Have a nice day.</Div>\r\n\r\n</body>";
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
	
	
	private String[] getDates() {
		String[] dates = new String[7];
		Calendar weekDay = Calendar.getInstance();
		weekDay.add(Calendar.DATE, -7);
		for (int j = 0; j < 7; ++j) {
			weekDay.add(Calendar.DATE, 1);
			String[] date = weekDay.getTime().toString().split("\\s+");
			dates[j] = date[2] + "-" + date[1] + "-" + date[5];
		}
		return dates;
	}
	
	public void designedWeeklyStatisticsEmail(final String subject, final String toMail, final String name,
			WeeklyStatistics s) {
		String[] dates=getDates();
		s.calculateWeeklyData();
		String messegeText = "<head>\r\n<style>\r\ntable {\r\n  font-family: arial, sans-serif;\r\n"
				+ "  border-collapse: collapse;\r\n  width: 100%;\r\n}\r\n\r\ntd, th {\r\n"
				+ "  border: 1px solid #dddddd;\r\n  text-align: left;\r\n  padding: 8px;\r\n}\r\n"
				+ "\r\ntr:nth-child(even) {\r\n  background-color: #dddddd;\r\n}\r\n</style>\r\n"
				+ "</head>\r\n<body>\r\n<h3>Date: " + getDate() + "</h3></br>"
				+ "<center><h1><u>Weekly statistics</u></h1></center>\r\n</br>\r\nHi " + name + ",</br>\r\n"
				+ "This is your current statistics of the past seven days:\r\n</br>\r\n</br>\r\n</br>\r\n"
				+ "</br>\r\n\r\n<table>\r\n  <tr>\r\n    <th>Date</th>\r\n"
				+ "<th>calories</th>\r\n    <th>proteins</th>\r\n"
				+ "    <th>Carbs</th>\r\n    <th>Fats</th>\r\n  </tr>\r\n ";
		int dayIndex=0;
		for (DailyStatistics ds : s.dailyStatistics)
			messegeText += "<tr><td>" + dates[dayIndex++] + "</td>\r\n   <td>"
					+ String.format("%.2f", Double.valueOf(ds.dailyCalories)) + "</td>\r\n    <td>"
					+ String.format("%.2f", Double.valueOf(ds.dailyProteins)) + "</td>\r\n    <td>"
					+ String.format("%.2f", Double.valueOf(ds.dailyCarbs)) + "</td>\r\n    <td>"
					+ String.format("%.2f", Double.valueOf(ds.dailyFats)) + "</td>\r\n  </tr>\r\n";
		messegeText += "  <tr style=\"outline: thin solid\">\r\n  <td  style=\"outline: thin solid\" >Total</td>\r\n"
				+ String.format("%.2f", Double.valueOf(s.weeklyCalories)) + "</td>\r\n  <td  style=\"outline: thin solid\" >"
				+ String.format("%.2f", Double.valueOf(s.weeklyProteins)) + "</td>\r\n  <td  style=\"outline: thin solid\" >"
				+ String.format("%.2f", Double.valueOf(s.weeklyCarbs)) + "</td>\r\n  <td  style=\"outline: thin solid\" >"
				+ String.format("%.2f", Double.valueOf(s.weeklyFats)) + "</td>\r\n  </tr>\r\n</table>\r\n</br>\r\nYou drank "
				+ s.weeklyWaterCups + " cups of water and smoked " + s.weeklyCiggaretsSmoked+ " cigarettes\r\n"
				+ "</br>\r\n</br>\r\n<Div>Have a nice day.</Div>\r\n\r\n</body>";
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
	
	public void sendWeightStatistics( final String subject, final String toMail,final String name ,ArrayList<Calendar> dates,ArrayList<Integer> weights) throws MessagingException  {
		simpleGraph g=new simpleGraph();
		String graphName=String.valueOf(toMail).replace(".", "_dot_");
		byte[] byteImage=g.setDates(dates).setWeights(weights).make().save(800, 300, graphName);
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		Message msg = new MimeMessage(session);
	    try {
	        msg.setFrom(new InternetAddress(username));
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
	        msg.setSubject(subject);

	        Multipart multipart = new MimeMultipart();
				MimeBodyPart textBodyPart = new MimeBodyPart();
	        textBodyPart.setText("A graph describing your weight progress is attached.");

	        MimeBodyPart attachmentBodyPart= new MimeBodyPart();
	        ByteArrayDataSource source = new ByteArrayDataSource(byteImage,"image/jpg"); // ex : "C:\\test.pdf"
	        attachmentBodyPart.setDataHandler(new DataHandler(source));
	        attachmentBodyPart.setFileName("weightProgress.jpg"); // ex : "test.pdf"

	        multipart.addBodyPart(textBodyPart);  // add the text part
	        multipart.addBodyPart(attachmentBodyPart); // add the attachement part

	        msg.setContent(multipart);

	        Transport.send(msg);
	    } catch (MessagingException e) {
	        throw e;
	    }
	    
	    //g.delete();
	    
	}
	
}
