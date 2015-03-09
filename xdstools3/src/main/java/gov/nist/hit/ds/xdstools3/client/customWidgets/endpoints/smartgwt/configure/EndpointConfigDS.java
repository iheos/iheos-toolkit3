package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * SmartGWT datasource for accessing entities over http in a RESTful manner.
 * Defines a RESTDataSource fields, scripts and REST service URLs.
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

        DataSourceTextField siteName = new DataSourceTextField("siteName", "Site Name");
        siteName.setRequired(true);
        siteName.setPrimaryKey(true);

        setFields(siteName);

    }
}
