package gov.nist.hit.ds.xdstools3.client.tabs.QRSCombinedTab;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * DataSource singleton
 * Created by dazais on 3/10/2015.
 */
public class QueryRetrieveData {

    private static ListGridRecord[] records;

    public static ListGridRecord[] getRecords() {
        if (records == null) {
            records = getNewRecords();
        }
        return records;
    }


    public static ListGridRecord createRecord(String profile) {
        ListGridRecord record = new ListGridRecord();
        record.setAttribute("profile", profile);
        return record;
    }

    public static ListGridRecord[] getNewRecords() {
        return new ListGridRecord[]{
                createRecord("XDS"),
                createRecord("XDR"),
                createRecord("XCA")
        };
    }


}
