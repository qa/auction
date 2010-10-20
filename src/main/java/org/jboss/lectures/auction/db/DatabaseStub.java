package org.jboss.lectures.auction.db;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

	@PostConstruct
	public void initializeModel() {

		users.add(new User("Lukas"));
		users.add(new User("Ondra"));
		users.add(new User("Martin"));

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
		users.add(user);
	}

	public synchronized User findUserByName(String name) {
		for (User user : users) {
			if (name == user.getName()) {
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

	public synchronized List<Auction> getAuctionsByOwner(User owner) {
		List<Auction> result = new LinkedList<Auction>();
		for (Auction auction : auctions) {
			if (auction.getOwner().equals(owner)) {
				result.add(auction);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public Auction getAuctionById(int auctionId) {
		for (Auction auction : auctions) {
			if (auctionId == auction.getId()) {
				return auction;
			}
		}

		throw new IllegalStateException("no such auctionId " + auctionId);
	}

	public void addAuction(Auction auction) {
		auction = new Auction(auction);

		synchronized (this) {
			auctions.add(auction);
		}
	}

	/*
	 * Auction Generation
	 */
	private Auction generateAuction(String auctionName) {
		User user = generateUserForAuction(auctionName);
		BigDecimal originalPrice = generateOriginalPrice(auctionName);

		Auction auction = new Auction(auctionName, user);
		auction.setOriginalPrice(originalPrice);

		for (int i = 0; i < auctionName.hashCode() % 5; i++) {
			auction.addBid(generateBid(auction));
		}

		return auction;
	}

	private User generateUserForAuction(String auctionName) {
		return users.get(Math.abs(auctionName.hashCode()) % users.size());
	}

	private BigDecimal generateOriginalPrice(String auctionName) {
		return new BigDecimal(20 + (Math.abs(auctionName.hashCode()) % 200));
	}

	private Bid generateBid(Auction auction) {
		final BigDecimal originalPrice = auction.getOriginalPrice();
		final Bid highestBid = auction.getHighestBid();
		final User owner = auction.getOwner();

		BigDecimal newBidAmount;
		int divisor = 2 + Math.abs(originalPrice.hashCode()) % 3;
		BigDecimal augend = originalPrice.divide(new BigDecimal(divisor), RoundingMode.FLOOR);

		if (highestBid == null) {
			newBidAmount = originalPrice.add(augend);
		} else {
			newBidAmount = highestBid.getAmount().add(augend);
		}

		List<User> possibleUsers = new LinkedList<User>(users);
		possibleUsers.remove(owner);
		if (highestBid != null) {
			possibleUsers.remove(highestBid.getBidder());
		}

		int userIndex = Math.abs(newBidAmount.hashCode()) % possibleUsers.size();
		User newBidder = possibleUsers.get(userIndex);

		return new Bid(newBidder, newBidAmount);
	}
}
