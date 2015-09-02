package gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Created by Diane Azais local on 7/29/2015.
 */
public class FakeData {

    public Record createRecord(String testNumber, String sectionNumber){
        ListGridRecord record = new ListGridRecord();
        if (testNumber.equals("11012")){
            record.setCanExpand(true);
        } else {
            record.setCanExpand(false);
        }
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

    public Record createSubRecord(String testNumber, String sectionNumber){
        ListGridRecord record = new ListGridRecord();
        String testReference = testNumber.concat(sectionNumber);
        record.setAttribute("testNumber2", testNumber);
        record.setAttribute("sectionNumber2", sectionNumber);
        record.setAttribute("testReference2", testReference);
        record.setAttribute("testDescription2", "This is the test name / short description.");
        record.setAttribute("commands2", "");
        record.setAttribute("time2","04:10 PM EST");
        record.setAttribute("testStatus2","");
        return record;
    }
}
