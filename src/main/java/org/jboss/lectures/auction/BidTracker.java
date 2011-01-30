package org.jboss.lectures.auction;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class BidTracker implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date lastBidDate;
	
	public boolean isBidAllowed()
	{
		if (lastBidDate == null)
		{
			return true;
		}
		return new Date().getTime() - lastBidDate.getTime() > (10 * 1000);
	}
	
	public void trackBidDate()
	{
		lastBidDate = new Date();
	}
}
