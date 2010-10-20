package org.jboss.lectures.auction.entity;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class Auction {

	private String name;
	private User owner;
	private BigDecimal originalPrice;
	private Bid highestBid;
	private List<Bid> bids = new LinkedList<Bid>();
	private int id;

	{
		this.id = this.hashCode();
	}

	public Auction() {
	}

	public Auction(String name, User owner) {
		this.name = name;
		this.owner = owner;
	}

	public Auction(Auction auction) {
		this.id = auction.getId();
		this.name = auction.getName();
		this.owner = auction.getOwner();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public Bid getHighestBid() {
		return highestBid;
	}

	public List<Bid> getBids() {
		return bids;
	}

	public void addBid(Bid bid) {
		if (!bid.isHigher(originalPrice)) {
			throw new IllegalArgumentException("The new bid ("
					+ bid.getAmount()
					+ ") have to be greater than original price ("
					+ originalPrice + ")");
		}

		if (!bid.isHigher(highestBid)) {
			throw new IllegalArgumentException("The new bid ("
					+ bid.getAmount()
					+ ") have to be greater than original price ("
					+ highestBid.getAmount() + ")");
		}

		this.bids.add(bid);
		this.highestBid = bid;
	}

	@Override
	public String toString() {
		return "Auction [name=" + name + ", id=" + id + "]";
	}
}
