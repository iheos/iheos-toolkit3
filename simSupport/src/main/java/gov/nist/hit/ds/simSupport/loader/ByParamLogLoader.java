package gov.nist.hit.ds.simSupport.loader;

import java.io.File;

/**
 * Load HTTP event from log format - 2 files
 * 	- test file with header - request_hdr.txt
 *  - byte array file with body  - request_body.bin
 * @author bmajur
 *
 */
public class ByParamLogLoader extends AbstractLogLoader {
	
	public ByParamLogLoader() {
	}
	
	public ByParamLogLoader setSource(File dir) {
		this.dir = dir;
		return this;
	}

	public ByParamLogLoader setSource(String dir) {
		this.dir = new File(dir);
		return this;
	}

	@Override
	public boolean showOutputInLogs() {
		return false;
	}

}
