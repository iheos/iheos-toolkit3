package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints;

/** Based on SmartGWT showcase 4.0p 
 *  Smart GWT (GWT for SmartClient) 
 *  Copyright 2008 and beyond, Isomorphic Software, Inc. 
 *  
 *  Smart GWT is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License version 3 
 * is published by the Free Software Foundation.  Smart GWT is also 
 * available under typical commercial license terms - see 
 * http://smartclient.com/license 
 *  
 * **/

  
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemInitHandler;
import com.smartgwt.client.widgets.form.fields.events.ShowValueEvent;
import com.smartgwt.client.widgets.form.fields.events.ShowValueHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedHandler;
  
/**
 * Creates an organized table that can be sorted
 * 
 * @author dazais
 *
 */
    public class EndpointWidget extends ListGrid {
    	
    	
    	public EndpointWidget () {
            setShowAllRecords(true);
          //  setDataSource(EndpointsDS.getInstance());
            // TODO needs to call on the Smartgwt server here to get datasource contents
            setAutoFetchData(true);
            draw();
            setWidth(500);
            setHeight(224);
        }
          

    };   // end class
      
    