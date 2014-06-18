package gov.nist.hit.ds.simSupport.loader;

import gov.nist.hit.ds.utilities.string.StringUtil;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Parse the Property file that describes a collection of SimComponents.
 * Create an element in componentProperties for each SimComponent configured.
 * Example:
 element1.class=gov.nist.hit.ds.simSupport.loader.ByParamLogLoader
 element1.name=Log Loader
 element1.description=Load incoming HTTP header and body from logs
 element1.source=src/test/resources/simple

 element2.class=gov.nist.hit.ds.httpSoapValidator.validators.HttpMsgParser
 element2.name=HTTP Message Parser/Validator

 element3.class=gov.nist.hit.ds.httpSoapValidator.validators.SimpleSoapEnvironmentValidator
 element3.name=Verify HTTP environment describes SIMPLE SOAP
 element3.description=Verify not multipart and correct content-type

 element4.class=gov.nist.hit.ds.xmlValidator.XmlParser
 element4.name=XML Parser

 element5.class=gov.nist.hit.ds.httpSoapValidator.validators.SoapParser
 element5.name=SOAP Parser
 element5.description=Parses XML into SOAP Header and SOAP Body

 element6.class=gov.nist.hit.ds.httpSoapValidator.validators.SoapHeaderValidator
 element6.name=SOAP Header Validator
 element6.expectedWsAction=urn:ihe:iti:2007:RegisterDocumentSet-b
 *
 * .class indicates the Component implementation
 * .name and .description are documentation
 * Others, like .source or .expectedWsAction are parameters to the
 * Compoment.
 */
public class SimComponentPropFormatParser {
	Properties properties;
	List<Properties> componentProperties = new ArrayList<Properties>();
	boolean hasRun = false;
	
	public SimComponentPropFormatParser(Properties properties) {
		this.properties = properties;
	}
	
	public List<Properties> getComponentProperties() {
		run();
		return componentProperties;
	}
	
	public int size() {
		run();
		return componentProperties.size();
	}
	
	void run() {
		if (hasRun)
			return;
		hasRun = true;
		for (int i=1; i<9999; i++) {
			String prefix = getElementName(i);
			Properties p = getPropertiesThatStartWith(prefix);
			if (p.isEmpty())
				break;
			componentProperties.add(p);
		}
	}
	
	String getElementName(int i) {
		return "element" + i;
	}
	
	/**
	 * Remove prefix before stuffing into new Properties object.
	 * @param prefix
	 * @return
	 */
	Properties getPropertiesThatStartWith(String prefix) {
		Properties props = new Properties();
		if (!prefix.endsWith("."))
			prefix = prefix + ".";
		for (Enumeration<?> en = properties.propertyNames(); en.hasMoreElements(); ) {
			String name = (String) en.nextElement();
			String value = properties.getProperty(name);
			if (name.startsWith(prefix)) {
				String newName = StringUtil.removePrefix(name, prefix);
				props.setProperty(newName, value);
			}
		}
		return props;
	}
	
}
