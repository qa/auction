package org.jboss.lectures.auction;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.persistence.SeamManaged;

@Dependent
@Stateless
public class EntityManagerProviderBean {
	
	@Produces
	@Dependent
	@SeamManaged
	@PersistenceContext(unitName = "jeelabPU")
	static EntityManager em;
}
