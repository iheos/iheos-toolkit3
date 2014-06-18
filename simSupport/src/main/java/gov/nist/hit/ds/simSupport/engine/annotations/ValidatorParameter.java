package gov.nist.hit.ds.simSupport.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation does nothing but allow the developer to 
 * label set methods that are used by SimEngine to inject 
 * parameters into a validator. 
 * @author bill
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatorParameter {

}
