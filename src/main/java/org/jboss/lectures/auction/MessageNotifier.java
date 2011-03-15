package org.jboss.lectures.auction;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.jboss.lectures.auction.entity.Auction;
import org.jboss.lectures.auction.entity.User;

@Stateless
public class MessageNotifier implements Serializable {

	private static final long serialVersionUID = 2526805960172770139L;

	@Resource(mappedName = "java:/JmsXA")
	ConnectionFactory jmsConnectionFactory;
	
	@Resource(mappedName = "/queue/UserCreated")
	Destination userCreated;

	@Resource(mappedName = "/topic/AuctionNotification")
	Destination auctionNotification;


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void notifyNewUser(User newUser) {
		try {
			Connection connection = jmsConnectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer sender = session.createProducer(userCreated);
			TextMessage msg = session.createTextMessage();
			msg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			msg.setText("New user " + newUser.getName() + " with email <"
					+ newUser.getEmail() + "> registered");
			sender.send(msg);
			sender.close();
			session.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyBid(User bidder, Auction auction, long bidAmount) {
		try {
			Connection connection = jmsConnectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer sender = session
					.createProducer(auctionNotification);
			ObjectMessage msg = session.createObjectMessage();
			msg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			msg.setObject(bidAmount);
			msg.setStringProperty("user", bidder.getEmail());
			msg.setLongProperty("auctionId", auction.getId());
			sender.send(msg);
			sender.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
