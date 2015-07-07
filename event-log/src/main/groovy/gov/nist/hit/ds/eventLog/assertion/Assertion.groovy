package gov.nist.hit.ds.eventLog.assertion

import gov.nist.hit.ds.utilities.datatypes.RequiredOptional


class Assertion {
    String id = "";
//    String name = "";
    AssertionStatus status = AssertionStatus.SUCCESS;
    String found = "";
    String expected = "";
    def reference = [];
    String msg = "";
    String code = "";
    String location = "";
    RequiredOptional requiredOptional = RequiredOptional.R;
    boolean saved = false
    String defaultMsg = false   // overwritten by real message

    String expectedFoundString() {
        if (expected == '' && found == '') return ''
        return "Expected: ${expected}; Found: ${found}"
    }

    boolean failed() { return status.isError(); }

    boolean internalError() { return status == AssertionStatus.INTERNALERROR }

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
    void msg(String value) { msg = value }
    public String getFound() { found }
    public String getExpected() { expected }
}
