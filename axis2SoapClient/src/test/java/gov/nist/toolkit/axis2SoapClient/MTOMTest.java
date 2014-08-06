package gov.nist.toolkit.axis2SoapClient;

import gov.nist.toolkit.axis2SoapClient.impl.Axis2SoapClient;
import gov.nist.toolkit.axis2SoapClient.soapProcessing.OMProcessing;
import gov.nist.toolkit.soapClientAPI.RequestConfig;
import gov.nist.toolkit.soapClientAPI.SoapClient;
import gov.nist.toolkit.soapClientAPI.SoapClientFault;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.junit.Test;

public class MTOMTest extends BaseTest {

	
	 /* Ignored when building the module because it depends on the manual setup
	  	of the target service.*/
	 /*
	  * This proves that base64 encoded element are properly transmitted as MIME attachments by the axis2 client.
	  */
 	@Test
 	public void mtomRequest() throws IOException, SoapClientFault, XMLStreamException, FactoryConfigurationError {
 
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

	private RequestConfig getTestConfiguration() throws IOException {
		RequestConfig conf = new RequestConfig();

		// get properties
		InputStream is = MTOMTest.class.getResourceAsStream("/validConfig/soapRequestConfig.properties");
		conf.load(is);
		is.close();
		return conf;
	}

	private static OMElement createPayLoad() throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
		//create the input stream
		InputStream in = MTOMTest.class.getResourceAsStream("/xml/mtom.xml");
		Reader r = new InputStreamReader(in);
		OMElement img = OMProcessing.buildAxiomFromReader(r);
		
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace("http://echoService", "ns1");
		OMElement method = fac.createOMElement("echo", omNs);
		OMElement value = fac.createOMElement("value", omNs);
		value.addChild(img);
		method.addChild(value);
		return method;
	}
	
	

}
