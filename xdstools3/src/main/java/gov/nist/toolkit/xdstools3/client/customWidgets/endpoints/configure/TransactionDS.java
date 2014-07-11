package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.configure;

import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * SmartGWT datasource for accessing entities over http in a RESTful manner.
 * Defines a RESTDataSource fields, operations and REST service URLs.
 */
public class TransactionDS extends RestDataSource {

    private static TransactionDS instance = null;

    public static TransactionDS getInstance() {
        if (instance == null) {
            instance = new TransactionDS();
        }
        return instance;
    }

	private TransactionDS() {
        setID("transactionDS");
        setDataURL("resources/datasources/endpoints/pub-edited.data.xml");
        setRecordXPath("/site"); //the XML path of the element we want to display, in the datasource file (.data.xml)
        setClientOnly(true);

        DataSourceTextField endpointName = new DataSourceTextField("name", "Endpoint Name");
        //System.out.println(endpointName.getValueXPath());
        //endpointName.setHidden(true);
        endpointName.setCanEdit(false);
        endpointName.setForeignKey("endpointConfigDS.endpointName");

        DataSourceTextField code = new DataSourceTextField("transaction.code");
        code.setPrimaryKey(true);
        code.setHidden(true);
        DataSourceTextField name = new DataSourceTextField("transaction.name", "Transaction Type", 128, true); // isRequired = true
        DataSourceTextField tls = new DataSourceTextField("transaction.secure", "TLS Endpoint");
        DataSourceTextField notls = new DataSourceTextField("transaction.unsecure", "Non-TLS Endpoint");


        setFields(code, name, tls, notls, endpointName);
	}
}
