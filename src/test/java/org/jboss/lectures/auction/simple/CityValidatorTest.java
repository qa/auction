package org.jboss.lectures.auction.simple;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;

import org.jboss.lectures.auction.validation.CityValidator;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

public class CityValidatorTest extends Arquillian
{
   @Inject
   private CityValidator validator;
   
   @Deployment
   public static JavaArchive jar() {
      return ShrinkWrap.create(JavaArchive.class)
      .addClass(CityValidator.class)
      .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()), ArchivePaths.create("beans.xml"));
   }
   
   @Test
   public void isInBigCity() {
      Assert.assertFalse(validator.isValid("Ostrava", null), "Ostrava neni dost velke mesto");
   }
   
}
