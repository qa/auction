package org.jboss.lectures.auction;

import javax.ejb.ApplicationException;

import org.jboss.lectures.auction.entity.User;

@ApplicationException(rollback = true)
public class InvalidUserException extends Exception {
	
	private static final long serialVersionUID = -1636025478402191970L;

	public InvalidUserException(User user) {
		super("User '" + user.getEmail() + "' is invalid");
	}
}
