package gov.nist.hit.ds.toolkit.tkProps;

import gov.nist.hit.ds.toolkit.installation.InitializationFailedException;
import gov.nist.hit.ds.toolkit.tkProps.client.TkProps;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.utilities.io.LinesOfFile;

import java.io.File;
import java.io.IOException;

public class TkLoader {

	static public TkPropsServer LOAD(File file) throws IOException {
		LinesOfFile lof;
		TkPropsServer p = new TkPropsServer();

		if (!file.exists()) 
			throw new IOException("Property file " + file + " does not exist");

		lof = new LinesOfFile(file);
		while(lof.hasNext()) {
			String l = lof.next();
			p.parse(l);
		}

		return p;
	}

	static public void SAVE(TkPropsServer p, File file) throws IOException {
		String content = p.toString();
		Io.stringToFile(file, content);
	}

	static public TkProps tkProps(File configFile) throws InitializationFailedException, IOException {
		File installedTkProps = configFile;
		if (installedTkProps.exists())
			return TkLoader.LOAD(installedTkProps).toTkProps();
		throw new InitializationFailedException("Cannot load tk_props");
	}


}
