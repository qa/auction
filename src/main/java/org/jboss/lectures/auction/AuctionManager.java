package org.jboss.lectures.auction;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.lectures.auction.db.DatabaseStub;
import org.jboss.lectures.auction.entity.Auction;
import org.jboss.lectures.auction.entity.Bid;
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
	@Dependent
	@Named
	public Auction getCurrentAuction() {
		return currentAuction;
	}

	public Long getCurrentAuctionId() {
		return (currentAuction == null) ? null : currentAuction.getId();
	}

	public void setCurrentAuctionId(Long currentId) {
		this.currentAuction = database.getAuctionById(currentId);
	}

	public List<Auction> getAll() {
		return database.getAllAuctions();
	}

	public List<Auction> getAuctionsWinningByUser(User user) {
		return database.getAuctionsWinningByUser(user);
	}

	public List<Auction> getAuctionLoosingByUser(User user) {
		return database.getAuctionsLoosingByUser(user);
	}

	public void addAuction(Auction auction) {
		if (!loginManager.isLogged()) {
			throw new IllegalStateException(
					"user must be logged in order to add auction");
		}
		auction.setOwner(loginManager.getCurrentUser());
		currentAuction = database.createAuction(auction);
	}

	public void addBid(long bidAmount) {
		if (!loginManager.isLogged()) {
			throw new IllegalStateException(
					"user must be logged in order to add bid");
		}
		if (currentAuction == null) {
			throw new IllegalStateException(
					"currentAuction have to be selected in order to add bid");
		}
		new Bid(loginManager.getCurrentUser(), currentAuction, bidAmount);
	}
	
	public void addFavorite(User user, Auction auction) {
		database.addFavorite(user, auction);
	}
	
	public void removeFavorite(User user, Auction auction) {
		database.removeFavorite(user, auction);
	}

	@Produces
	@RequestScoped
	@Named("newAuction")
	public Auction createNewAuction() {
		return new Auction();
	}

	public void validateBid(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		long bidAmount = Long.valueOf(value.toString());

		if (currentAuction == null) {
			produceMessageForComponent(context, component,
					"Není zvolena aktuální aukce");
		}

		if (currentAuction.getOriginalPrice() >= bidAmount) {
			produceMessageForComponent(context, component, "Nová nabídka ("
					+ bidAmount + ") musí být vyšší než originální cena ("
					+ currentAuction.getOriginalPrice() + ")");
		}

		if (currentAuction.getHighestBid() == null) {
			return;
		}

		if (currentAuction.getHighestBid().getAmount() >= bidAmount) {
			produceMessageForComponent(context, component, "Nová nabídka ("
					+ bidAmount + ") musí být vyšší než dosavadní nejvyšší nabídka ("
					+ currentAuction.getHighestBid().getAmount() + ")");
		}
	}

	private void produceMessageForComponent(FacesContext context,
			UIComponent component, String message) {
		FacesMessage facesMessage = new FacesMessage(message);

		context.addMessage(component.getId(), facesMessage);

		throw new ValidatorException(facesMessage);
	}
}
