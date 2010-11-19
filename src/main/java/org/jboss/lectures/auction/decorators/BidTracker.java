package org.jboss.lectures.auction.decorators;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import javax.enterprise.context.SessionScoped;

/**
 * Tracks the Bids made by the current user
 */
@SessionScoped
public class BidTracker implements Serializable
{
   
   private static final long serialVersionUID = 1L;

   private LinkedList<Date> bids;
   
   public BidTracker()
   {
      this.bids = new LinkedList<Date>();
   }
   
   public void addBid()
   {
      this.bids.offerFirst(new Date());
   }
   
   public boolean isNewBidAllowed()
   {
      if (bids.size() > 2)
      {
         long diff = new Date().getTime() - bids.get(2).getTime();
         return diff > 20 * 1000;
      }
      else
      {
         return true;
      }
   }

}
