package org.jboss.lectures.auction.entity;

import java.math.BigDecimal;

public class Bid {
	private User bidder;
	private BigDecimal amount;

	public Bid(User bidder, BigDecimal amount) {
		this.bidder = bidder;
		this.amount = amount;
	}

	public User getBidder() {
		return bidder;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public boolean isHigher(Bid bid) {
		if (bid == null) {
			return true;
		}

		return amount.compareTo(bid.amount) > 0;
	}

	public boolean isHigher(BigDecimal amount) {
		return this.amount.compareTo(amount) > 0;
	}
}
