package gov.nist.hit.ds.simSupport.validationEngine.annotation;

import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidationFault {
	String version() default "ITI 2013";
	RequiredOptional required() default RequiredOptional.R;
	String id();
	String msg();
	String[] ref() default {"none"}; 
	FaultCode faultCode();
	String[] dependsOn() default {"none"};
}
