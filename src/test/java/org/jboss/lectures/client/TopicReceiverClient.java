package org.jboss.lectures.client;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class TopicReceiverClient {

	public static void main(String[] args) {
		try {
			String filter = null;
			if (args.length > 0) {
				filter = args[0];
			}
			InitialContext ctx = new InitialContext();
			ConnectionFactory connectionFactory = (ConnectionFactory) ctx
					.lookup("/ConnectionFactory");
			Connection connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Topic topic = (Topic) ctx.lookup("/topic/AuctionNotification");
			MessageConsumer consumer = session.createConsumer(topic, filter);
			while (true) {
				ObjectMessage message = (ObjectMessage) consumer.receive();
				System.out.println("Bid " + message.getObject() + " by user "
						+ message.getStringProperty("user") + " on auction "
						+ message.getLongProperty("auctionId"));
			}
//			consumer.close();
//			session.close();
//			connection.stop();
//			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
