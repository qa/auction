package org.jboss.lectures.auction;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.jboss.seam.persistence.SeamManaged;

@ConversationScoped
public class ApplicationScopedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SeamManaged
	@PersistenceContext(unitName = "jeelabPU")
	EntityManager em;
	
	@Produces
	@Dependent
	public EntityManager getEntityManager() {
		return em;
	}
}
