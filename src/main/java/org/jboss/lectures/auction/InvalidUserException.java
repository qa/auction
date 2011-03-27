package org.jboss.lectures.auction;

import javax.ejb.ApplicationException;

import org.jboss.lectures.auction.entity.User;

@ApplicationException(rollback = true)
public class InvalidUserException extends Exception {
	
	public InvalidUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidUserException(String message) {
		super(message);
	}

	private static final long serialVersionUID = -1636025478402191970L;

	public InvalidUserException(Throwable cause) {
		super(cause);
	}

	public InvalidUserException(User user) {
		super("User '" + user.getEmail() + "' is invalid");
	}
}
