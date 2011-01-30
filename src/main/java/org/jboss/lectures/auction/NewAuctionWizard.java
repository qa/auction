package org.jboss.lectures.auction;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.lectures.auction.entity.Auction;
import org.jboss.lectures.auction.entity.User;
import org.jboss.lectures.auction.qualifiers.LoggedIn;

@ConversationScoped
@Stateful
@Named
public class NewAuctionWizard implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	@Inject
	@LoggedIn
	private User user;

	@Inject
	private EntityManager em;

	private Auction auction = new Auction();

	public void beginConversation() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	public void cancel() {
		conversation.end();
	}

	public void saveAuction() {
		auction.setOwner(user);
		em.persist(auction);
		conversation.end();
	}

	public Auction getAuction() {
		return auction;
	}
}
