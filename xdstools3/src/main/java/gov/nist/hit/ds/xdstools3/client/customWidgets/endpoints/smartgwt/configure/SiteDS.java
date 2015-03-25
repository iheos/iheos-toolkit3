package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * SmartGWT datasource.
 *
 *  Factory that creates a SiteDS, a customized type of SmartGWT DataSource, based on the current site name
 * Field names must match the XML source and must be unique to this datasource.
 */
public class SiteDS extends DataSource {

    private static SiteDS instance = null;

    //TODO as far as I know the singleton is not used anymore
    public static SiteDS getInstance() {
        if (instance == null){
            instance = new SiteDS();
        }
        return instance;
    }

    public SiteDS() { // was private (singleton)
        //setID("siteDS");
        //setDataURL("resources/datasources/endpoints/pub.data.xml");
        setRecordXPath("/site"); //the XML path of the element we want to display, in the datasource file (.data.xml)
        setClientOnly(true);


        DataSourceTextField siteName = new DataSourceTextField("name");
        siteName.setValueXPath("@name");
        siteName.setHidden(true);
        siteName.setForeignKey("endpointConfigDS.siteName");

        DataSourceTextField pidFeedHost = new DataSourceTextField("pidfeedhost");
        pidFeedHost.setValueXPath("actor/pid-feed/host");
        pidFeedHost.setDisplayField("pidfeedhost");
        pidFeedHost.setPrimaryKey(true);

        DataSourceTextField pidFeedPort = new DataSourceTextField("pidfeedport");
        pidFeedPort.setValueXPath("actor/pid-feed/port");
        pidFeedPort.setDisplayField("pidfeedport");

/*        DataSourceTextField registerTls = new DataSourceTextField("rtls");
        pidFeedPort.setValueXPath("actor/pid-feed/port");
        pidFeedPort.setDisplayField("pidfeedport");*/


//        DataSourceTextField actorCode = new DataSourceTextField("actorCode", "Endpoint Code");
//        actorCode.setValueXPath("parent::actor/@actorCode");
//        actorCode.setDisplayField("actorType");
//        actorCode.setCanEdit(false);
//        actorCode.setHidden(true); // is already displayed in group mode title
//
//        DataSourceTextField actorType = new DataSourceTextField("actorType", "Actor Type");
//        actorType.setValueXPath("parent::actor/@actorType");
//        actorType.setValueMap("Document Registry", "Document Repository");


        //---- transactions -----
//
//        DataSourceTextField transactionCode = new DataSourceTextField("transactionCode");
//        transactionCode.setDisplayField("transactionName");

//        DataSourceTextField transactionName = new DataSourceTextField("transactionName", "Transaction Type");
//        transactionName.setCanEdit(false);
//        transactionName.setHidden(true); // used only for formatting


        setFields(siteName, pidFeedHost, pidFeedPort);
    }
}
