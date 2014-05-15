package gov.nist.toolkit.xdstools3.server.endpoints;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.FieldType;

/**
 * Created by dazais on 5/13/2014.
 */
public class EndpointDS extends DataSource{

    private static EndpointDS instance = null;

    public static EndpointDS getInstance() {
        if (instance == null) {
            instance = new EndpointDS("endpointDS");
        }
        return instance;
    }

    public EndpointDS(String id) {
        setID(id);
        setRecordXPath("/data/datasources/endpoints");
        DataSourceField endpointNameField = new DataSourceField("endpointName", FieldType.TEXT, "Endpoint");
        //DataSourceField gdpField = new DataSourceField("gdp", FieldType.FLOAT, "GDP ($B)");

        setFields(endpointNameField);
        setDataURL("/data/datasources/endpoints.data.xml");
    }

}
