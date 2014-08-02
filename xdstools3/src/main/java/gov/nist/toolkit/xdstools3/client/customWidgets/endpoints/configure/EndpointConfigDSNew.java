package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.configure;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * SmartGWT datasource for accessing entities over http in a RESTful manner.
 * Defines a RESTDataSource fields, operations and REST service URLs.
 */
public class EndpointConfigDSNew extends DataSource {


    private static EndpointConfigDSNew instance = null;

    public static EndpointConfigDSNew getInstance() {
        if (instance == null) {
            instance = new EndpointConfigDSNew();
        }
        return instance;
    }

	private EndpointConfigDSNew() {
        setID("endpointConfigDSNew");
        setDataURL("resources/datasources/endpoints/_actors.data.xml");
        setRecordXPath("/sites/site"); // this is the path to the record we want to display, inside the XML file holding the data
        setClientOnly(true);

        DataSourceTextField endpointName = new DataSourceTextField("endpointName", "Endpoint Name");
        endpointName.setRequired(true);
        endpointName.setPrimaryKey(true);

        setFields(endpointName);

    }
}
