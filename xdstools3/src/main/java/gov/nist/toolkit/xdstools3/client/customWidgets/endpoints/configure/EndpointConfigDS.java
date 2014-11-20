package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.configure;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * SmartGWT datasource for accessing entities over http in a RESTful manner.
 * Defines a RESTDataSource fields, operations and REST service URLs.
 */
public class EndpointConfigDS extends DataSource {


    private static EndpointConfigDS instance = null;

    public static EndpointConfigDS getInstance() {
        if (instance == null) {
            instance = new EndpointConfigDS();
        }
        return instance;
    }

	private EndpointConfigDS() {
        setID("endpointConfigDS");
        setDataURL("resources/datasources/endpoints/_actors.data.xml");
        setRecordXPath("/sites/site"); // this is the path to the record we want to display, inside the XML file holding the data
        setClientOnly(true);

        DataSourceTextField endpointName = new DataSourceTextField("endpointName", "Endpoint Name");
        endpointName.setRequired(true);
        endpointName.setPrimaryKey(true);

        setFields(endpointName);

    }
}
