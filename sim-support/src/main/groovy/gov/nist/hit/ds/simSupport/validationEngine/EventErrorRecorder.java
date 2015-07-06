package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus;
import gov.nist.hit.ds.xdsExceptions.ExceptionUtil;
import gov.nist.toolkit.errorrecording.ErrorRecorder;
import gov.nist.toolkit.errorrecording.client.ValidatorErrorItem;
import gov.nist.toolkit.errorrecording.client.XdsErrorCode;
import gov.nist.toolkit.errorrecording.factories.ErrorRecorderBuilder;
import gov.nist.toolkit.xdsexception.ToolkitRuntimeException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bill on 7/5/15.
 */
public class EventErrorRecorder extends ValComponentBase implements ErrorRecorder {
    ErrorRecorderBuilder errorRecorderBuilder;
    List<ErrorRecorder> children = new ArrayList<>();

    public EventErrorRecorder(Event event) {  // only used by v3
        super(event);
    }

    public EventErrorRecorder(Object o) {
        this((Event) o );
    }


    @Override
    public void err(XdsErrorCode.Code code, String msg, String location, String resource, Object log_message) {
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.ERROR);
        a.setCode(code);
        a.setMsg(msg);
        a.setLocation(location);
        a.setReference(resource);
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void err(XdsErrorCode.Code code, String msg, String location, String resource) {
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.ERROR);
        a.setCode(code);
        a.setMsg(msg);
        a.setLocation(location);
        a.setReference(resource);
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void err(XdsErrorCode.Code code, String msg, Object location, String resource) {
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.ERROR);
        a.setCode(code);
        a.setMsg(msg);
        a.setLocation(location);
        a.setReference(resource);
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void err(XdsErrorCode.Code code, Exception e) {
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.ERROR);
        a.setCode(code);
        a.setMsg(e.getMessage());
        a.setLocation(ExceptionUtil.exception_details(e, 5));
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void err(XdsErrorCode.Code code, String msg, String location, String severity, String resource) {
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.ERROR);
        a.setCode(code);
        a.setMsg(msg);
        a.setLocation(location);
        a.setReference(resource);
        event.getAssertionGroup().addAssertion(a, true);
        throw new ToolkitRuntimeException("How to handle String severity?????");
    }

    @Override
    public void err(String code, String msg, String location, String severity, String resource) {
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.ERROR);
        a.setCode(code);
        a.setMsg(msg);
        a.setLocation(location);
        a.setReference(resource);
        event.getAssertionGroup().addAssertion(a, true);
        throw new ToolkitRuntimeException("How to handle String severity?????");
    }

    @Override
    public void warning(String code, String msg, String location, String resource) {
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.WARNING);
        a.setCode(code);
        a.setMsg(msg);
        a.setLocation(location);
        a.setReference(resource);
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void warning(XdsErrorCode.Code code, String msg, String location, String resource) {
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.WARNING);
        a.setCode(code);
        a.setMsg(msg);
        a.setLocation(location);
        a.setReference(resource);
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void sectionHeading(String msg) {
        Assertion a = new Assertion();
        a.setMsg(msg);
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void challenge(String msg) {
        Assertion a = new Assertion();
        a.setMsg(msg);
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void externalChallenge(String msg) {
        Assertion a = new Assertion();
        a.setMsg(msg);
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void detail(String msg) {
        Assertion a = new Assertion();
        a.setMsg(msg);
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void success(String dts, String name, String found, String expected, String RFC) {
        throw new ToolkitRuntimeException("How to handle success with dts?????");
    }

    @Override
    public void error(String dts, String name, String found, String expected, String RFC) {
        throw new ToolkitRuntimeException("How to handle error with dts?????");
    }

    @Override
    public void warning(String dts, String name, String found, String expected, String RFC) {
        throw new ToolkitRuntimeException("How to handle warning with dts?????");
    }

    @Override
    public void info(String dts, String name, String found, String expected, String RFC) {
        throw new ToolkitRuntimeException("How to handle info with dts?????");
    }

    @Override
    public void summary(String msg, boolean success, boolean part) {
        Assertion a = new Assertion();
        if (!success) a.setStatus(AssertionStatus.ERROR);
        a.setMsg(msg);
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void finish() {

    }

    @Override
    public void showErrorInfo() {

    }

    @Override
    public boolean hasErrors() {
        return event.getAssertionGroup().hasErrors();
    }

    @Override
    public int getNbErrors() {
        return event.getAssertionGroup().getFailedAssertions().size();
    }

    @Override
    public void concat(ErrorRecorder er) {
        throw new ToolkitRuntimeException("How to handle concat?????");
    }

    @Override
    public List<ValidatorErrorItem> getErrMsgs() {
        throw new ToolkitRuntimeException("How to handle getErrMsgs?????");
    }

    @Override
    public List<ErrorRecorder> getChildren() {
        throw new ToolkitRuntimeException("How to handle getChildren?????");
    }

    @Override
    public int depth() {
        return 0;
    }

    @Override
    public ErrorRecorder buildNewErrorRecorder() {
        throw new ToolkitRuntimeException("Not Implemented");
    }

    @Override
    public ErrorRecorder buildNewErrorRecorder(Object o) {
        if (o instanceof  Event) {
            ErrorRecorder er =  errorRecorderBuilder.buildNewErrorRecorder();
            children.add(er);
            asPeer();
            if (er instanceof EventErrorRecorder) {
                EventErrorRecorder eer = (EventErrorRecorder) er;
                eer.event = event;
            }
            return er;
        }
        throw new ToolkitRuntimeException("EventErrorRecorder#buildNewErrorRecorder() - called with object that is not of class Event");
    }

}
