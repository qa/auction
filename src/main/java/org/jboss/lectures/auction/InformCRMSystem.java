package org.jboss.lectures.auction;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Message-Driven Bean implementation class for: InformCRMSystem
 * 
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "/queue/UserCreated") },
		mappedName = "/queue/UserCreated")
public class InformCRMSystem implements MessageListener {

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message messageParam) {
		TextMessage message = (TextMessage) messageParam;
		try {
			System.out.println(message.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
