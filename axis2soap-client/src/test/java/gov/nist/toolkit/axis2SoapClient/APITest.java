package gov.nist.toolkit.axis2SoapClient;

import gov.nist.toolkit.axis2SoapClient.impl.Axis2SoapClient;
import gov.nist.toolkit.axis2SoapClient.soapProcessing.OMProcessing;
import gov.nist.toolkit.soapClientAPI.RequestConfig;
import gov.nist.toolkit.soapClientAPI.SoapClient;
import gov.nist.toolkit.soapClientAPI.SoapClientFault;

import java.io.IOException;
import java.io.InputStream;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.junit.Ignore;
import org.junit.Test;

public class APITest extends BaseTest {

	
	 /* Ignored when building the module because it depends on the manual setup
	  	of the target service.*/
	 
 	@Test
 	public void runRequest() throws IOException, SoapClientFault {
 
 		RequestConfig conf = getTestConfiguration();
 
 		ConfigurationContext context = ConfigurationContextFactory.createConfigurationContextFromFileSystem(
 				System.getProperty("user.dir") + "/src/test/resources/validConfig/axis2repo",
 				System.getProperty("user.dir") + "/src/test/resources/validConfig/axis2repo/conf/axis2.xml");
 
 		SoapClient c = new Axis2SoapClient(context);
 
 		// get payload
 		OMElement payload = createPayLoad();
 		SOAPFactory fac = OMAbstractFactory.getSOAP12Factory();
 		SOAPEnvelope envelope = fac.getDefaultEnvelope();
 		envelope.getBody().addChild(payload);
 
 		SOAPEnvelope res = c.runBlockingRequest(conf, envelope);
 		System.out.println(OMProcessing.toString(res));
 
 	}

	
	 /* This test shows we can provide additional headers.*/
	 
 	@Test
 	public void runRequestWithAdditionalHeaders() throws IOException, SoapClientFault {
 
 		RequestConfig conf = getTestConfiguration();
 
 		ConfigurationContext context = ConfigurationContextFactory.createConfigurationContextFromFileSystem(
 				System.getProperty("user.dir") + "/src/test/resources/validConfig/axis2repo",
 				System.getProperty("user.dir") + "/src/test/resources/validConfig/axis2repo/conf/axis2.xml");
 
 		SoapClient c = new Axis2SoapClient(context);
 
 		// get payload
 		OMElement payload = createPayLoad();
 		OMElement headers = createMessageIdHeader();
 		SOAPFactory fac = OMAbstractFactory.getSOAP12Factory();
 		SOAPEnvelope envelope = fac.getDefaultEnvelope();
 		envelope.getBody().addChild(payload);
 
 		OMNamespace secNs = fac.createOMNamespace(
 				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
 		SOAPHeaderBlock h1 = envelope.getHeader().addHeaderBlock("Security", secNs);
 		h1.setText("blah");
 
 		SOAPEnvelope res = c.runBlockingRequest(conf, envelope);
 		System.out.println(OMProcessing.toString(res));
 	}

 	@Test
 	public void runRequestWithAdditionalRequiredHeaders() throws IOException, SoapClientFault {
 
 		RequestConfig conf = getTestConfiguration();
 
 		ConfigurationContext context = ConfigurationContextFactory.createConfigurationContextFromFileSystem(
 				System.getProperty("user.dir") + "/src/test/resources/validConfig/axis2repo",
 				System.getProperty("user.dir") + "/src/test/resources/validConfig/axis2repo/conf/axis2.xml");
 
 		SoapClient c = new Axis2SoapClient(context);
 
 		// get payload
 		OMElement payload = createPayLoad();
 		OMElement headers = createMessageIdHeader();
 		SOAPFactory fac = OMAbstractFactory.getSOAP12Factory();
 		SOAPEnvelope envelope = fac.getDefaultEnvelope();
 		envelope.getBody().addChild(payload);
 
 		OMNamespace omNs = fac.createOMNamespace("http://www.w3.org/2005/08/addressing", "wsa");
 		SOAPHeaderBlock h1 = envelope.getHeader().addHeaderBlock("MessageID", omNs);
 		h1.setText("urn:uuid:9");
 
 		SOAPEnvelope res = c.runBlockingRequest(conf, envelope);
 		System.out.println(OMProcessing.toString(res));
 
 	}

	private OMElement createMessageIdHeader() {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace("http://www.w3.org/2005/08/addressing", "wsa");
		OMElement messageID = fac.createOMElement("MessageID", omNs);
		messageID.setText("urn:uuid:9");
		return messageID;
	}

	private RequestConfig getTestConfiguration() throws IOException {
		RequestConfig conf = new RequestConfig();

		// get properties
		InputStream is = APITest.class.getResourceAsStream("/validConfig/soapRequestConfig.properties");
		conf.load(is);
		is.close();
		return conf;
	}

	private static OMElement createPayLoad() {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace("http://echoService", "ns1");
		OMElement method = fac.createOMElement("echo", omNs);
		OMElement value = fac.createOMElement("value", omNs);
		value.setText("Antoine");
		method.addChild(value);
		return method;
	}
	
	

}
