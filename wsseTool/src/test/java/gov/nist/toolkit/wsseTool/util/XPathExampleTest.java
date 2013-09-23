package gov.nist.toolkit.wsseTool.util;
import static org.junit.Assert.*;
import gov.nist.hit.ds.wsseTool.api.exceptions.GenerationException;
import gov.nist.hit.ds.wsseTool.util.MyXmlUtils;
import gov.nist.toolkit.wsseTool.BaseTest;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XPathExampleTest extends BaseTest {

	
	private static Logger log = LoggerFactory.getLogger("");
	
	private String templateFile = "templates/basic_template_unsigned.xml";
	
	
    @Test
    public void test() throws SAXException, IOException, ParserConfigurationException, GenerationException{
    	
    	Document doc = MyXmlUtils.getDocumentWithResourcePath(templateFile);
    	XPathFactory xpf = XPathFactory.newInstance();
		XPath xp = xpf.newXPath();
		Node node;
    	
		try {
			node = (Node) xp.evaluate("//*[@Name='urn:oasis:names:tc:xacml:2.0:resource:resource-id']", doc.getDocumentElement(),
					XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw new GenerationException("could not update 'resource-id' attribute statement value", e);
		}
		
		assertTrue(node.getChildNodes().item(1).getTextContent()!= null);
		
    }
}