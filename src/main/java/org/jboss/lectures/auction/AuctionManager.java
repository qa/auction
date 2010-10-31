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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


import org.jboss.lectures.auction.db.DatabaseStub;
import org.jboss.lectures.auction.entity.Auction;
import org.jboss.lectures.auction.entity.Bid;
import org.jboss.lectures.auction.entity.User;



@ViewScoped
@Named
public class AuctionManager {

	private Auction currentAuction = null;

	//@Inject
	//private DatabaseStub database;
	
	@PersistenceContext
	public EntityManager em;

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
		//this.currentAuction = database.getAuctionById(currentId);
		this.currentAuction = em.find(Auction.class, currentId);
	}
	
	public List<Auction> getAll() {
		//return database.getAllAuctions();
		return em.createQuery("SELECT a FROM Auction a", Auction.class).getResultList();
	}

	public List<Auction> getAuctionsWinningByUser(User user) {
		//return database.getAuctionsWinningByUser(user);
		
		//String jql = "FROM Auction a RIGHT JOIN Bid b RIGHT JOIN User u WHERE u = :user";
		//String jql = "SELECT a FROM User u LEFT JOIN u.bids LEFT JOIN Auction a WHERE u = :user";
		//String jql = "SELECT u.bids.auction FROM User u WHERE u = :user";
		String jql = "SELECT b2.auction FROM Bid b2 WHERE b2.auction IN " +
						"(SELECT u.bids.auction FROM User u WHERE u = :user) " +
						"GROUP BY b2.auction ORDER BY b2.amount DESC";
		TypedQuery<Auction> query = em.createQuery( jql, Auction.class);
		query.setParameter("user", user);
		List<Auction> auctions = query.getResultList();
		return auctions;
	}

	public List<Auction> getAuctionLoosingByUser(User user) {
		String jql = "SELECT b2.auction FROM Bid b2 WHERE b2.auction IN " +
						"(SELECT u.bids.auction FROM User u WHERE u = :user) " +
						"GROUP BY b2.auction ORDER BY b2.amount DESC";
		TypedQuery<Auction> query = em.createQuery( jql, Auction.class);
		query.setParameter("user", user);
		List<Auction> auctions = query.getResultList();
		return auctions;
	}

	public void addAuction(Auction auction) {
		if (!loginManager.isLogged()) {
			throw new IllegalStateException(
					"user must be logged in order to add auction");
		}
		auction.setOwner(loginManager.getCurrentUser());
		
		//currentAuction = database.createAuction(auction);
		em.persist(auction);
		em.flush();
		em.refresh(auction);
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
		
		Bid bid = new Bid(loginManager.getCurrentUser(), currentAuction, bidAmount);
		em.persist(bid);
		em.flush();
		em.refresh(bid);
	}
	
	public void addFavorite(User user, Auction auction) {
		//database.addFavorite(user, auction);
		if( em.contains(user))
			user = em.merge(user);
		user.getFavorites().add(auction);
		user = em.merge(user);
	}
	
	public void removeFavorite(User user, Auction auction) {
		//database.removeFavorite(user, auction);
		if( em.contains(user))
			user = em.merge(user);
		user.getFavorites().remove(auction);
		user = em.merge(user);
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
