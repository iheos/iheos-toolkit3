package gov.nist.hit.ds.testEngine.logging;


import gov.nist.hit.ds.utilities.xml.OMFormatter;
import gov.nist.hit.ds.utilities.xml.Util;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.util.ArrayList;
import java.util.Map;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNode;

public class OmLogger implements ILogger {

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#add_simple_element(org.apache.axiom.om.OMElement, java.lang.String)
	 */
	@Override
	public OMElement add_simple_element(OMElement parent, String name) {
		OMElement ele = XmlUtil.om_factory.createOMElement(name, null);
		parent.addChild(ele);
		return ele;
	}

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#add_simple_element_with_id(org.apache.axiom.om.OMElement, java.lang.String, java.lang.String)
	 */
	@Override
	public OMElement add_simple_element_with_id(OMElement parent, String name,
			String id) {
		OMElement ele = add_simple_element(parent, name);
		ele.addAttribute("id", id, null);
		return ele;
	}

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#add_simple_element_with_id(org.apache.axiom.om.OMElement, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public OMElement add_simple_element_with_id(OMElement parent, String name,
			String id, String value) {
		if (name == null) name = "";
		if (id == null) id = "";
		if (value == null) value = "";
		OMElement ele = add_simple_element(parent, name);
		ele.addAttribute("id", id, null);
		ele.addChild(XmlUtil.om_factory.createOMText(encodeLT(value)));
		return ele;
	}

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#add_name_value(org.apache.axiom.om.OMElement, java.lang.String, java.util.ArrayList)
	 */
	@Override
	public void add_name_value(OMElement parent, String name, ArrayList<OMElement> data) {
		for (OMElement ele : data) {
			OMElement elel = XmlUtil.om_factory.createOMElement(name, null);
			try {
				elel.addChild(Util.deep_copy(ele));
			} catch (XdsInternalException e) {
				e.printStackTrace();
			}
			parent.addChild(elel);
		}
	}
	
	String encodeLT(String msg) {
		return msg;
	}

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#add_name_value(org.apache.axiom.om.OMElement, java.lang.String, java.util.Map)
	 */
	@Override
	public void add_name_value(OMElement parent, String name, Map<String, String> data) {
		OMElement elel = XmlUtil.om_factory.createOMElement(name, null);
		parent.addChild(elel);
		elel.setText(encodeLT(data.toString()));
	}

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#add_name_value(org.apache.axiom.om.OMElement, java.lang.String, java.lang.String)
	 */
	@Override
	public OMElement add_name_value(OMElement parent, String name, String value) {
		System.out.println(name + ": " + value);
		OMElement ele = XmlUtil.om_factory.createOMElement(name, null);
		ele.addChild(XmlUtil.om_factory.createOMText(encodeLT(value)));
		parent.addChild(ele);
		return ele;
	}

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#add_name_value_with_id(org.apache.axiom.om.OMElement, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public OMElement add_name_value_with_id(OMElement parent, String name, String id, String value) {
		if (name == null) name = "";
		if (id == null) id = "";
		if (value == null) value = "";
		OMElement ele = XmlUtil.om_factory.createOMElement(name, null);
		ele.addAttribute("id", id, null);
		ele.addChild(XmlUtil.om_factory.createOMText(encodeLT(value)));
		parent.addChild(ele);
		return ele;
	}

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#add_name_value(org.apache.axiom.om.OMElement, java.lang.String, org.apache.axiom.om.OMElement)
	 */
	@Override
	public OMElement add_name_value(OMElement parent, String name, OMElement value) {
		OMNode val = value;
		OMElement ele = XmlUtil.om_factory.createOMElement(name, null);
		if (val == null)
			val = XmlUtil.om_factory.createOMElement("None", null);
		else {
			try {
//				if (name.equals("InputMetadata")) {
//					System.out.println("InputMetadata:\n" + new OMFormatter(value).toString());
//				}
				val = Util.deep_copy(value);
			} catch (Exception e) {}
		}
		try {
			ele.addChild(val);
		}
		catch (OMException e) {
			Util.mkElement("Exception", "Exception writing log content\n" + OMFormatter.encodeAmp(ExceptionUtil.exception_details(e))
					+ "\n" + new OMFormatter(value).toString(), ele);
		}
		parent.addChild(ele);
		return ele;
	}

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#add_name_value(org.apache.axiom.om.OMElement, java.lang.String, org.apache.axiom.om.OMElement, org.apache.axiom.om.OMElement)
	 */
	@Override
	public OMElement add_name_value(OMElement parent, String name, OMElement value1, OMElement value2) {
		OMElement ele = XmlUtil.om_factory.createOMElement(name, null);
		OMNode val1 = value1;
		if (val1 == null)
			val1 = XmlUtil.om_factory.createOMText("null");
		ele.addChild(val1);
		OMNode val2 = value2;
		if (val2 == null)
			val2 = XmlUtil.om_factory.createOMText("null");
		ele.addChild(val2);
		parent.addChild(ele);
		return ele;
	}

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#add_name_value(org.apache.axiom.om.OMElement, java.lang.String, org.apache.axiom.om.OMElement, org.apache.axiom.om.OMElement, org.apache.axiom.om.OMElement)
	 */
	@Override
	public OMElement add_name_value(OMElement parent, String name, OMElement value1, OMElement value2, OMElement value3) {
		OMElement ele = XmlUtil.om_factory.createOMElement(name, null);
		OMNode val1 = value1;
		if (val1 == null)
			val1 = XmlUtil.om_factory.createOMText("null");
		ele.addChild(val1);
		OMNode val2 = value2;
		if (val2 == null)
			val2 = XmlUtil.om_factory.createOMText("null");
		ele.addChild(val2);
		OMNode val3 = value3;
		if (val3 == null)
			val3 = XmlUtil.om_factory.createOMText("null");
		ele.addChild(val3);
		parent.addChild(ele);
		return ele;
	}

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#create_name_value(java.lang.String, org.apache.axiom.om.OMElement)
	 */
	@Override
	public OMElement create_name_value(String name, OMElement value) {
		OMNode val = value;
		OMElement ele = XmlUtil.om_factory.createOMElement(name, null);
		if (val == null)
			val = XmlUtil.om_factory.createOMText("null");
		ele.addChild(val);
		return ele;
	}

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#create_name_value(java.lang.String, java.lang.String)
	 */
	@Override
	public OMElement create_name_value(String name, String value) {
		OMElement ele = XmlUtil.om_factory.createOMElement(name, null);
		ele.addChild(XmlUtil.om_factory.createOMText(value));
		return ele;
	}

	/* (non-Javadoc)
	 * @see gov.nist.toolkit.testengine.ILogger#add_name_value(org.apache.axiom.om.OMElement, org.apache.axiom.om.OMElement)
	 */
	@Override
	public OMElement add_name_value(OMElement parent, OMElement element) {
		parent.addChild(element);
		return element;
	}

}