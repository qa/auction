package org.jboss.lectures.auction;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.lectures.auction.entity.Auction;
import org.jboss.lectures.auction.entity.Bid;
import org.jboss.lectures.auction.entity.User;
import org.jboss.lectures.auction.qualifiers.CurrentAuction;

@ViewScoped
@Named("auctionManager")
@Stateful
public class AuctionManagerBean implements Serializable, AuctionManager {

	private static final long serialVersionUID = 1L;

	private Auction currentAuction = null;

	@Inject
	private EntityManager em;

	@Inject
	private LoginManager loginManagerBean;

	@Resource(mappedName = "/ConnectionFactory")
	ConnectionFactory jmsConnectionFactory;

	@Resource(mappedName = "/topic/AuctionNotification")
	Destination auctionNotification;

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

	public List<Auction> getAuctionsWinningByUser(User user) {
		String jql = "SELECT auction FROM Auction auction, User user "
				+ "WHERE user=:user AND auction.highestBid member of user.bids "
				+ "ORDER BY auction.id";
		TypedQuery<Auction> query = em.createQuery(jql, Auction.class);
		query.setParameter("user", user);
		List<Auction> auctions = query.getResultList();
		return auctions;
	}

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

	public void addBid(long bidAmount) {
		if (!loginManagerBean.isLogged()) {
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
		notifyBid(loginManagerBean.getCurrentUser(), currentAuction, bidAmount);

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

	private void notifyBid(User bidder, Auction auction, long bidAmount) {
		try {
			Connection connection = jmsConnectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer sender = session
					.createProducer(auctionNotification);
			MapMessage msg = session.createMapMessage();
			msg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			msg.setString("user", bidder.getEmail());
			msg.setLong("auctionId", auction.getId());
			msg.setLong("bid", bidAmount);
			sender.send(msg);
			sender.close();
			session.close();
			connection.stop();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
