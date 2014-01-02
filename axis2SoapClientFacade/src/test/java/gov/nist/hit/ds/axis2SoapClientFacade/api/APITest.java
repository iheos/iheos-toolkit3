package gov.nist.hit.ds.axis2SoapClientFacade.api;

import gov.nist.hit.ds.axis2SoapClientFacade.impl.Axis2ClientFacadeOperationClientImpl;
import gov.nist.hit.ds.axis2SoapClientFacade.soapProcessing.OMProcessing;

import java.io.IOException;
import java.io.InputStream;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.junit.Test;

public class APITest extends BaseTest {

	/*
	 * Ignored when building the module because it depends on the manual setup of the target service.
	 */
	@Test 
	public void runRequest() throws IOException {

		RequestConfig conf = getTestConfiguration();

		ConfigurationContext context = ConfigurationContextFactory.createConfigurationContextFromFileSystem(
				System.getProperty("user.dir")+
				"/src/test/resources/validConfig/axis2repo", 
				System.getProperty("user.dir")+
				"/src/test/resources/validConfig/axis2repo/conf/axis2.xml");

		Axis2SoapClientFacade c = new Axis2ClientFacadeOperationClientImpl(context);

		// get payload
		OMElement payload = createPayLoad();
		SOAPFactory fac = OMAbstractFactory.getSOAP12Factory();
		SOAPEnvelope envelope = fac.getDefaultEnvelope();
		envelope.getBody().addChild(payload);

		SOAPEnvelope res = c.runRequestSynchronously(conf, envelope);
		System.out.println(OMProcessing.toString(res));

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
