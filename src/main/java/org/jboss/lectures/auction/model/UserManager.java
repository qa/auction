package org.jboss.lectures.auction.model;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.lectures.auction.db.DatabaseStub;
import org.jboss.lectures.auction.entity.User;

@RequestScoped
@Named
public class UserManager {

	@Inject
	private DatabaseStub database;

	public void addUser(User user) {
		database.addUser(user);
	}

	public User getUserByName(String name) {
		return database.findUserByName(name);
	}
}
