package org.jboss.lectures.auction;

import javax.transaction.Synchronization;

public class PrintSynchronization implements Synchronization {

	@Override
	public void afterCompletion(int status) {
		System.out.println("Transaction finished with result " + status);
	}

	@Override
	public void beforeCompletion() {
		System.out.println("Transaction voting started");
	}

}
