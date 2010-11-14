package org.jboss.lectures.auction;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.lectures.auction.entity.User;

@Dependent
@Named
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public void addUser(User user) {
		em.persist(user);
	}

	public User getUserByEmail(String email) {
		// return database.findUserByEmail(email);
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
