package gov.nist.hit.ds.soapSupport;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.Fault;
import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorContext;
import gov.nist.hit.ds.eventLog.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode;

public class SoapFaultException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    FaultCode faultCode;
    String faultString;
    String faultActor;
    String faultDetail;

    public FaultCode getFaultCode() {
        return faultCode;
    }

    public String getFaultString() {
        return faultString;
    }

    public String getFaultActor() {
        return faultActor;
    }

    public String getFaultDetail() {
        return faultDetail;
    }

    public Fault asFault() {
        Fault f = new Fault();
        f.setFaultCode((faultCode == null) ? "" : faultCode.toString());
        f.setFaultMsg(faultString);
        f.setFaultActor(faultActor);
        f.setFaultDetail(faultDetail);
        return f;
    }

    /***************************************************
     *
     * AssertionGroup
     *
     ***************************************************/

    public SoapFaultException(IAssertionGroup er, FaultCode faultCode, String faultString, String faultActor, String faultDetail) {
        super(faultCode.toString() + ": " + faultString);
        this.faultCode = faultCode;
        this.faultString = faultString;
        this.faultActor = faultActor;
        this.faultDetail = faultDetail;
        if (er != null)
            er.err(XdsErrorCode.Code.SoapFault, this);
    }

    public SoapFaultException(IAssertionGroup er, FaultCode faultCode, String faultString) {
        this(er, faultCode, faultString, null, null);
    }

    public SoapFaultException(IAssertionGroup er, FaultCode faultCode, ErrorContext errorContext) {
        this(er, faultCode, errorContext.toString(), null, null);
    }

    public SoapFaultException(AssertionGroup ag, FaultCode faultCode, ErrorContext errorContext) {
        super(errorContext.getMsg());
        Assertion a = new Assertion();
        a.setFound(faultCode.name());
        a.setStatus(AssertionStatus.FAULT);
        a.setMsg(errorContext.getMsg());
        a.setReference(errorContext.getResource());
        ag.addAssertion(a);
        this.faultCode = faultCode;
        this.faultString = errorContext.getMsg();
        this.faultDetail = errorContext.getResource();
    }

    public SoapFaultException(AssertionGroup ag, FaultCode faultCode, String msg) {
        super(msg);
        Assertion a = new Assertion();
        a.setFound(faultCode.name());
        a.setStatus(AssertionStatus.FAULT);
        a.setMsg(msg);
        ag.addAssertion(a);
        this.faultCode = faultCode;
        this.faultString = msg;
    }

    public SoapFaultException(Event event, FaultCode faultCode, String msg) {
        this(event.getAssertionGroup(), faultCode, msg);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();

        buf.append("SoapFault: ");
        if (faultCode != null)
            buf.append(faultCode.toString());
        buf.append(": ").append(faultString);
        if (faultActor != null)
            buf.append("\nactor: ").append(faultActor);
        if (faultDetail != null)
            buf.append("\ndetail: ").append(faultDetail);

        return buf.toString();
    }
}
