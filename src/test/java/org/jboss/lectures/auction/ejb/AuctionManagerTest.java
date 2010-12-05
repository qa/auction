package org.jboss.lectures.auction.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.lectures.auction.AuctionManager;
import org.jboss.lectures.auction.AuctionManagerBean;
import org.jboss.lectures.auction.LoginManager;
import org.jboss.lectures.auction.UserManager;
import org.jboss.lectures.auction.entity.Auction;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class AuctionManagerTest
{

   @Deployment
   public static Archive<?> jar() {
      return ShrinkWrap.create(JavaArchive.class)
            .addClasses(AuctionManager.class, AuctionManagerBean.class, UserManager.class, LoginManager.class)
            .addPackage(Auction.class.getPackage())
            .addResource("import.sql", ArchivePaths.create("import.sql"))
            .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()), ArchivePaths.create("beans.xml"))
            .addManifestResource("META-INF/persistence.xml", ArchivePaths.create("persistence.xml"));
   }
   
   @PersistenceContext(unitName = "jeelabPU")
   @Produces
   @Default
   EntityManager em;
   
   @EJB
   AuctionManager auctionManager;
   
   
   @Test
   public void retrieveAuctions() {
      List<Auction> auctions = auctionManager.getAll();      
     
      System.out.println("Aukcicky:" + auctions.size());
      
      Assert.assertEquals("V databazi je 25 aukci", 25, auctions.size());
      
      Assert.assertEquals("Drazime body ze cvika", "Body ze cvika", auctions.get(24).getName());      
 
   }
   
}
