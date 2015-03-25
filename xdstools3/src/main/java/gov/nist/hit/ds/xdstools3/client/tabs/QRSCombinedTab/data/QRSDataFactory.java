package gov.nist.hit.ds.xdstools3.client.tabs.QRSCombinedTab.data;


import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Created by dazais on 3/12/2015.
 */
public class QRSDataFactory {

    public static ListGridRecord[] getRecord(String type){
        String formattedStr = type.toLowerCase();

        if (formattedStr == "profile"){
            return ProfileData.getRecords();
        }
        else if (formattedStr == "from"){
            return FromData.getRecords();
        }
        else if (formattedStr == "to"){
            return ToData.getRecords();
        }
        else if (formattedStr == "transaction"){
            return TransactionData.getRecords();
        }
        else {
            return new ListGridRecord[]{};
        }
    }

}
