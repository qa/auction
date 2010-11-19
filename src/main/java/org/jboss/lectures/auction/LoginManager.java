package org.jboss.lectures.auction;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.lectures.auction.entity.User;

@SessionScoped
@Named
@Stateful
public class LoginManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;
	
	@Inject
	private UserManager userManager;
	
	private User currentUser;

	@Produces
	@Named
	public User getCurrentUser() {
		if (currentUser != null && !em.contains(currentUser)) {
			currentUser = em.merge(currentUser);
		}
		return currentUser;
	}
	
	public void login(String email) {
		currentUser = userManager.getUserByEmail(email);

		if (currentUser == null) {
			currentUser = new User(email);
			userManager.addUser(currentUser);
		}
	}

	public void logout() {
		this.currentUser = null;
	}

	public boolean isLogged() {
		return this.currentUser != null;
	}
}
