package gov.nist.hit.ds.eventLog;


import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.eventLog.assertion.Assertions;
import gov.nist.hit.ds.eventLog.assertion.Fault;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.SimpleAsset;

import java.io.File;

/**
 * Holds references to input header and body of the HTTP message.  Here the term
 * event corresponds to a transaction, an input to an actor.
 * There are no I/O methods included since Assets are capable of storing themselves.
 * @author bill
 *
 */
public class Event {
	Asset event;
	File eventDir;
	InOutMessages inOut;
	Artifacts artifacts;
	Assertions assertions;
	Fault fault;

	public Event(Asset event) throws RepositoryException {
		this.event = event;
		init();
	}

	void init() throws RepositoryException {
		Asset a;
		inOut = new InOutMessages();
		a = inOut.init(event);
		a.setOrder(1);

		artifacts = new Artifacts();
		a = artifacts.init(event);
		a.setOrder(2);

		assertions = new Assertions();
		a = assertions.init(event);
		a.setOrder(3);

		// Created only if needed
		fault = new Fault();
		fault.init(event, 4);
	}

	public InOutMessages getInOutMessages() {
		return inOut;
	}

	public Fault getFault() {
		return fault;
	}

	public Artifacts getArtifacts() {
		return artifacts;
	}

	public Assertions getAssertions() {
		return assertions;
	}

	public Event addAssertionGroup(AssertionGroup ag) throws RepositoryException {
		assertions.add(ag);
		return this;
	}
}
