package gov.nist.hit.ds.simSupport.factory;

import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;


public interface TransactionFactory {
	void run() throws SoapFaultException;
}
