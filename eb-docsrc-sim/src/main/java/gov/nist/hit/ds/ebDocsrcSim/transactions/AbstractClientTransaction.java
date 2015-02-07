package gov.nist.hit.ds.ebDocsrcSim.transactions;

import gov.nist.hit.ds.ebDocsrcSim.engine.*;
import gov.nist.hit.ds.ebDocsrcSim.soap.Soap;
import gov.nist.hit.ds.ebDocsrcSim.soap.SoapActionFactory;
import gov.nist.hit.ds.simSupport.simulator.SimHandle;
import gov.nist.hit.ds.utilities.xml.OMFormatter;
import gov.nist.hit.ds.xdsExceptions.XdsInternalException;
import gov.nist.toolkit.utilities.xml.Util;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractClientTransaction {
	public OMElement instruction_output;
	public StepContext s_ctx;
	OmLogger testLog = new TestLogFactory().getLogger();

	public boolean no_convert = false;
	static public final short xds_none = 0;
	static public final short xds_b = 2;
	public short xds_version = AbstractClientTransaction.xds_none;
	public String endpoint = null;
	protected boolean soap_1_2 = true;
	protected boolean async = false;
	boolean useMtom;
	boolean useAddressing;
	public Map<String, String> nameUuidMap = null;
	private Soap soap;
    SimHandle simHandle;

	public List<OMElement> additionalHeaders = new ArrayList<>();

	public TransactionSettings transactionSettings = null;

	protected abstract String getRequestAction();

    protected AbstractClientTransaction() {  }


	String getResponseAction() {
		return SoapActionFactory.getResponseAction(getRequestAction());
	}

	protected OMElement soapCall(OMElement requestBody) throws Exception {
		soap = new Soap();
        soap.simHandle = simHandle;
		soap.setAsync(async);
//		if (testConfig.saml) {
//			soap.setRepositoryLocation(testConfig.testmgmt_dir + File.separator + "rampart" + File.separator + "client_repositories" );
//		}

		if (additionalHeaders != null) {
			for (OMElement hdr : additionalHeaders)
				try {
					soap.addHeader(Util.deep_copy(hdr));
				} catch (XdsInternalException e) {
					s_ctx.set_error(e.getMessage());
					logSoapRequest(soap);
				}
		}
		
		/*
		if (wsSecHeaders != null) {
			for (OMElement hdr : wsSecHeaders)
				try {
					soap.addSecHeader(Util.deep_copy(hdr));
				} catch (XdsInternalException e) {
					s_ctx.set_error(e.getMessage());
					failed();
					logSoapRequest(soap);
				}
		}
		*/

        OMElement results = null;
		try {
            if (testLog != null && instruction_output != null)
			    testLog.add_name_value(instruction_output, "Request", requestBody);

            if (s_ctx.transactionSettings.securityParams != null)
			    soap.setSecurityParams(s_ctx.transactionSettings.securityParams);

			results = soap.soapCall(requestBody,
					endpoint, 
					useMtom, //mtom
					useAddressing,  // WS-Addressing
					soap_1_2,  // SOAP 1.2
					getRequestAction(),
					getResponseAction(),
                    ""
			);
		}
		catch (AxisFault e) {
            OMElement e1 = e.getDetail();
            OMElement e2 = e.getFaultNodeElement();
            String e1m = new OMFormatter(e1).toString();
            String e2m = new OMFormatter(e2).toString();

			s_ctx.set_error("SOAPFault: " + e.getMessage() + "\nEndpoint is " + endpoint + "\n" + e1m + "\n" + e2m);
				s_ctx.set_fault(e);
		}
		finally {
            logSoapRequest(soap);
			soap.clearHeaders();
		}


        return results;
	}
	public void logSoapRequest(Soap soap) {
        if (testLog == null) return;
		try {
			testLog.add_name_value(instruction_output, "RequestHeader", soap.getOutHeader());
			testLog.add_name_value(instruction_output, "RequestAction", getRequestAction());
			testLog.add_name_value(instruction_output, "ExpectedResponseAction", getResponseAction());
			testLog.add_name_value(instruction_output, "ResponseHeader", soap.getInHeader());
			testLog.add_name_value(instruction_output, "Response", soap.getResult());
		} catch (Exception e) {
			System.out.println("oops");
			e.printStackTrace();
		}
	}

	protected OMElement getSoapResult() {
		if (soap == null) return null;
		return soap.getResult();
	}
}
