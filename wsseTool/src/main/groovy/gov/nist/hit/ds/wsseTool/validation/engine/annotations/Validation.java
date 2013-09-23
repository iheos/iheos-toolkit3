package gov.nist.hit.ds.wsseTool.validation.engine.annotations;

import gov.nist.hit.ds.wsseTool.api.config.ValConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)

public @interface Validation {
	
	String version() default "1.0";
	String prefixId() default "MA";
	String id() default "[unassigned]"; // from the Messaging_Authorization_Test_Package_v3.0e.xlsx spreadsheet - last update : 9/28/2012
	String[] rtm() default {"none"}; // from RTM : Transport and Security Specification.pdf
	String category() default "required";
	int status() default ValConfig.Status.implemented;

	
} 
