package gov.nist.hit.ds.eventLog.assertion.annotations;

import gov.nist.hit.ds.utilities.datatypes.RequiredOptional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validation {
	String version() default "ITI 2013";
	RequiredOptional required() default RequiredOptional.R;
	String msg();
	String id() default "[unassigned]"; 
	String[] ref() default {"none"};
	String[] dependsOn() default {"none"};
}
