package gov.nist.hit.ds.actorSimFactory;

import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;


public interface ActorFactory {
	void run() throws SoapFaultException;
}
