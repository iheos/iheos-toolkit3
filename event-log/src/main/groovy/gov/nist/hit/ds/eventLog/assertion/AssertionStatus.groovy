package gov.nist.hit.ds.eventLog.assertion

/**
 * Created by bill on 4/15/14.
 */
enum AssertionStatus {
    NONE, INFO, FALSE, TRUE, SUCCESS, WARNING, ERROR, FAULT, INTERNALERROR;

    AssertionStatus getWorst(AssertionStatus asst) {
        if (asst.ordinal() > this.ordinal()) return asst
        return this
    }

    boolean isError() {
        return (ordinal() >= ERROR.ordinal())
    }

}