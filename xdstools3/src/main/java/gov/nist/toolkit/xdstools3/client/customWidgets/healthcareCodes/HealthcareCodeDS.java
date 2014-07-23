package gov.nist.toolkit.xdstools3.client.customWidgets.healthcareCodes;

import com.smartgwt.client.data.OperationBinding;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.DSProtocol;

/**
 * SmartGWT datasource for Healthcare Codes, to use with RESTful services.
 * Defines a RESTDataSource fields, operations and REST service URLs.
 */
public class HealthcareCodeDS extends RestDataSource {

        public HealthcareCodeDS() {

            // Set fields
            DataSourceTextField code = new DataSourceTextField("code");
            code.setPrimaryKey(true);
            code.setCanEdit(false);
            DataSourceTextField descr = new DataSourceTextField("description");
            setFields(code, descr);

            // Define operations
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
            setFetchDataURL("rest/healthcodes/read");
            setAddDataURL("rest/healthcodes/add");
            setUpdateDataURL("rest/healthcodes/update");
            setRemoveDataURL("rest/healthcodes/remove");
        }

}
