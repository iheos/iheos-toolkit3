package gov.nist.hit.ds.actorSimFactory.factory;

import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;


public interface ActorFactory {
	void run() throws SoapFaultException;
}
