package gov.nist.toolkit.xdstools3.client.restDatasourceTest;

import com.smartgwt.client.data.OperationBinding;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.DSProtocol;

/**
 * SmartGWT datasource for accessing {@link gov.nist.toolkit.xdstools3.client.restDatasourceTest.message.Message} entities over http in a RESTful manner.
 */
public class MessageDS extends RestDataSource {
	
	public MessageDS() {
		setID("MessageDS");
		DataSourceTextField messageId = new DataSourceTextField("id");
		messageId.setPrimaryKey(true);
		messageId.setCanEdit(false);
		
		DataSourceTextField messageValue = new DataSourceTextField("value");
		setFields(messageId, messageValue);
		
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
				
		setFetchDataURL("rest/message/read");
		setAddDataURL("rest/message/add");
		setUpdateDataURL("rest/message/update");
		setRemoveDataURL("rest/message/remove");
	}
}
