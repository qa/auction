package org.jboss.lectures.auction.selenium;

import java.io.File;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.selenium.annotation.Selenium;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.dependencies.Dependencies;
import org.jboss.shrinkwrap.dependencies.impl.MavenDependencies;
import org.jboss.shrinkwrap.dependencies.impl.filter.ScopeFilter;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.thoughtworks.selenium.DefaultSelenium;

@RunWith(Arquillian.class)
@Run(RunModeType.AS_CLIENT)
public class SeleniumTest
{
   @Selenium
   DefaultSelenium selenium;

   @Deployment
   public static Archive<?> war()
   {

      WebArchive war = ShrinkWrap.create(WebArchive.class, "practice0x-testing.war")

      .addLibraries(Dependencies.use(MavenDependencies.class).resolveFrom("pom.xml", new ScopeFilter("", "compile")))
            .addPackages(true, NO_TEST_CLASSES, org.jboss.lectures.auction.AuctionManager.class.getPackage())
            .addResource(new File("src/main/webapp/detail.xhtml"), ArchivePaths.create("detail.xhtml"))
            .addResource(new File("src/main/webapp/index.html"), ArchivePaths.create("index.html"))
            .addResource(new File("src/main/webapp/list.xhtml"), ArchivePaths.create("list.xhtml"))
            .addResource(new File("src/main/webapp/myAuctions.xhtml"), ArchivePaths.create("myAuctions.xhtml"))
            .addResource(new File("src/main/webapp/myOverview.xhtml"), ArchivePaths.create("myOverview.xhtml"))
            .addResource(new File("src/main/webapp/newAuction.xhtml"), ArchivePaths.create("newAuction.xhtml"))
            .addResource(new File("src/main/webapp/newAuctionDetails.xhtml"), ArchivePaths.create("newAuctionDetails.xhtml"))
            .addResource(new File("src/main/webapp/newAuctionSummary.xhtml"), ArchivePaths.create("newAuctionSummary.xhtml"))
            .addResource(new File("src/main/webapp/script.js"), ArchivePaths.create("script.js"))
            .addResource(new File("src/main/webapp/style.css"), ArchivePaths.create("style.css"))
            .addResource(new File("src/main/webapp/style.ie6.css"), ArchivePaths.create("style.ie6.css"))
            .addResource(new File("src/main/webapp/style.ie7.css"), ArchivePaths.create("style.ie7.css"))
            .addResource(new File("src/main/webapp/images/01.png"), ArchivePaths.create("images/01.png"))
            .addResource(new File("src/main/webapp/images/02.png"), ArchivePaths.create("images/02.png"))
            .addResource(new File("src/main/webapp/images/03.png"), ArchivePaths.create("images/03.png"))
            .addResource(new File("src/main/webapp/images/Block-h.png"), ArchivePaths.create("images/Block-h.png"))
            .addResource(new File("src/main/webapp/images/Block-s.png"), ArchivePaths.create("images/Block-s.png"))
            .addResource(new File("src/main/webapp/images/Block-v.png"), ArchivePaths.create("images/Block-v.png"))
            .addResource(new File("src/main/webapp/images/BlockContentBullets.png"), ArchivePaths.create("images/BlockContentBullets.png"))
            .addResource(new File("src/main/webapp/images/BlockHeader.png"), ArchivePaths.create("images/BlockHeader.png"))
            .addResource(new File("src/main/webapp/images/BlockHeaderIcon.png"), ArchivePaths.create("images/BlockHeaderIcon.png"))
            .addResource(new File("src/main/webapp/images/Button.png"), ArchivePaths.create("images/Button.png"))
            .addResource(new File("src/main/webapp/images/Footer.png"), ArchivePaths.create("images/Footer.png"))
            .addResource(new File("src/main/webapp/images/Header.jpg"), ArchivePaths.create("images/Header.jpg"))
            .addResource(new File("src/main/webapp/images/Header.png"), ArchivePaths.create("images/Header.png"))
            .addResource(new File("src/main/webapp/images/MenuItem.png"), ArchivePaths.create("images/MenuItem.png"))
            .addResource(new File("src/main/webapp/images/Page-BgGlare.png"), ArchivePaths.create("images/Page-BgGlare.png"))
            .addResource(new File("src/main/webapp/images/Page-BgSimpleGradient.jpg"), ArchivePaths.create("images/Page-BgSimpleGradient.jpg"))
            .addResource(new File("src/main/webapp/images/PostQuote.png"), ArchivePaths.create("images/PostQuote.png"))
            .addResource(new File("src/main/webapp/images/Sheet-h.png"), ArchivePaths.create("images/Sheet-h.png"))
            .addResource(new File("src/main/webapp/images/Sheet-s.png"), ArchivePaths.create("images/Sheet-s.png"))
            .addResource(new File("src/main/webapp/images/Sheet-v.png"), ArchivePaths.create("images/Sheet-v.png"))
            .addResource(new File("src/main/webapp/images/contact.jpg"), ArchivePaths.create("images/contact.jpg"))
            .addResource(new File("src/main/webapp/images/link photo.txt"), ArchivePaths.create("images/link photo.txt"))
            .addResource(new File("src/main/webapp/images/nav.png"), ArchivePaths.create("images/nav.png"))
            .addResource(new File("src/main/webapp/images/rssIcon.png"), ArchivePaths.create("images/rssIcon.png"))
            .addResource(new File("src/main/webapp/images/spacer.gif"), ArchivePaths.create("images/spacer.gif"))
            .addResource(new File("src/main/webapp/images/spectacles.gif"), ArchivePaths.create("images/spectacles.gif"))
            .addResource(new File("src/main/webapp/images/subitem-bg.png"), ArchivePaths.create("images/subitem-bg.png"))
            .addResource(new File("src/main/webapp/resources/auction/auctionList.xhtml"), ArchivePaths.create("resources/auction/auctionList.xhtml"))
            .addResource(new File("src/main/webapp/resources/auction/menuItem.xhtml"), ArchivePaths.create("resources/auction/menuItem.xhtml"))
            .addResource(new File("src/main/webapp/templates/home.xhtml"), ArchivePaths.create("templates/home.xhtml"))
            .addResource(new File("src/main/webapp/templates/menu.xhtml"), ArchivePaths.create("templates/menu.xhtml"))
            .addResource(new File("src/main/webapp/templates/login.xhtml"), ArchivePaths.create("templates/login.xhtml"))
            .addWebResource(new File("src/main/webapp/WEB-INF/beans.xml"), "beans.xml")
            .addWebResource(new File("src/main/webapp/WEB-INF/faces-config.xml"), "faces-config.xml")
            .addWebResource(new File("src/main/webapp/WEB-INF/jboss-scanning.xml"), "jboss-scanning.xml")
            .addWebResource(new File("src/main/resources/import.sql"), ArchivePaths.create("classes/import.sql"))
            .addManifestResource(new File("src/main/resources/META-INF/persistence.xml"))
            .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));

      System.out.println(war.toString(true));
      File file = new File("/tmp/test.war");
      if (file.exists())
         file.delete();
      war.as(ZipExporter.class).exportZip(file);

      return war;

   }

   @Test
   public void testLogin()
   {

      selenium.open("http://localhost:8080/practice0x-testing/index.html");

      selenium.type("tester@tester.org", "xpath=//input[contains(@id, 'emailInput')]");
      selenium.click("xpath=//input[contains(@id, 'loginButton')]");
   }

   private static final Filter<ArchivePath> NO_TEST_CLASSES = new Filter<ArchivePath>()
   {

      @Override
      public boolean include(ArchivePath object)
      {
         return !object.get().contains("Test");
      }

   };

}
