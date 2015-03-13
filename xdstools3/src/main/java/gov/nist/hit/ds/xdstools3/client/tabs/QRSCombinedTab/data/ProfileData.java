package gov.nist.hit.ds.xdstools3.client.tabs.QRSCombinedTab.data;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Data for the Profile selection grid
 * Created by dazais on 3/10/2015.
 */
public class ProfileData extends QRSDataModel {
    private static ListGridRecord[] records;


    private ProfileData(){super();}

    public static ListGridRecord[] getRecords() {
        if (records == null) {
            records = getNewRecords();
        }
        return records;
    }

    public static ListGridRecord[] getNewRecords() {
        return new ListGridRecord[]{
                createRecord("XDS", "profile"),
                createRecord("XDR", "profile"),
                createRecord("XCA", "profile")
        };
    }

}
