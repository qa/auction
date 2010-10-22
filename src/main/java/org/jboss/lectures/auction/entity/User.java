package org.jboss.lectures.auction.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class User {

	private final String name;
	private List<Bid> bids = new ArrayList<Bid>();
	private List<Auction> auctions = new ArrayList<Auction>();
	private List<Auction> favorites = new ArrayList<Auction>();
	private Long id;
	
	public User() {
		this.name = null;
	}

	public User(User user) {
		this.name = user.getName();
	}

	public User(String name) {
		this.name = name;
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
	
	@OneToMany(cascade = ALL, fetch = EAGER, mappedBy = "user")
    @Column(nullable = true, updatable = false)
	public List<Bid> getBids() {
		return bids;
	}
	
	public void setBids(List<Bid> bids) {
	        this.bids = bids;
	}
	
	@OneToMany(cascade = ALL, fetch = EAGER, mappedBy = "user")
    @Column(nullable = true, updatable = false)
	public List<Auction> getAuctions() {
		return auctions;
	}
	
	public void setAuctions(List<Auction> auctions) {
	        this.auctions = auctions;
	}
	
	@ManyToMany(cascade = ALL, fetch = EAGER)
    @Column(nullable = true, updatable = true)
	public List<Auction> getFavorites() {
		return favorites;
	}
	
	public void setFavorites(List<Auction> favorites) {
	        this.favorites = favorites;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [name=" + name + "]";
	}
}
