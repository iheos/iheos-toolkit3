package gov.nist.hit.ds.eventLog.assertion

import gov.nist.hit.ds.utilities.csv.CSVEntry
import gov.nist.hit.ds.utilities.datatypes.RequiredOptional

/**
 * Created by bill on 4/15/14.
 */
class AssertionDAO {

    /**
     * The following elements are all sensitive to the order and identity of the fields defined.
     */
    def columnNames =  [ "ID", "STATUS", "R/O", "EXPECTED", "FOUND", "MSG", "CODE", "LOCATION", "REFERENCE" ];

    public CSVEntry asCVSEntry(a) {
        if (a instanceof CSVEntry) return a
        CSVEntry entry = new CSVEntry();

        entry.
                add(a.id).    					            // 0
                add(a.status.name()).				            // 1
                add(a.requiredOptional.name()).	            // 2
                add(nocomma(a.expected)).			            // 3
                add(nocomma(a.found)).			            // 4
                add(nocomma(a.msg)).				            // 5
                add(a.code).						            // 6
                add(a.location).					            // 7
                add(nocomma(buildSemiDivided(a.reference)))  // 8

        return entry;
    }

    Assertion asAssertion(entry) {
        if (entry instanceof Assertion) return entry
        entry as CSVEntry
        Assertion a = new Assertion()
        a.with {
            id = entry.get(0)
            status = AssertionStatus.valueOf(entry.get(1))
            requiredOptional = RequiredOptional.valueOf(entry.get(2))
            expected = entry.get(3)
            found = entry.get(4)
            msg = entry.get(5)
            code = entry.get(6)
            location = entry.get(7)
            reference = parseSemiDivided(entry.get(8))
        }
        a
    }

    /**
     * The above elements are all sensitive to the order and identity of the fields defined.
     */

    def nocomma(str) {
        if (str == null) return "";
        return str.replaceAll(",", ";");
    }

    public String buildSemiDivided(String[] values) { buildSemiDivided(values as List) }

    def buildSemiDivided(List values) {
        StringBuffer buf = new StringBuffer();

        if (values?.size() > 0)
            buf.append(values[0]);
        for (int i=1; i<values?.size(); i++) {
            buf.append(";").append(values[i]);
        }

        return buf.toString();
    }

    def parseSemiDivided(String str) {
        if (str == null)
            return [];
        String[] values = str.split(";");
        if (values == null) {
            String[] val = { str } ;
            return  val;
        }
        return values;
    }


}
