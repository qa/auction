package org.jboss.lectures.auction.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

@Entity
public class Auction {

	private String name;
	private User owner;
	private String description;
	private Long originalPrice;
	private Bid highestBid;
	private List<Bid> bids = new ArrayList<Bid>();
	private List<User> bookmarkedBy = new ArrayList<User>();
	private Long id;

	public Auction() {
	}

	public Auction(String name, User owner) {
		this.name = name;
		this.owner = owner;

		owner.getAuctions().add(this);
	}

	public Auction(Auction auction) {
		this(auction.getName(), auction.getOwner());
		this.id = auction.getId();
		this.description = auction.getDescription();
		this.originalPrice = auction.getOriginalPrice();
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
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

	@OneToMany(cascade = ALL, fetch = LAZY, mappedBy = "auction")
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Auction)) {
			return false;
		}
		Auction other = (Auction) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Auction [name=" + name + ", owner=" + owner + "]";
	}
}
