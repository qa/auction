package org.jboss.lectures.auction.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.lectures.auction.AuctionManager;
import org.jboss.lectures.auction.AuctionManagerBean;
import org.jboss.lectures.auction.LoginManager;
import org.jboss.lectures.auction.entity.Auction;
import org.jboss.lectures.auction.entity.User;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.dependencies.Dependencies;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(Arquillian.class)
public class MockitoAuctionManagerTest
{

   @Deployment
   public static Archive<?> jar()
   {
      return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(AuctionManager.class, AuctionManagerBean.class, LoginManager.class)
            .addPackage(Auction.class.getPackage())
            .addResource("import.sql", ArchivePaths.create("import.sql"))
            .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()), ArchivePaths.create("beans.xml"))
            .addManifestResource("META-INF/persistence.xml", ArchivePaths.create("persistence.xml"))
            .addLibraries(Dependencies.artifact("org.mockito:mockito-all:1.8.5").resolve());
   }

   @PersistenceContext(unitName = "jeelabPU")
   @Produces
   @Default
   EntityManager em;

   @Produces
   @Default
   LoginManager lm = Mockito.mock(LoginManager.class, Mockito.withSettings().serializable());

   @Inject
   AuctionManager auctionManager;

   @Test
   public void retrieveAuctions()
   {

      User user = new User();
      user.setEmail("kpiwko@redhat.com");
      user.setId(4l);

      Mockito.when(lm.isLogged()).thenReturn(true);
      Mockito.when(lm.getCurrentUser()).thenReturn(user);

      List<Auction> auctions = auctionManager.getAuctionsWinningByUser(user);

      System.out.println("Aukcicky:" + auctions.size());

      Assert.assertEquals("V databazi je 25 aukci", 25, auctions.size());

      Assert.assertEquals("Drazime body ze cvika", "Body ze cvika", auctions.get(24).getName());

   }

}
