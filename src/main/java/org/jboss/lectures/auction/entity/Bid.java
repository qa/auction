package org.jboss.lectures.auction.entity;

import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Bid {
	private User bidder;
	private Auction auction;
	private Long amount;
	private Long id;

	public Bid(User bidder, Auction auction, Long amount) {
		this.bidder = bidder;
		this.auction = auction;
		this.amount = amount;
		
		auction.addBid(this);
	}

	@Id
	@GeneratedValue(strategy = AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	public User getBidder() {
		return bidder;
	}

	public Long getAmount() {
		return amount;
	}
	
	@ManyToOne
	public Auction getAuction() {
		return auction;
	}

	public boolean isHigher(Bid bid) {
		if (bid == null) {
			return true;
		}

		return amount > bid.amount;
	}

	public boolean isHigher(Long amount) {
		return this.amount > amount;
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
		if (!(obj instanceof Bid)) {
			return false;
		}
		Bid other = (Bid) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Bid [bidder=" + bidder + ", amount=" + amount + "]";
	}
}
