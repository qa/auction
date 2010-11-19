package org.jboss.lectures.auction;

import java.util.List;
import org.jboss.lectures.auction.entity.Auction;
import org.jboss.lectures.auction.entity.User;

public interface AuctionManager 
{

	public abstract Auction getCurrentAuction();

	public abstract Long getCurrentAuctionId();

	public abstract void setCurrentAuctionId(Long currentId);

	public abstract List<Auction> getAll();

	public abstract List<Auction> getAuctionsWinningByUser(User user);

	public abstract List<Auction> getAuctionLoosingByUser(User user);

	public abstract void addAuction(Auction auction);

	public abstract void refreshAuction(Auction auction);

	public abstract void addBid(long bidAmount);

	public abstract void addFavorite(User user, Auction auction);

	public abstract void removeFavorite(User user, Auction auction);

	public abstract Auction createNewAuction();

}