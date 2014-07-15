package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.configure;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * SmartGWT datasource.
 */
public class TransactionDS extends DataSource {

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
        setRecordXPath("/site/transaction"); //the XML path of the element we want to display, in the datasource file (.data.xml)
        setClientOnly(true);

        DataSourceTextField endpointName = new DataSourceTextField("siteName", "Endpoint Name");
        endpointName.setValueXPath("/site/transaction/ancestor::site/@siteName");
        endpointName.setHidden(true);
        endpointName.setCanEdit(false);
        endpointName.setForeignKey("endpointConfigDSNew.endpointName");

        DataSourceTextField name = new DataSourceTextField("name", "Transaction Type");
        name.setPrimaryKey(true);
        DataSourceTextField tls = new DataSourceTextField("secure", "TLS Endpoint");
        DataSourceTextField notls = new DataSourceTextField("unsecure", "Non-TLS Endpoint");


        setFields(name, tls, notls, endpointName);
    }
}
