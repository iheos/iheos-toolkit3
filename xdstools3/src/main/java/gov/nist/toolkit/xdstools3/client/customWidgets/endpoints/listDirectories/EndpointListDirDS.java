package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.listDirectories;

import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * SmartGWT datasource for accessing entities over http in a RESTful manner.
 * Defines a RESTDataSource fields, operations and REST service URLs.
 */
public class EndpointListDirDS extends RestDataSource {

    private static EndpointListDirDS instance = null;

    public static EndpointListDirDS getInstance() {
        if (instance == null) {
            instance = new EndpointListDirDS();
        }
        return instance;
    }

	private EndpointListDirDS() {
        setID("endpointListDir");
        setDataURL("resources/datasources/categories.data.xml");
        setRecordXPath("/List/supplyCategory");

        // Set fields
		DataSourceTextField endpointId = new DataSourceTextField("id");
		endpointId.setPrimaryKey(true);
		endpointId.setCanEdit(false);
        DataSourceTextField categoryName = new DataSourceTextField("categoryName", "Category", 128, true);
        categoryName.setPrimaryKey(true);

        DataSourceTextField parentField = new DataSourceTextField("parentID", null);
        parentField.setHidden(true);
        parentField.setRequired(true);
        parentField.setRootValue("root");
        parentField.setForeignKey("supplyCategoryDS.categoryName");

        setFields(categoryName, parentField);



        // Define REST URLs - not used for now
		//setFetchDataURL("rest/endpointconfig/read");
		//setAddDataURL("rest/endpointconfig/add");
		//setUpdateDataURL("rest/endpointconfig/update");
		//setRemoveDataURL("rest/endpointconfig/remove");
	}
}
