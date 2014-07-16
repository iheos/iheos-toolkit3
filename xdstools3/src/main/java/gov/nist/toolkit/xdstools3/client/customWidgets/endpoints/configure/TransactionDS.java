package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.configure;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * SmartGWT datasource.
 *
 * Field names must match the XML source and must be unique to this datasource.
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
        setDataURL("resources/datasources/endpoints/pub-edited-2.data.xml");
        setRecordXPath("/site/actor/transaction"); //the XML path of the element we want to display, in the datasource file (.data.xml)
        setClientOnly(true);

        //---- actors ----

        DataSourceTextField siteName = new DataSourceTextField("siteName", "Site Name");
        siteName.setValueXPath("ancestor::site/@siteName");
        siteName.setHidden(true);
        siteName.setForeignKey("endpointConfigDSNew.endpointName");

        DataSourceTextField actorCode = new DataSourceTextField("actorCode", "Endpoint Code");
        actorCode.setValueXPath("parent::actor/@actorCode");
        actorCode.setDisplayField("actorName");
        actorCode.setHidden(true); // is already displayed in group mode title

        DataSourceTextField actorName = new DataSourceTextField("actorName");
        actorName.setValueXPath("parent::actor/@actorName");
        actorName.setHidden(true); // used only for formatting


        //---- transactions -----

        DataSourceTextField transactionCode = new DataSourceTextField("transactionCode");
        transactionCode.setPrimaryKey(true);
        transactionCode.setCanEdit(false); // primary keys cannot be edited. A workaround is to delete the record and create a new one with the same values.
        transactionCode.setDisplayField("transactionName");

        DataSourceTextField transactionName = new DataSourceTextField("transactionName", "Transaction Type");
        transactionName.setCanEdit(false);
        transactionName.setHidden(true); // used only for formatting

        DataSourceTextField tls = new DataSourceTextField("secure", "TLS Endpoint");
        DataSourceTextField notls = new DataSourceTextField("unsecure", "Non-TLS Endpoint");


        setFields(siteName, actorCode, actorName, transactionCode, transactionName, tls, notls);
    }
}
