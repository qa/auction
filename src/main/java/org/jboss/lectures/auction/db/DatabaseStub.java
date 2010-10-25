package org.jboss.lectures.auction.db;

import java.util.ArrayList;
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
		users.add(generateUser("lfryc@redhat.com"));
		users.add(generateUser("ozizka@redhat.com"));
		users.add(generateUser("mvecera@redhat.com"));

		auctions.add(generateAuction("Pět švestek"));
		auctions.add(generateAuction("Rohlík za odvoz"));
		auctions.add(generateAuction("Osmisměrky"));
		auctions.add(generateAuction("Vodováha"));
		auctions.add(generateAuction("Příručka mladých svišťů"));
		auctions.add(generateAuction("Leporelo"));
		auctions.add(generateAuction("Neznámá hmota"));
		auctions.add(generateAuction("Dětská sedačka"));
		auctions.add(generateAuction("Velorex"));
		auctions.add(generateAuction("Tajemný klíč"));
		auctions.add(generateAuction("Zámek"));
		auctions.add(generateAuction("Prkenná ohrada"));
		auctions.add(generateAuction("Past na myši"));
		auctions.add(generateAuction("Nabíjecí baterie"));
		auctions.add(generateAuction("Plastová víčka"));
		auctions.add(generateAuction("Inkoustové náplně"));
		auctions.add(generateAuction("Mycí houba"));
		auctions.add(generateAuction("Violoncello"));
		auctions.add(generateAuction("Průvodce pokojových plodin"));
		auctions.add(generateAuction("Socové kafe"));
		auctions.add(generateAuction("Olejnička"));
		auctions.add(generateAuction("Hadí akvárium"));
		auctions.add(generateAuction("Dvojdílné trámoví"));
		auctions.add(generateAuction("Psí bouda"));
		auctions.add(generateAuction("Staré kolo"));
	}

	/*
	 * User
	 */

	public synchronized void addUser(User user) {
		user = new User(user);
		user.setId(userSequenceId++);
		users.add(user);
	}

	public synchronized User findUserByEmail(String email) {
		for (User user : users) {
			if (email.equals(user.getEmail())) {
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

	public Auction createAuction(Auction auction) {
		auction = new Auction(auction);
		auction.setId(auctionSequenceId++);

		synchronized (this) {
			auctions.add(auction);
		}
		
		return auction;
	}
	
	public List<Auction> getAuctionsWinningByUser(User user) {
		List<Auction> winning = new ArrayList<Auction>();
		
		for (Auction auction : auctions) {
			if (auction.getHighestBid() == null) {
				continue;
			}
			if (auction.getHighestBid().getBidder().equals(user)) {
				winning.add(auction);
			}
		}
		
		return winning;
	}
	
	public List<Auction> getAuctionsLoosingByUser(User user) {
		List<Auction> loosing = new ArrayList<Auction>();
		
		for (Auction auction : auctions) {
			if (auction.getHighestBid() == null) {
				continue;
			}
			if (auction.getHighestBid().getBidder().equals(user)) {
				continue;
			}
			for (Bid bid : auction.getBids()) {
				if (bid.getBidder().equals(user)) {
					loosing.add(auction);
					break;
				}
			}
		}
		
		return loosing;
	}
	
	/*
	 * Favorites
	 */
	
	public void addFavorite(User user, Auction auction) {
		user.getFavorites().add(auction);
		auction.getBookmarkedBys().add(user);
	}
	
	public void removeFavorite(User user, Auction auction) {
		user.getFavorites().remove(auction);
		auction.getBookmarkedBys().remove(user);
	}

	/*
	 * Model Generation
	 */
	public User generateUser(String email) {
		User user = new User(email);
		user.setId(userSequenceId++);
		return user;
	}
	
	private Auction generateAuction(String auctionName) {
		User user = generateUserForAuction(auctionName);
		Long originalPrice = generateOriginalPrice(auctionName);

		Auction auction = new Auction(auctionName, user);
		auction.setId(auctionSequenceId++);
		auction.setOriginalPrice(originalPrice);
		auction.setDescription("Zde se bude dražit o " + auctionName.toLowerCase() + "...");

		for (int i = 0; i < auctionName.hashCode() % 5; i++) {
			Bid bid = generateBid(auction);
			bid.setId(bidSequenceId++);
			
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
