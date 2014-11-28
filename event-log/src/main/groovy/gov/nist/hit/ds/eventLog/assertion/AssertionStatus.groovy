package gov.nist.hit.ds.eventLog.assertion;

/**
 * Created by bill on 4/15/14.
 */
public enum AssertionStatus {
    NONE, INFO, FALSE, TRUE, SUCCESS, WARNING, ERROR, FAULT, INTERNALERROR;

    public AssertionStatus getWorst(AssertionStatus asst) {
        if (asst.ordinal() > this.ordinal()) return asst;
        return this;
    }

    static AssertionStatus getWorst(statuses) {
        AssertionStatus worst = NONE
        statuses.each { worst = it.getWorst(worst)}
        return worst
    }

    public boolean isError() {
        return (ordinal() >= ERROR.ordinal());
    }

}