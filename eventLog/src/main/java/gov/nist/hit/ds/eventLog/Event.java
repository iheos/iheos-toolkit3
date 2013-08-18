package gov.nist.hit.ds.eventLog;

import java.io.File;

/**
 * Holds references to input header and body of the HTTP message.  Here the term
 * event corresponds to a transaction, an input to an actor.
 * @author bill
 *
 */
public class Event {
	File eventDir;
	InOutMessages inOut;
	Artifacts artifacts;
	Assertions assertions;

	public Event(File eventDir) {
		this.eventDir = eventDir;
		inOut = new InOutMessages(new File(eventDir, "inout"));
		artifacts = new Artifacts(new File(eventDir, "artifacts"));
		assertions = new Assertions(new File(eventDir, "assertions"));
	}

	public File getEventDir() {
		return eventDir;
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
