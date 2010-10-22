package org.jboss.lectures.auction.model;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.lectures.auction.db.DatabaseStub;
import org.jboss.lectures.auction.entity.Auction;
import org.jboss.lectures.auction.entity.User;

@ViewScoped
@Named
public class AuctionManager {

	private Auction currentAuction = null;

	@Inject
	private DatabaseStub database;

	@Inject
	private LoginManager loginManager;

	@Produces
	@RequestScoped
	@Named
	public Auction getCurrentAuction() {
		return currentAuction;
	}

	public Integer getCurrentAuctionId() {
		return (currentAuction == null) ? null : currentAuction.getId();
	}

	public void setCurrentAuctionId(Integer currentId) {
		this.currentAuction = database.getAuctionById(currentId);
	}

	public List<Auction> getAll() {
		return database.getAllAuctions();
	}
	
	public List<Auction> getAuctionsByOwner(User owner) {
		return database.getAuctionsByOwner(owner);
	}

	public void addAuction(Auction auction) {
		if (!loginManager.isLogged()) {
			throw new IllegalStateException(
					"user must be logged in order to add auction");
		}
		auction.setOwner(loginManager.getCurrentUser());
		database.addAuction(auction);
		currentAuction = auction;
	}

	@Produces
	@RequestScoped
	@Named("newAuction")
	public Auction createNewAuction() {
		return new Auction();
	}
}
