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
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bill on 7/5/15.
 */
public class EventErrorRecorder extends ValComponentBase implements ErrorRecorder {
    ErrorRecorderBuilder errorRecorderBuilder;
    List<ErrorRecorder> children = new ArrayList<>();
    static Logger logger = Logger.getLogger(EventErrorRecorder.class);

    // Should only be called from Factory class - EventErrorRecorderBuilder
    protected EventErrorRecorder(Event event) {  // only used by v3
        super(event);
    }

    protected EventErrorRecorder(Object o) {
        this((Event) o );
    }


    @Override
    public void err(XdsErrorCode.Code code, String msg, String location, String resource, Object log_message) {
        logger.info("ERROR - " + msg);
        String[] msgLines = msg.split("\\n");
        if (msgLines == null) return;
        for (int i=0; i<msgLines.length; i++) {
            Assertion a = new Assertion();
            a.setStatus(AssertionStatus.ERROR);
            a.setCode(code.name());
            a.setMsg(msgLines[i]);
            a.setLocation(location);
            a.setReference(resource);
            event.getAssertionGroup().addAssertion(a, true);
        }
    }

    @Override
    public void err(XdsErrorCode.Code code, String msg, String location, String resource) {
        logger.info("ERROR - " + msg);
        String[] msgLines = msg.split("\\n");
        if (msgLines == null) return;
        for (int i=0; i<msgLines.length; i++) {
            Assertion a = new Assertion();
            a.setStatus(AssertionStatus.ERROR);
            a.setCode(code.name());
            a.setMsg(msgLines[i]);
            a.setLocation(location);
            a.setReference(resource);
            event.getAssertionGroup().addAssertion(a, true);
        }
    }

    @Override
    public void err(XdsErrorCode.Code code, String msg, Object location, String resource) {
        logger.info("ERROR - " + msg);
        String[] msgLines = msg.split("\\n");
        if (msgLines == null) return;
        for (int i=0; i<msgLines.length; i++) {
            Assertion a = new Assertion();
            if (location != null) a.setLocation(location.getClass().getName());
            a.setStatus(AssertionStatus.ERROR);
            a.setCode(code.name());
            a.setMsg(msgLines[i]);
            a.setReference(resource);
            event.getAssertionGroup().addAssertion(a, true);
        }
    }

    @Override
    public void err(XdsErrorCode.Code code, Exception e) {
        logger.info("ERROR - " + e.getMessage());
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.ERROR);
        a.setCode(code.name());
        a.setMsg(e.getMessage());
        a.setLocation(ExceptionUtil.exception_details(e, 5));
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void err(XdsErrorCode.Code code, String msg, String location, String severity, String resource) {
        logger.info("ERROR - " + msg);
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.ERROR);
        a.setCode(code.name());
        a.setMsg(msg);
        a.setLocation(location);
        a.setReference(resource);
        event.getAssertionGroup().addAssertion(a, true);
        throw new ToolkitRuntimeException("How to handle String severity?????");
    }

    @Override
    public void err(String code, String msg, String location, String severity, String resource) {
        logger.info("ERROR - " + msg);
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
        logger.info("WARNING - " + msg);
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
        logger.info("WARNING - " + msg);
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.WARNING);
        a.setCode(code.name());
        a.setMsg(msg);
        a.setLocation(location);
        a.setReference(resource);
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void sectionHeading(String msg) {
        logger.info("Section - " + msg);
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.INFO);
        a.setMsg(msg);
        a.setLocation("SectionHeading");
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void challenge(String msg) {
        logger.info("Challenge - " + msg);
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.INFO);
        a.setMsg("..." + msg);
        a.setLocation("Challenge");
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void externalChallenge(String msg) {
        logger.info("ExternalChallenge - " + msg);
        Assertion a = new Assertion();
        a.setMsg(msg);
        a.setLocation("ExternalChallenge");
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void detail(String msg) {
        logger.info("Detail - " + msg);
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.INFO);
        a.setMsg("......." + msg);
        a.setLocation("Detail");
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void success(String dts, String msg, String found, String expected, String RFC) {
        logger.info("Success - " + msg);
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.SUCCESS);
        a.setExpected(expected);
        a.setFound(found);
        a.setMsg("......." + msg);
        a.setLocation(RFC);
        event.getAssertionGroup().addAssertion(a, true);
    }

    @Override
    public void error(String dts, String msg, String found, String expected, String RFC) {
        logger.info("Error - " + msg);
        Assertion a = new Assertion();
        a.setStatus(AssertionStatus.ERROR);
        a.setExpected(expected);
        a.setFound(found);
        a.setMsg("......." + msg);
        a.setLocation(RFC);
        event.getAssertionGroup().addAssertion(a, true);
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
//        throw new ToolkitRuntimeException("Not implemented");
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
        return buildNewErrorRecorder(event);
    }

    @Override
    public ErrorRecorder buildNewErrorRecorder(Object o) {
        if (o instanceof  Event) {
            ErrorRecorder er =  errorRecorderBuilder.buildNewErrorRecorder((Event) o);
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
