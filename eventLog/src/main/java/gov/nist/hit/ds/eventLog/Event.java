package gov.nist.hit.ds.eventLog;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.RepositoryException;

import java.io.File;

/**
 * Holds references to input header and body of the HTTP message.  Here the term
 * event corresponds to a transaction, an input to an actor.
 * @author bill
 *
 */
public class Event {
	Asset event;
	File eventDir;
	InOutMessages inOut;
	Artifacts artifacts;
	Assertions assertions;
	
	public Event(Asset event) {
		this.event = event;
	}
	
	void init() throws RepositoryException {
		Asset a;
		inOut = new InOutMessages();
		a = inOut.init(event);
		AssetHelper.setOrder(a, 1);

		artifacts = new Artifacts();
		a = artifacts.init(event);
		AssetHelper.setOrder(a, 2);

		assertions = new Assertions();
		a = assertions.init(event);
		AssetHelper.setOrder(a, 3);
	}

	public InOutMessages getInOutMessages() {
		return inOut;
	}
	
	public Artifacts getArtifacts() {
		return artifacts;
	}
	
	public Assertions getAssertions() {
		return assertions;
	}
}
