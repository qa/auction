package org.jboss.lectures.auction.decorators;

import java.io.Serializable;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
//import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.jboss.lectures.auction.AuctionManager;

@Decorator
public abstract class BidFloodingDecorator implements AuctionManager, Serializable 
{

	private static final long serialVersionUID = 1L;

	@Inject	@Delegate
	private AuctionManager auctionManager;
	
	@Inject
	private BidTracker bidTracker;
	
	//@Inject
	//private FacesContext ctx;
	
	public void addBid(long bidAmount)
	{
		if (bidTracker.isNewBidAllowed())
		{
			bidTracker.addBid();
			auctionManager.addBid(bidAmount);
		}
		else 
		{
			//FacesMessage msg = new FacesMessage("You've bidded more than 2 times in 10 seconds. No flooding allowed!");
			//ctx.addMessage(null, msg);
			throw new IllegalStateException("You've bidded more than 2 times in 20 seconds. No flooding allowed!");
		}
	}

}
