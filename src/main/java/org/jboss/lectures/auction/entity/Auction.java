package org.jboss.lectures.auction.entity;

import java.util.List;
import java.util.LinkedList;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.CascadeType.*;

@Entity
public class Auction {

	private String name;
	private User owner;
	private List<User> bookmarkedBy = new LinkedList<User>();
	private Long originalPrice;
	private Bid highestBid;
	private List<Bid> bids = new LinkedList<Bid>();
	private Long id;

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

	@Id
	@GeneratedValue(strategy = AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void setOriginalPrice(Long originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Long getOriginalPrice() {
		return originalPrice;
	}

	@OneToOne(cascade = ALL)
	public Bid getHighestBid() {
		return highestBid;
	}
	
	@OneToMany(cascade = ALL, fetch = EAGER, mappedBy = "auction")
	@Column(nullable = true, updatable = false)
	@OrderBy("amount DESC")
	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}
	
	public List<User> getBookmarkedBys() {
		return bookmarkedBy;
	}
	
	@ManyToMany
	public void setBookmarkedBys(List<User> bookmarkedBy) {
		this.bookmarkedBy = bookmarkedBy;
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
