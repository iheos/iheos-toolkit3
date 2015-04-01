package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt;

import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * SmartGWT datasource for accessing entities over http in a RESTful manner.
 * Defines a RESTDataSource fields, scripts and REST service URLs.
 */
public class EndpointConfigDS_old extends RestDataSource {

    private static EndpointConfigDS_old instance = null;

    public static EndpointConfigDS_old getInstance() {
        if (instance == null) {
            instance = new EndpointConfigDS_old();
        }
        return instance;
    }

	private EndpointConfigDS_old() {
        setID("supplyCategoryDS");
        setDataURL("resources/datasources/categories.data.xml");
        setRecordXPath("/List/supplyCategory");

        // Set fields
		DataSourceTextField endpointId = new DataSourceTextField("id");
		endpointId.setPrimaryKey(true);
		endpointId.setCanEdit(false);

        DataSourceTextField categoryName = new DataSourceTextField("categoryName", "Category", 128, true);
        //categoryName.setPrimaryKey(true);

        DataSourceTextField parentField = new DataSourceTextField("parentID", null);
        parentField.setHidden(true);
        parentField.setRequired(true);
        parentField.setRootValue("root");
        parentField.setForeignKey("supplyCategoryDS.categoryName");
        parentField.setPrimaryKey(true);

        setFields(categoryName, parentField);



        // Define REST URLs - not used for now
        setDataURL("rest/endpointconfig");
	}
}
