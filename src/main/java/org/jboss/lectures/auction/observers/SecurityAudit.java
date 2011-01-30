package org.jboss.lectures.auction.observers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.lectures.auction.entity.User;
import org.jboss.lectures.auction.qualifiers.LoggedIn;

@ApplicationScoped
public class SecurityAudit {

	public void loggedIn(@Observes @LoggedIn User user)
	{
		System.out.println("User " + user.getEmail() + " logged in.");
	}
	
}
