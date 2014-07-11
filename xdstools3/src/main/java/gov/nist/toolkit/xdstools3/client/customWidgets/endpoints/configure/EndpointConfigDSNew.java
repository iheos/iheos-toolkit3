package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.configure;

import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * SmartGWT datasource for accessing entities over http in a RESTful manner.
 * Defines a RESTDataSource fields, operations and REST service URLs.
 */
public class EndpointConfigDSNew extends RestDataSource {


    private static EndpointConfigDSNew instance = null;

    public static EndpointConfigDSNew getInstance() {
        if (instance == null) {
            instance = new EndpointConfigDSNew();
        }
        return instance;
    }

	private EndpointConfigDSNew() {
        setID("endpointConfigDS");
        setDataURL("resources/datasources/endpoints/_actors.data.xml");
        setRecordXPath("/sites/site"); // this is the path to the record we want to display, inside the XML file holding the data
        setClientOnly(true);

        DataSourceTextField endpointName = new DataSourceTextField("endpointName", "Endpoint Name");
        endpointName.setRequired(true);
        endpointName.setPrimaryKey(true);


//        DataSourceTextField parentID = new DataSourceTextField("parentID");
//        parentID.setHidden(true);
//        parentID.setRootValue("root");
//        parentID.setForeignKey("transactionDS.name");


        setFields(endpointName);

    }
}
