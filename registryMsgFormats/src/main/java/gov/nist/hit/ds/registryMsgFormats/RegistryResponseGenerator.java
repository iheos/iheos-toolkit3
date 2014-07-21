package gov.nist.hit.ds.registryMsgFormats;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.engine.annotations.SimComponentInject;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import org.apache.axiom.om.OMElement;

public class RegistryResponseGenerator extends ResponseGenerator {
	Event event;

	@SimComponentInject
	public void setEvent(Event event) {
		this.event = event;
	}

	public RegistryResponseGenerator()  {
		response = XmlUtil.om_factory.createOMElement("RegistryResponse", ebRSns);
	}

	public RegistryResponseGenerator(RegistryErrorListGenerator rel) throws ToolkitRuntimeException {
		super(rel);
		response = XmlUtil.om_factory.createOMElement("RegistryResponse", ebRSns);
	}

	public void addQueryResults(OMElement metadata) {

	}

	public OMElement getRoot() { return response; }

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		try {
			event.getInOutMessages().putResponse(getResponseAsString());
		} catch (RepositoryException e) {
			throw new SoapFaultException(
					null,
					FaultCode.Receiver,
					ExceptionUtil.exception_details(e));
		}
	}

	@Override
	public boolean showOutputInLogs() {
		return true;
	}

}
