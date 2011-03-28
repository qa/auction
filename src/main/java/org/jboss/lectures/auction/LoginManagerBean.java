package org.jboss.lectures.auction;

import java.io.Serializable;
import java.security.Principal;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.security.auth.Subject;

import org.jboss.lectures.auction.entity.User;
import org.jboss.lectures.auction.qualifiers.LoggedIn;
import org.jboss.security.AuthenticationManager;
import org.jboss.security.SimplePrincipal;
import org.picketbox.config.PicketBoxConfiguration;
import org.picketbox.factories.SecurityFactory;

@SessionScoped
@Named("loginManager")
@Stateful
public class LoginManagerBean implements Serializable, LoginManager {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	@Inject
	private UserManager userManager;
	
	@Inject
	@LoggedIn
	private Event<User> loggedInEvent;
	
	private User currentUser;

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

	public void login(String email, String password) throws InvalidUserException {

		// perform authentication
		authenticateUser(email, password);
		
		currentUser = userManager.getUserByEmail(email);

		if (currentUser == null) {
			currentUser = new User(email);
			userManager.addUser(currentUser);
			
		}
		loggedInEvent.fire(currentUser);
	}

	public void logout() {
		this.currentUser = null;
	}

	public boolean isLogged() {
		return this.currentUser != null;
	}
	
	private void authenticateUser(String email, String password)
			throws InvalidUserException {
		SecurityFactory.prepare();
		try {
			
			String configFile = "security/auth-conf.xml";
	        PicketBoxConfiguration idtrustConfig = new PicketBoxConfiguration();
	        idtrustConfig.load(configFile);
	
			AuthenticationManager am = SecurityFactory
					.getAuthenticationManager(LoginManager.SECURITY_DOMAIN);
			if (am == null) {
				throw new InvalidUserException("Authentication Manager is null");
			}

			Subject subject = new Subject();
			Principal principal = new SimplePrincipal(email);
			Object credential = password;

			boolean result = am.isValid(principal, credential);
			if (result == false) {
				throw new InvalidUserException("Authentication Failed");
			}
			result = am.isValid(principal, credential, subject);
			if (result == false)
				throw new InvalidUserException("Authentication Failed");

			if (subject.getPrincipals().size() < 1)
				throw new InvalidUserException("Subject has zero principals");
			System.out.println("Authentication Successful");
		} finally {
			SecurityFactory.release();
		}

	}
	
}
