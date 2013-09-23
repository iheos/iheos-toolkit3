package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.eventLog.assertion.Validation;
import gov.nist.hit.ds.soapSupport.core.FaultCode;
import gov.nist.hit.ds.soapSupport.core.ValidationFault;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.xdsException.ExceptionUtil;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class ValidationEngine {
	SimComponentBase validationObject;
	public ValidationFault valFault;
	public Validation val;
	static Logger logger = Logger.getLogger(ValidationEngine.class);

	public ValidationEngine(SimComponentBase validationObject) {
		this.validationObject = validationObject;
	}
	
	public void run() throws SoapFaultException {
		try {
			innerRun();
		} catch (SoapFaultException e) {
			throw e;  // pass it up
		} catch (Exception e) {
			logger.fatal(ExceptionUtil.exception_details(e));
			throw new SoapFaultException(
					validationObject.ag,
					FaultCode.Receiver,
					new ErrorContext(ExceptionUtil.exception_details(e))
					);
		}
	}

	public void innerRun() throws Exception {
		Class<?> clazz = validationObject.getClass();
		logger.debug("Running ValidationEngine on " + clazz.getName());
		Method[] valMethods = clazz.getMethods();
		// For all validations
		for (int methI=0; methI<valMethods.length; methI++) {
			Method meth = valMethods[methI];
//			logger.debug("Method " + meth.getName());
			Class<?>[] subParamTypes = meth.getParameterTypes();
			if (meth.isAnnotationPresent(Validation.class)) {
				if (subParamTypes != null && subParamTypes.length > 0) 
					throw new Exception("Validation <" + clazz.getName() + "#" + meth.getName() + "> : a validation method accepts no parameters");
				
				
				
			} else if (meth.isAnnotationPresent(ValidationFault.class)) {
				if (subParamTypes != null && subParamTypes.length > 0) 
					throw new Exception("Validation <" + clazz.getName() + "#" + meth.getName() + "> : a validation method accepts no parameters");
				
				logger.debug("Calling " + meth.getName());

				valFault = meth.getAnnotation(ValidationFault.class);
				val = null;
					meth.invoke(validationObject);
			}
		}

	}

}
