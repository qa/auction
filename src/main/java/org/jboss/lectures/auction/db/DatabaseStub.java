package org.jboss.lectures.auction.db;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.jboss.lectures.auction.entity.Auction;
import org.jboss.lectures.auction.entity.Bid;
import org.jboss.lectures.auction.entity.User;

@ApplicationScoped
public class DatabaseStub {

	private List<User> users = new LinkedList<User>();

	private List<Auction> auctions = new LinkedList<Auction>();
	
	private volatile long userSequenceId = 1; 
	private volatile long auctionSequenceId = 1;
	private volatile long bidSequenceId = 1;

	@PostConstruct
	public void initializeModel() {
		users.add(generateUser("Lukas"));
		users.add(generateUser("Ondra"));
		users.add(generateUser("Martin"));

		auctions.add(generateAuction("Pet svestek"));
		auctions.add(generateAuction("Rohlik za odvoz"));
		auctions.add(generateAuction("Osmismerky"));
		auctions.add(generateAuction("Vodovaha"));
		auctions.add(generateAuction("Prirucka mladych svistu"));
		auctions.add(generateAuction("Leporelo"));
		auctions.add(generateAuction("Neznama hmota"));
		auctions.add(generateAuction("Detska sedacka"));
		auctions.add(generateAuction("Velorex"));
		auctions.add(generateAuction("Tajemny klic"));
		auctions.add(generateAuction("Zamek"));
		auctions.add(generateAuction("Prkenna ohrada"));
		auctions.add(generateAuction("Past na mysi"));
		auctions.add(generateAuction("Nabijeci baterie"));
		auctions.add(generateAuction("Plastova vicka"));
		auctions.add(generateAuction("Inkoustove naplne"));
		auctions.add(generateAuction("Myci houba"));
		auctions.add(generateAuction("Violoncello"));
		auctions.add(generateAuction("Pruvodce pokojovych plodin"));
		auctions.add(generateAuction("Socove kafe"));
		auctions.add(generateAuction("Olejnicka"));
		auctions.add(generateAuction("Hadi akvarium"));
		auctions.add(generateAuction("Dvojdilne tramovi"));
		auctions.add(generateAuction("Psi bouda"));

		auctions.add(generateAuction("Stare kolo"));
	}

	/*
	 * User
	 */

	public synchronized void addUser(User user) {
		user = new User(user);
		user.setId(userSequenceId++);
		users.add(user);
	}

	public synchronized User findUserByName(String name) {
		for (User user : users) {
			if (name.equals(user.getName())) {
				return user;
			}
		}

		return null;
	}

	/*
	 * Auction
	 */

	public synchronized List<Auction> getAllAuctions() {
		return Collections.unmodifiableList(auctions);
	}

	public Auction getAuctionById(long auctionId) {
		for (Auction auction : auctions) {
			if (auctionId == auction.getId()) {
				return auction;
			}
		}

		throw new IllegalStateException("no such auctionId " + auctionId);
	}

	public void addAuction(Auction auction) {
		auction = new Auction(auction);
		auction.setId(auctionSequenceId++);

		synchronized (this) {
			auctions.add(auction);
		}
	}

	/*
	 * Model Generation
	 */
	public User generateUser(String name) {
		User user = new User(name);
		user.setId(userSequenceId++);
		return user;
	}
	
	private Auction generateAuction(String auctionName) {
		User user = generateUserForAuction(auctionName);
		Long originalPrice = generateOriginalPrice(auctionName);

		Auction auction = new Auction(auctionName, user);
		auction.setId(auctionSequenceId++);
		auction.setOriginalPrice(originalPrice);

		for (int i = 0; i < auctionName.hashCode() % 5; i++) {
			Bid bid = generateBid(auction);
			bid.setId(bidSequenceId++);
			
			auction.addBid(bid);
			
			if (((Long.MAX_VALUE / 2) + (auctionSequenceId * bidSequenceId)) % 5 > 1) {
				if (!user.getFavorites().contains(auction)) {
					bid.getBidder().getFavorites().add(auction);
					auction.getBookmarkedBys().add(bid.getBidder());
				}
			}
		}

		return auction;
	}

	private User generateUserForAuction(String auctionName) {
		return users.get(Math.abs(auctionName.hashCode()) % users.size());
	}

	private Long generateOriginalPrice(String auctionName) {
		return 20l + (Math.abs(auctionName.hashCode()) % 200);
	}

	private Bid generateBid(Auction auction) {
		final Long originalPrice = auction.getOriginalPrice();
		final Bid highestBid = auction.getHighestBid();
		final User owner = auction.getOwner();

		Long newBidAmount;
		long divisor = 2l + Math.abs(originalPrice.hashCode()) % 3;
		Long augend = originalPrice / divisor;

		if (highestBid == null) {
			newBidAmount = originalPrice + augend;
		} else {
			newBidAmount = highestBid.getAmount() + augend;
		}

		List<User> possibleUsers = new LinkedList<User>(users);
		possibleUsers.remove(owner);
		if (highestBid != null) {
			possibleUsers.remove(highestBid.getBidder());
		}

		int userIndex = Math.abs(newBidAmount.hashCode()) % possibleUsers.size();
		User newBidder = possibleUsers.get(userIndex);

		return new Bid(newBidder, auction, newBidAmount);
	}
}
