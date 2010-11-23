package org.jboss.lectures.auction.test;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;

import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

@ApplicationScoped
@Singleton
@Startup
public class MockSmtpServer {

	private Wiser server;
	
	@PostConstruct
	public void start()
	{
		server = new Wiser(); 
		server.setPort(2525);
		server.start();
	}
	
	public void checkMail()
	{
		if (server.getMessages().isEmpty())
		{
			System.out.println("No message received");
			return;
		}
		
		for (WiserMessage message : server.getMessages())
		{
			System.out.println("======================RECEIVED E-MAIL=======================");
			System.out.println(new String(message.getData()));
			System.out.println("============================================================");
		}
		server.getMessages().clear();
	}
	
	@PreDestroy
	public void stop()
	{
		server.stop();
	}
}
