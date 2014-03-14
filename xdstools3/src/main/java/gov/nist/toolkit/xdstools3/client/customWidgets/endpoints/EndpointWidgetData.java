package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints;

  
import com.smartgwt.client.widgets.grid.ListGridRecord;
  
/**
 * Loads the data for a list of actors / endpoints of type EndpointWidget.
 * @author dazais
 *
 */
public class EndpointWidgetData {  
  
    private static ListGridRecord[] records;    
        
    public static ListGridRecord[] getRecords() {  
        if (records == null) {  
            records = getNewRecords();    
        }    
        return records;    
    }    
    
    public static ListGridRecord createRecord(String actorName) {  
        ListGridRecord record = new ListGridRecord();  
        record.setAttribute("actorName", actorName);  
       // record.setAttribute("actorType", actorType); 
        // add additional actor properties here
     
        return record;  
    }  
  
    public static ListGridRecord[] getNewRecords() {  
        return new ListGridRecord[]{  
               createRecord("Tiani"),
               createRecord("Pub"),
               createRecord("GE"),
               createRecord("EMC"),
               createRecord("Fore"),
        };  
    }  
}  