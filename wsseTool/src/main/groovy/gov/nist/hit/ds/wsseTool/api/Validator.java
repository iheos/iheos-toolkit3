package gov.nist.hit.ds.wsseTool.api;

import gov.nist.hit.ds.wsseTool.api.config.Context;
import gov.nist.hit.ds.wsseTool.api.exceptions.ValidationException;

import org.w3c.dom.Element;

public interface Validator {

	public void validate(Element xml, Context context) throws ValidationException;

}
