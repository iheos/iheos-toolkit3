package gov.nist.hit.ds.simSupport.validationEngine.annotation

import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Created by bmajur on 8/21/14.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface ErrorCode {
    XdsErrorCode.Code code()
}
