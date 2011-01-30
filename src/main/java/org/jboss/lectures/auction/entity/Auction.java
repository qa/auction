package org.jboss.lectures.auction.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

import java.io.Serializable;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.lectures.auction.validation.City;

@Entity
public class Auction implements Serializable {

	private static final long serialVersionUID = -38089703767395198L;
	@Size(min=2, max=30, message = "Nazov aukcie musi mat minimalne {min} a maximalne {max} znakov")
	private String name;
	private User owner;
	@Size(max=1000, message = "Popis moze mat maximalne {max} znakov")
	private String description;
	@Min(value = 1, message = "Minimalne vyvolavacia cena je 1 Kc")
	@NotNull(message = "Zadajte vyvolavaciu cenu")
	private Long originalPrice;
	private Bid highestBid;
	private List<Bid> bids = new ArrayList<Bid>();
	private List<User> bookmarkedBy = new ArrayList<User>();
	private Long id;
	@City
	private String location;

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

	public void setHighestBid(Bid highestBid) {
		this.highestBid = highestBid;
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

	@ManyToMany
	public List<User> getBookmarkedBys() {
		return bookmarkedBy;
	}

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

		this.getBids().add(bid);
		this.setHighestBid(bid);
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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
