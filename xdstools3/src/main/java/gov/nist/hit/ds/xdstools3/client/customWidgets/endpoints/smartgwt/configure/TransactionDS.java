package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

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
        siteName.setForeignKey("endpointConfigDS.endpointName");

        DataSourceTextField actorCode = new DataSourceTextField("actorCode", "Endpoint Code");
        actorCode.setValueXPath("parent::actor/@actorCode");
        actorCode.setDisplayField("actorType");
        actorCode.setCanEdit(false);
        actorCode.setHidden(true); // is already displayed in group mode title

        DataSourceTextField actorType = new DataSourceTextField("actorType", "Actor Type");
        actorType.setValueXPath("parent::actor/@actorType");
        actorType.setValueMap("Document Registry", "Document Repository");
        


        //---- transactions -----

        DataSourceTextField transactionCode = new DataSourceTextField("transactionCode");
        transactionCode.setDisplayField("transactionName");

        DataSourceTextField transactionName = new DataSourceTextField("transactionName", "Transaction Type");
        transactionName.setCanEdit(false);
        transactionName.setHidden(true); // used only for formatting

        DataSourceTextField tls = new DataSourceTextField("secure", "TLS Endpoint");
        tls.setPrimaryKey(true);

        DataSourceTextField notls = new DataSourceTextField("unsecure", "Non-TLS Endpoint");


        //----- repositoryUniqueID and homeCommunityID ------
        DataSourceTextField repositoryUniqueID = new DataSourceTextField("uid");
        repositoryUniqueID.setValueXPath("parent::actor/@uid");


        setFields(siteName, actorCode, actorType, transactionCode, transactionName, tls, notls, repositoryUniqueID);
    }
}
