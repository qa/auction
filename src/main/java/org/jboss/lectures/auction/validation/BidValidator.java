package org.jboss.lectures.auction.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.lectures.auction.entity.Auction;
import org.jboss.lectures.auction.qualifiers.CurrentAuction;

@Named
public class BidValidator {
	
	@Inject
	@CurrentAuction
	private Auction currentAuction;
	
	public void validateBid(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		long bidAmount = Long.valueOf(value.toString());

		if (currentAuction == null) {
			produceMessageForComponent(context, component,
					"Není zvolena aktuální aukce");
		}

		if (currentAuction.getOriginalPrice() >= bidAmount) {
			produceMessageForComponent(context, component, "Nová nabídka ("
					+ bidAmount + ") musí být vyšší než originální cena ("
					+ currentAuction.getOriginalPrice() + ")");
		}

		if (currentAuction.getHighestBid() == null) {
			return;
		}

		if (currentAuction.getHighestBid().getAmount() >= bidAmount) {
			produceMessageForComponent(context, component, "Nová nabídka ("
					+ bidAmount
					+ ") musí být vyšší než dosavadní nejvyšší nabídka ("
					+ currentAuction.getHighestBid().getAmount() + ")");
		}
	}

	private void produceMessageForComponent(FacesContext context,
			UIComponent component, String message) {
		FacesMessage facesMessage = new FacesMessage(message);

		context.addMessage(component.getId(), facesMessage);

		throw new ValidatorException(facesMessage);
	}
}
