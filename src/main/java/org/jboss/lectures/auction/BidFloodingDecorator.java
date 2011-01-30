package org.jboss.lectures.auction;

import java.io.Serializable;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@Decorator
public abstract class BidFloodingDecorator implements AuctionManager, Serializable {
	
	private static final long serialVersionUID = -1649208093375630472L;

	@Inject
	@Delegate
	private AuctionManager delegate;
	
	@Inject
	private BidTracker bidTracker;

	@Override
	public void addBid(long bidAmount) {
		if (bidTracker.isBidAllowed())
		{
			delegate.addBid(bidAmount);
			bidTracker.trackBidDate();
		}
		else
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You've bidded more than 2 times in 10 seconds. No flooding allowed!"));
		}
	}
}
