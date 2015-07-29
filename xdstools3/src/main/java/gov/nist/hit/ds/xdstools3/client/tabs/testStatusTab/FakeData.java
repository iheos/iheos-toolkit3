package gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Created by Diane Azais local on 7/29/2015.
 */
public class FakeData {

    public Record createRecord(String testNumber, String sectionNumber){
        ListGridRecord record = new ListGridRecord();
        String testReference = testNumber.concat(sectionNumber);
        record.setAttribute("testNumber", testNumber);
        record.setAttribute("sectionNumber", sectionNumber);
        record.setAttribute("testReference", testReference);
        record.setAttribute("testDescription", "This is the test name / short description.");
        record.setAttribute("commands", "");
        record.setAttribute("time","04:10 PM EST");
        record.setAttribute("testStatus","");
        return record;
    }

}
