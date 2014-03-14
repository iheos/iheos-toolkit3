package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Holds lists of both initiating and receiving endpoints.
 * 
 * @author dazais
 * @see EndpointWidget
 */
public class AllEndpointsWidget extends HLayout {

	public AllEndpointsWidget() {
		
		// Widget: Initiators
		final DynamicForm actors = new DynamicForm();    
		EndpointWidget actorField = new EndpointWidget("Initiators");  
		actorField.setGridData(EndpointWidgetData.getRecords());  
		actorField.setGridFields(new ListGridField("actorName", "Initiators"));  
		actors.setFields(actorField); 
		actors.setWidth100();

		// Widget: Receivers
		final DynamicForm transactions = new DynamicForm();   
		EndpointWidget transactionField = new EndpointWidget("Receivers");  
		transactionField.setGridData(EndpointWidgetData.getRecords());  
		transactionField.setGridFields(new ListGridField("actorName", "Receivers"));  
		transactions.setFields(transactionField);  
		transactions.setWidth100();
		//transactions.setValues(initialValues);

		// Add all to panel
		addMembers(actors, transactions);
		actors.setWidth("10%");
		transactions.setWidth("10%");

	}

}
