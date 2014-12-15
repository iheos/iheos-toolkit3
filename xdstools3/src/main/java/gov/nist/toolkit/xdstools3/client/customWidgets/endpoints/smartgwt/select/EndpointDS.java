package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.smartgwt.select;

import com.smartgwt.client.data.OperationBinding;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.DSProtocol;

/**
 * SmartGWT datasource for accessing entities over http in a RESTful manner.
 * Defines a RESTDataSource fields, scripts and REST service URLs.
 */
public class EndpointDS extends RestDataSource {
	
	public EndpointDS() {

        // Set fields
		DataSourceTextField endpointId = new DataSourceTextField("id");
		endpointId.setPrimaryKey(true);
		endpointId.setCanEdit(false);
		DataSourceTextField endpointName = new DataSourceTextField("name");
        DataSourceTextField endpointType = new DataSourceTextField("type");
        setFields(endpointId, endpointName, endpointType);

        // Define scripts
		OperationBinding fetch = new OperationBinding();
		fetch.setOperationType(DSOperationType.FETCH);
		fetch.setDataProtocol(DSProtocol.POSTMESSAGE);
		OperationBinding add = new OperationBinding();
		add.setOperationType(DSOperationType.ADD);
		add.setDataProtocol(DSProtocol.POSTMESSAGE);
		OperationBinding update = new OperationBinding();
		update.setOperationType(DSOperationType.UPDATE);
		update.setDataProtocol(DSProtocol.POSTMESSAGE);
		OperationBinding remove = new OperationBinding();
		remove.setOperationType(DSOperationType.REMOVE);
		remove.setDataProtocol(DSProtocol.POSTMESSAGE);
		setOperationBindings(fetch, add, update, remove);

        // Define REST URLs
		setFetchDataURL("rest/message/read");
		setAddDataURL("rest/message/add");
		setUpdateDataURL("rest/message/update");
		setRemoveDataURL("rest/message/remove");
	}
}
