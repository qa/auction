package org.jboss.lectures.auction;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.TransactionManager;

import org.jboss.lectures.auction.entity.Auction;
import org.jboss.lectures.auction.entity.Bid;
import org.jboss.lectures.auction.entity.User;
import org.jboss.lectures.auction.qualifiers.CurrentAuction;

@ViewScoped
@Named("auctionManager")
@Stateful
@DeclareRoles({"auctionUser","auctionAdmin"})
public class AuctionManagerBean implements Serializable, AuctionManager {

	private static final long serialVersionUID = 1L;

	private Auction currentAuction = null;

	@Inject
	private EntityManager em;

	@Inject
	private LoginManager loginManagerBean;

	@EJB
	MessageNotifier messageNotifier;

	@Resource(mappedName = "java:/TransactionManager")
	TransactionManager txManager;

	@Resource SessionContext sessionContext;
	
	@Produces
	@Named
	@Dependent
	@CurrentAuction
	public Auction getCurrentAuction() {
		if (currentAuction != null && !em.contains(currentAuction)) {
			currentAuction = em.merge(currentAuction);
		}
		return currentAuction;
	}

	public Long getCurrentAuctionId() {
		return (currentAuction == null) ? null : currentAuction.getId();
	}

	public void setCurrentAuctionId(Long currentId) {
		this.currentAuction = em.find(Auction.class, currentId);
	}

	public List<Auction> getAll() {
		return em.createQuery("SELECT a FROM Auction a", Auction.class)
				.getResultList();
	}

	@RolesAllowed({"auctionUser","auctionAdmin"})
	public List<Auction> getAuctionsWinningByUser(User user) {
		String jql = "SELECT auction FROM Auction auction, User user "
				+ "WHERE user=:user AND auction.highestBid member of user.bids "
				+ "ORDER BY auction.id";
		TypedQuery<Auction> query = em.createQuery(jql, Auction.class);
		query.setParameter("user", user);
		List<Auction> auctions = query.getResultList();
		return auctions;
	}

	@RolesAllowed({"auctionUser","auctionAdmin"})
	public List<Auction> getAuctionLoosingByUser(User user) {
		String jql = "SELECT DISTINCT auction FROM User user "
				+ "JOIN user.bids bid JOIN bid.auction auction "
				+ "WHERE user=:user AND auction.highestBid.bidder != user "
				+ "ORDER BY auction.id";
		TypedQuery<Auction> query = em.createQuery(jql, Auction.class);
		query.setParameter("user", user);
		List<Auction> auctions = query.getResultList();
		return auctions;
	}

	public void refreshAuction(Auction auction) {
		em.refresh(auction);
	}

	@RolesAllowed({"auctionUser"})
	public void addBid(long bidAmount) {
		try {
			txManager.getTransaction().registerSynchronization(
					new PrintSynchronization());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (sessionContext.getCallerPrincipal() == null) {
			throw new IllegalStateException(
					"user must be logged in order to add bid");
		}
		if (currentAuction == null) {
			throw new IllegalStateException(
					"currentAuction have to be selected in order to add bid");
		}

		Bid bid = new Bid(loginManagerBean.getCurrentUser(), currentAuction,
				bidAmount);
		em.persist(bid);
		em.flush();
		em.refresh(bid);
		messageNotifier.notifyBid(loginManagerBean.getCurrentUser(),
				currentAuction, bidAmount);
	}

	public void addFavorite(User user, Auction auction) {
		if (em.contains(user))
			user = em.merge(user);
		user.getFavorites().add(auction);
		user = em.merge(user);
	}

	public void removeFavorite(User user, Auction auction) {
		if (em.contains(user))
			user = em.merge(user);
		user.getFavorites().remove(auction);
		user = em.merge(user);
	}

}
