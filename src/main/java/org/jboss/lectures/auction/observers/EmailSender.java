package org.jboss.lectures.auction.observers;

import java.util.Properties;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.lectures.auction.entity.User;
import org.jboss.lectures.auction.qualifiers.Registered;
import org.jboss.lectures.auction.test.MockSmtpServer;

public class EmailSender {

	@Inject
	private MockSmtpServer smtpServer;
	
	public void sendEmail(@Observes @Registered User user)
	{
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "localhost");
		properties.put("mail.smtp.port", "2525");
		Session session = Session.getDefaultInstance(properties, null);
		
		MimeMessage message = new MimeMessage(session);
		try
		{
			message.setFrom(new InternetAddress("noreply@aukce.redhat.com"));
			message.addRecipient(RecipientType.TO, new InternetAddress(user.getEmail()));
			message.setSubject("Vitejte na aukce.redhat.com");
			message.setText("Bla bla");
			Transport.send(message);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		smtpServer.checkMail();
	}
	
}
