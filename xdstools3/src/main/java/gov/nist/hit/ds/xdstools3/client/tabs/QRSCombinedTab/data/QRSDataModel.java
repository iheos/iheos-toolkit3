package gov.nist.hit.ds.xdstools3.client.tabs.QRSCombinedTab.data;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Data model for the selection grids. See *Data classes for extensions of this class dedicated to each grid.
 * The purpose of this class is to store the data functions common to all *Data singletons.
 *
 * Created by dazais on 3/10/2015.
 */
public class QRSDataModel {

    public QRSDataModel(){}


    public static ListGridRecord createRecord(String property, String value) {
        ListGridRecord record = new ListGridRecord();
        record.setAttribute(value, property);
        return record;
    }


}
