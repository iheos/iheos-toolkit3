package gov.nist.hit.ds.testEngine.mgmt;

import gov.nist.hit.ds.utilities.xml.XmlUtil;

import java.util.HashMap;
import java.util.Map;

import org.apache.axiom.om.OMElement;

public class TestPlan {
	OMElement testplanEle;
	Map<String, TestStep> steps;
	
	public TestPlan(OMElement testplanEle) throws Exception {
		this.testplanEle = testplanEle;
		parse();
	}
	
	void parse() throws Exception {
		steps = new HashMap<String, TestStep>();
		
		for (OMElement ele : XmlUtil.childrenWithLocalName(testplanEle, "TestStep")) {
			String name = ele.getAttributeValue(XmlUtil.id_qname);
			if (name == null)
				throw new Exception("Testplan has TestStep without id attribute");
			steps.put(name, new TestStep(ele));
		}
	}
}
