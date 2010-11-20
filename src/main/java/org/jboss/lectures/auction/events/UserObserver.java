package org.jboss.lectures.auction.events;

import java.util.Properties;
import javax.enterprise.event.Observes;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import org.jboss.lectures.auction.entity.User;


public class UserObserver 
{

	public void userLoggedIn(@Observes @LoggedIn User user)
	{
		System.out.println("User identified by " + user.getEmail() + " logged in"); 
	}
	
	public void newUserRegistered(@Observes @Registered User user)
	{
		Wiser wiser = startWiser();  //start recipient stub
		
		String subject = "User " + user.getEmail() + " registered";

		sendMail(subject);
		
		checkMail(wiser);
		
		wiser.stop(); //stop recipient stub
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
		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
		}
	}
	
	public Wiser startWiser()
	{
		/* Wiser is listening on port 2525 to receive a message (for testing purposes) */
		Wiser wiser = new Wiser(); 
	    wiser.setPort(2525);
	    wiser.start();
	    return wiser;
	}
	
	public void checkMail(Wiser wiser)
	{
		if (wiser.getMessages().isEmpty())
		{
			System.out.println("No message received");
			return;
		}
		
		WiserMessage message = wiser.getMessages().get(0);
	    System.out.println("==========================MESSAGE===========================");
	    System.out.println(new String(message.getData()));
	    System.out.println("============================================================");
	}
	
}
