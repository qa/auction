package org.jboss.lectures.auction;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.lectures.auction.entity.User;
import org.jboss.lectures.auction.events.LoggedIn;
import org.jboss.lectures.auction.events.Registered;

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

	@Inject
	@LoggedIn
	private Event<User> loggedInEvent;

	@Inject
	@Registered
	private Event<User> registeredEvent;

	@Produces
	@LoggedIn
	@Named
	public User getCurrentUser() {
		if (currentUser != null && !em.contains(currentUser)) {
			currentUser = em.merge(currentUser);
			em.refresh(currentUser);
		}
		return currentUser;
	}

	public void login(String email) {
		currentUser = userManager.getUserByEmail(email);

		if (currentUser == null) {
			currentUser = new User(email);
			userManager.addUser(currentUser);
			registeredEvent.fire(currentUser); // fire event
		}

		loggedInEvent.fire(currentUser); // fire event
	}

	public void logout() {
		this.currentUser = null;
	}

	public boolean isLogged() {
		return this.currentUser != null;
	}
}
