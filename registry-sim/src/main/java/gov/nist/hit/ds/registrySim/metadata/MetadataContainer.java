package gov.nist.hit.ds.registrySim.metadata;

import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.valSupport.engine.MessageValidatorEngine;

/**
 * Empty shell of a validator that holds a copy of the pre-parsed 
 * metadata so other validators can find it later.
 * @author bill
 *   
 */
public class MetadataContainer   {
	Metadata m;
	
	public MetadataContainer(Metadata m) {
		this.m = m;
	}

	public void run(MessageValidatorEngine mvc) {
		
	}
	
	public Metadata getMetadata() {
		return m;
	}

}
