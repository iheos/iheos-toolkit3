package gov.nist.hit.ds.eventLog.assertion

import gov.nist.hit.ds.eventLog.errorRecording.RequiredOptional;

class Assertion {
    String id = "";
//    String name = "";
    AssertionStatus status = AssertionStatus.SUCCESS;
    def found = "";
    def expected = "";
    def reference = [];
    def msg = "";
    def code = "";
    def location = "";
    RequiredOptional requiredOptional = RequiredOptional.R;
    def saved = false

    boolean failed() { return status.isError(); }

    public String toString() {
        new AssertionDAO().asCVSEntry(this).toString()
    }

    def equals(Assertion a) {
        id == a.id &&
                status == a.status &&
                found == a.found &&
                expected == a.expected &&
                reference == a.reference &&
                msg == a.msg &&
                code == a.code &&
                location == a.location &&
                requiredOptional == a.requiredOptional
    }

    public String getMsg() { msg }
    public String getFound() { found }
    public String getExpected() { expected }
}
