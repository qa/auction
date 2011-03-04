package org.jboss.lectures.auction;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.lectures.auction.entity.User;
import org.jboss.lectures.auction.qualifiers.Registered;

@Named
@Stateless
public class UserManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;
	
	@Inject
	@Registered
	private Event<User> registeredEvent;
	
    @Resource(mappedName = "/ConnectionFactory")
    ConnectionFactory jmsConnectionFactory;

    @Resource(mappedName = "/queue/UserCreated")
    Destination userCreated;


	public void addUser(User user) {
		em.persist(user);
		registeredEvent.fire(user);
		notifyNewUser(user);
	}

	public User getUserByEmail(String email) {
		String jql = "SELECT u FROM User u WHERE u.email = :email) ";
		TypedQuery<User> query = em.createQuery(jql, User.class);
		query.setParameter("email", email);
		
		List<User> users = query.getResultList();

		if (!users.isEmpty()) {
			return users.get(0);
		} else {
			return null;
		}
	}
	
    private void notifyNewUser(User newUser) {
        try {
                Connection connection = jmsConnectionFactory.createConnection();
                connection.start();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer sender = session.createProducer(userCreated);
                TextMessage msg = session.createTextMessage();
                msg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
                msg.setText("New user " + newUser.getName() + " with email <" + newUser.getEmail() + "> registered");
                sender.send(msg);
                sender.close();
                session.close();
                connection.stop();
                connection.close();
        }
        catch (JMSException e) {
                e.printStackTrace();
        }
}

}
