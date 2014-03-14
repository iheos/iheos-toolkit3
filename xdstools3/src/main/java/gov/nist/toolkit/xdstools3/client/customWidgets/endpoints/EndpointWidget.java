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
    public class EndpointWidget extends CanvasItem {  
    	
    	
    	public EndpointWidget (String name) {  
            super(name);  
            setEndRow(true);  
            setStartRow(true);    
            setShowTitle(false);
            
              
            // this is going to be an editable data item  
            setShouldSaveValue(true);  
              
            addShowValueHandler(new ShowValueHandler() {  
                @Override  
                public void onShowValue(ShowValueEvent event) {  
                    CanvasItem item = (CanvasItem) event.getSource();  
                      
                    ListGrid grid = (ListGrid)item.getCanvas();  
                    if (grid==null) return;  
                      
                    grid.deselectAllRecords();  
                    String value = (String) event.getDisplayValue();  
                    if (value==null) return;  
                      
                    RecordList recordList = grid.getDataAsRecordList();  
                    int index = recordList.findIndex(item.getFieldName(), value);  
                    grid.selectRecord(index);  
                }  
            });  
              
            setInitHandler(new FormItemInitHandler () {  
                @Override  
                public void onInit(FormItem item) {  
                    ListGrid grid = new ListGrid();   
                    grid.setLeaveScrollbarGap(false); 
                    grid.setWidth("*");  
                    grid.setHeight("*");
                    grid.setFields(((EndpointWidget) item).getGridFields());  
                    grid.setData(((EndpointWidget)item).getGridData());  
                    grid.setAutoFetchData(true);  
                      
                    grid.addDrawHandler(new DrawHandler() {  
                        @Override  
                        public void onDraw(DrawEvent event) {  
                            ListGrid grid = (ListGrid)event.getSource();  
                            RecordList data = grid.getDataAsRecordList();  
                            CanvasItem item = grid.getCanvasItem();  
                            String value = (String)item.getValue();  
                            String fieldName = item.getFieldName();  
                            if (value != null) grid.selectRecord(data.find(fieldName, value));                              
                        }  
                    });  
                      
                    grid.addSelectionUpdatedHandler(new SelectionUpdatedHandler() {  
                        @Override  
                        public void onSelectionUpdated(SelectionUpdatedEvent event) {  
                            ListGrid grid = (ListGrid) event.getSource();  
                            CanvasItem item = grid.getCanvasItem();  
                            ListGridRecord record = grid.getSelectedRecord();  
                            if (record != null) {  
                                item.storeValue(record.getAttribute(item.getFieldName()));  
                            } else {  
                                item.storeValue((com.smartgwt.client.data.Record)null);  
                            }  
                        }  
                    });  
                      
                    ((CanvasItem) item).setCanvas(grid);  
                }  
            });  
        }  
          
        private ListGridRecord[] gridData;  
        public void setGridData(ListGridRecord[] gridData) {  
            this.gridData = gridData;  
        }  
          
        public ListGridRecord[] getGridData() {  
            return gridData;  
        }  
          
        private ListGridField[] gridFields;  
        public void setGridFields(ListGridField... gridFields) {  
            this.gridFields = gridFields;  
        }  
          
        public ListGridField[] getGridFields() {  
            return gridFields;  
        }          
    };   // end class
      
    