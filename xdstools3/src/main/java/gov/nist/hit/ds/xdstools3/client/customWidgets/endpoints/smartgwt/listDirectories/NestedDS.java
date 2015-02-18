package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.listDirectories;

import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * SmartGWT datasource for accessing entities over http in a RESTful manner.
 * Defines a RESTDataSource fields, scripts and REST service URLs.
 */
public class NestedDS extends RestDataSource {

    private static NestedDS instance = null;

    public static NestedDS getInstance() {
        if (instance == null) {
            instance = new NestedDS();
        }
        return instance;
    }

	private NestedDS() {
        setID("transactionDS");
        setDataURL("resources/datasources/endpoints/pub-edited.data.xml");
        setRecordXPath("/site/transaction"); //the XML path of the element we want to display, in the datasource file (.data.xml)
        setClientOnly(true);

        DataSourceTextField endpointName = new DataSourceTextField("name", "Endpoint Name");
         endpointName.setValueXPath("/site/@name");
       // System.out.println(endpointName.getValueMap());
        //endpointName.setHidden(true);
        endpointName.setCanEdit(false);
        endpointName.setForeignKey("endpointConfigDS.endpointName");

        DataSourceTextField code = new DataSourceTextField("code");
        code.setPrimaryKey(true);
        code.setHidden(true);
        DataSourceTextField name = new DataSourceTextField("name", "Transaction Type", 128, true); // isRequired = true
        DataSourceTextField tls = new DataSourceTextField("secure", "TLS Endpoint");
        DataSourceTextField notls = new DataSourceTextField("unsecure", "Non-TLS Endpoint");


        setFields(code, name, tls, notls, endpointName);
	}
}
