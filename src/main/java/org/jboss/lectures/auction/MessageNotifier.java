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
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.jboss.lectures.auction.entity.User;

@Stateless
public class MessageNotifier implements Serializable {

	private static final long serialVersionUID = 2526805960172770139L;

	@Resource(mappedName = "java:/JmsXA")
	ConnectionFactory jmsConnectionFactory;
	@Resource(mappedName = "/queue/UserCreated")
	Destination userCreated;

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

}
