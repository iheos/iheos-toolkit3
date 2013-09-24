package gov.nist.hit.ds.soapSupport.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidationFault {
	String version() default "ITI 2013";
	String prefixId() default "NA";
	String id() default "[unassigned]"; 
	String ref() default "none"; 
	FaultCode faultCode();
}
