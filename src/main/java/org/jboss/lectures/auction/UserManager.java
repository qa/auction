package org.jboss.lectures.auction;

import static javax.ejb.TransactionManagementType.BEAN;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import org.jboss.lectures.auction.entity.AuditUser;
import org.jboss.lectures.auction.entity.User;
import org.jboss.lectures.auction.qualifiers.Registered;

@Named
@Stateless
@TransactionManagement(BEAN)
public class UserManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	@Inject
	@Registered
	private Event<User> registeredEvent;

	@EJB
	MessageNotifier messageNotifier;

	@Resource
	UserTransaction tx;

	public void addUser(User user) throws InvalidUserException {
		try {
			// TX-1
			tx.begin();
			AuditUser audit = new AuditUser(user.getEmail());
			audit.setEventTime(new Timestamp(System.currentTimeMillis()));
			em.persist(audit);
			tx.commit();

			// TX-2
			tx.begin();
			em.persist(user);
			registeredEvent.fire(user);
			messageNotifier.notifyNewUser(user);
			if ("exc@mail.cz".equals(user.getEmail())) {
				tx.rollback();
				throw new InvalidUserException(user);
			}
			tx.commit();

		} catch (InvalidUserException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public User getUserByEmail(String email) {
		try {
			tx.begin();
			String jql = "SELECT u FROM User u WHERE u.email = :email) ";
			TypedQuery<User> query = em.createQuery(jql, User.class);
			query.setParameter("email", email);

			List<User> users = query.getResultList();
			if (!users.isEmpty()) {
				User ret = users.get(0); 
				tx.commit();
				return ret;
			} else {
				tx.commit();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
