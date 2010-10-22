package org.jboss.lectures.auction.model;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.lectures.auction.entity.User;

@SessionScoped
@Named
public class LoginManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserManager userManager;

	private User currentUser;

	@Produces
	@Named
	@Dependent
	public User getCurrentUser() {
		return currentUser;
	}

	public void login(String username) {
		currentUser = userManager.getUserByName(username);

		if (currentUser == null) {
			currentUser = new User(username);
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
