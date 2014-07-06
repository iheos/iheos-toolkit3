package gov.nist.hit.ds.eventLog.errorRecording;

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.eventLog.errorRecording.client.ValidatorErrorItem;
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.eventLog.errorRecording.factories.ErrorRecorderBuilder;

import java.util.List;

/**
 * Created by bill on 4/15/14.
 */
public class TempErrorRecorderAdaptor extends AssertionGroup implements IAssertionGroup  {
    IAssertionGroup iag;

    public TempErrorRecorderAdaptor(IAssertionGroup ag) {
        this.iag = ag;
    }

    @Override
    public void err(XdsErrorCode.Code code, ErrorContext context, Object location) {

    }

    @Override
    public void err(XdsErrorCode.Code code, Exception e) {

    }

    @Override
    public void warning(String code, ErrorContext context, String location) {

    }

    @Override
    public void warning(XdsErrorCode.Code code, ErrorContext context, String location) {

    }

    @Override
    public void sectionHeading(String msg) {

    }

    @Override
    public void challenge(String msg) {

    }

    @Override
    public void externalChallenge(String msg) {

    }

    @Override
    public void detail(String msg) {

    }

    @Override
    public void success(String dts, String name, String found, String expected, String RFC) {

    }

    @Override
    public void error(String dts, String name, String found, String expected, String RFC) {

    }

    @Override
    public void warning(String dts, String name, String found, String expected, String RFC) {

    }

    @Override
    public void info(String dts, String name, String found, String expected, String RFC) {

    }

    @Override
    public void summary(String msg, boolean success, boolean part) {

    }

    @Override
    public void finish() {

    }

    @Override
    public void showErrorInfo() {

    }

    @Override
    public boolean hasErrors() {
        return false;
    }

    @Override
    public int getNbErrors() {
        return 0;
    }

    @Override
    public void concat(IAssertionGroup er) {

    }

    @Override
    public List<ValidatorErrorItem> getErrMsgs() {
        return null;
    }

    @Override
    public ErrorRecorderBuilder getErrorRecorderBuilder() {
        return null;
    }

    @Override
    public IAssertionGroup buildNewErrorRecorder() {
        return null;
    }
}
