package gov.nist.hit.ds.eventLog
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.ValidatorErrorItem
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
/**
 * Created by bmajur on 7/28/14.
 */
class EventErrorRecorder implements ErrorRecorder {
    Event event

    EventErrorRecorder(Event event) { this.event = event }

    @Override
    void err(XdsErrorCode.Code code, String msg, String location, String resource, Object log_message) {
        assert false
    }

    @Override
    void err(XdsErrorCode.Code code, String msg, String location, String resource) {
        println "Error: ${code} -  ${msg} - ${location}"
    }

    @Override
    void err(XdsErrorCode.Code code, String msg, Object location, String resource) {
        println "Error: ${code} -  ${msg} - ${location}"
    }

    @Override
    void err(XdsErrorCode.Code code, Exception e) {
        println "Error: ${code} -  ${e.message}"
    }

    @Override
    void err(XdsErrorCode.Code code, String msg, String location, String severity, String resource) {
        assert false
    }

    @Override
    void err(String code, String msg, String location, String severity, String resource) {
        assert false
    }

    @Override
    void warning(String code, String msg, String location, String resource) {
        assert false
    }

    @Override
    void warning(XdsErrorCode.Code code, String msg, String location, String resource) {
        assert false
    }

    @Override
    void sectionHeading(String msg) {
        println "Section: ${msg}"
    }

    @Override
    void challenge(String msg) {
        println "Challenge: ${msg}"
    }

    @Override
    void externalChallenge(String msg) {
        assert false
    }

    @Override
    void detail(String msg) {
        println "Detail: ${msg}"
    }

    @Override
    void success(String dts, String name, String found, String expected, String RFC) {
        assert false
    }

    @Override
    void error(String dts, String name, String found, String expected, String RFC) {
        assert false
    }

    @Override
    void warning(String dts, String name, String found, String expected, String RFC) {
        assert false
    }

    @Override
    void info(String dts, String name, String found, String expected, String RFC) {
        assert false
    }

    @Override
    void summary(String msg, boolean success, boolean part) {
        assert false
    }

    void finish() {
        assert false
    }

    void showErrorInfo() {
        assert false
    }

    @Override
    boolean hasErrors() {
        return false
    }

    int getNbErrors() {
        return 0
    }

//    @Override
//    void concat(IAssertionGroup er) {
//        assert false
//    }

    @Override
    List<ValidatorErrorItem> getErrMsgs() {
        return null
    }

//    @Override
//    IAssertionGroup buildNewErrorRecorder() {
//        return null
//    }
//
//    @Override
//    IAssertionGroup getErrorRecorderBuilder() {
//        return null
//    }
}
