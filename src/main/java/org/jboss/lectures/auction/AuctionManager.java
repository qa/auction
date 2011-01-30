package org.jboss.lectures.auction;

import java.util.List;
import org.jboss.lectures.auction.entity.Auction;
import org.jboss.lectures.auction.entity.User;

public interface AuctionManager 
{
	Auction getCurrentAuction();

	Long getCurrentAuctionId();

	void setCurrentAuctionId(Long currentId);

	List<Auction> getAll();

	List<Auction> getAuctionsWinningByUser(User user);

	List<Auction> getAuctionLoosingByUser(User user);

	void refreshAuction(Auction auction);

	void addBid(long bidAmount);

	void addFavorite(User user, Auction auction);

	void removeFavorite(User user, Auction auction);
}