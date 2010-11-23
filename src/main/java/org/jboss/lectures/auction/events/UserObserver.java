package org.jboss.lectures.auction.events;

import java.util.Properties;
import javax.enterprise.event.Observes;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.jboss.lectures.auction.entity.User;
import org.jboss.lectures.auction.qualifiers.LoggedIn;
import org.jboss.lectures.auction.test.MockSmtpServer;


public class UserObserver 
{

	public void userLoggedIn(@Observes @LoggedIn User user)
	{
		System.out.println("User identified by " + user.getEmail() + " logged in"); 
	}
	
	public void newUserRegistered(@Observes @Registered User user, MockSmtpServer smtpServer)
	{
		String subject = "User " + user.getEmail() + " registered";

		sendMail(subject);
		
		smtpServer.checkMail();
	}
	
	public void sendMail(String subject)
	{
		String host = "localhost";
		String from = "info@bay.com";
		String to = "admin@bay.com";

		Properties props = System.getProperties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", Integer.toString(2525));
		
		Session session = Session.getDefaultInstance(props, null);
		
		try 
		{
			// Define message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText("");
			// Send message
			Transport.send(message);
			
			//*** or use real SMTP server (host), username and password *** 
			//message.saveChanges(); // implicit with send()
			//Transport transport = session.getTransport("smtp");
			//transport.connect(host, username, password);
			//transport.sendMessage(message, message.getAllRecipients());
			//transport.close();
		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
		}
	}
}
