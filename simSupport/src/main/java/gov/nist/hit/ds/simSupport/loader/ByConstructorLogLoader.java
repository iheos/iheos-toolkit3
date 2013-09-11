package gov.nist.hit.ds.simSupport.loader;

import java.io.File;

import org.apache.log4j.Logger;

public class ByConstructorLogLoader extends AbstractLogLoader {
	static Logger logger = Logger.getLogger(ByConstructorLogLoader.class);
	
	public ByConstructorLogLoader(File dir) {
		logger.trace("Loading from <" + dir + ">");
		this.dir = dir;
	}

}
