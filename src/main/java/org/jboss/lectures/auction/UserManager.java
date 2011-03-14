package org.jboss.lectures.auction;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
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
	
	@EJB
	MessageNotifier messageNotifier;
	
	public void addUser(User user) throws InvalidUserException {
		em.persist(user);
		registeredEvent.fire(user);
		messageNotifier.notifyNewUser(user);
		if ("exc@mail.cz".equals(user.getEmail())) {
			throw new InvalidUserException(user);
		}
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

}
