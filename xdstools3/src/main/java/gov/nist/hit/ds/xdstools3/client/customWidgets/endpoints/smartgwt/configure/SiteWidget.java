package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

import java.util.ArrayList;

/**
 * Contains the front-end code for one Site. This widget is composed of multiple forms, one per transaction type.
 * Created by dazais on 3/3/2015.
 */
public class SiteWidget extends VLayout {
    protected TransactionBasicForm docRegistryForm, docRepoForm, docSourceForm, srcRepoForm, docRecptForm, respGatewayForm, initGatewayForm;


    public SiteWidget(String datasourceID){
        setAlign(Alignment.CENTER);
        setMembersMargin(10);
        setLayoutTopMargin(10);
        setLayoutBottomMargin(10);
        setLayoutRightMargin(10);

        // create the DataSource and the forms
        SiteDS siteDS = DSFactory.getDataSource(datasourceID); // this should be created only once
        docRegistryForm = new TransactionBasicForm(siteDS);
        docRepoForm = new TransactionBasicForm(siteDS);
        docSourceForm = new TransactionBasicForm(siteDS);
        srcRepoForm = new TransactionBasicForm(siteDS);
        docRecptForm = new TransactionBasicForm(siteDS);
        respGatewayForm = new TransactionBasicForm(siteDS);
        initGatewayForm = new TransactionBasicForm(siteDS);


        // ------ Document Registry fields ------
        docRegistryForm.setGroupTitle("Document Registry");

        TextItem pidFeedHost = new TextItem("pidfeedhost");
        pidFeedHost.setTitle("Patient Identity Feed");
        pidFeedHost.setHint("Host");
        pidFeedHost.setWidth(400);
        TextItem pidFeedPort = new TextItem("pidfeedport");
        pidFeedPort.setShowTitle(false);
        pidFeedPort.setHint("Port");
        pidFeedPort.setWidth(50);
        BasicTextItem registerTls = new BasicTextItem("rtls");
        registerTls.setTitle("Register");
        BasicTextItem registerNoTls = new BasicTextItem("rnotls");
        registerNoTls.setShowTitle(false);
        BasicTextItem sqTls = new BasicTextItem("sqtls");
        sqTls.setTitle("Stored Query");
        BasicTextItem sqNoTls = new BasicTextItem("sqnotls");
        sqNoTls.setShowTitle(false);
        BasicTextItem updateTls = new BasicTextItem("updatetls");
        updateTls.setTitle("Update");
        BasicTextItem updateNoTls = new BasicTextItem("updatenotls");
        updateNoTls.setShowTitle(false);
        BasicTextItem mpqTls = new BasicTextItem("mpqtls");
        mpqTls.setTitle("Multi-Patient Query");
        BasicTextItem mpqNoTls = new BasicTextItem("mpqnotls");
        mpqNoTls.setShowTitle(false);

        docRegistryForm.setFields(pidFeedHost, pidFeedPort, registerTls, registerNoTls, sqTls, sqNoTls, updateTls, updateNoTls,
                mpqTls, mpqNoTls);


        // ------ Document Repository fields ------
        docRepoForm.setGroupTitle("Document Repository");

        TextItem repoUuid = new TextItem("repouuid");
        repoUuid.setTitle("Repository Unique ID");
        repoUuid.setWidth(400);
        BasicTextItem retrieveTls = new BasicTextItem("retrievetls");
        retrieveTls.setTitle("Retrieve");
        BasicTextItem retrieveNoTls = new BasicTextItem("retrievenotls");
        retrieveNoTls.setShowTitle(false);
        BasicTextItem pnrTls = new BasicTextItem("pnrtls");
        pnrTls.setTitle("Provide and Register");
        BasicTextItem pnrNoTls = new BasicTextItem("pnrnotls");
        pnrNoTls.setShowTitle(false);

        docRepoForm.setFields(repoUuid, retrieveTls, retrieveNoTls, pnrTls, pnrNoTls);


        // ------ On-Demand Document Source Fields ------
        docSourceForm.setGroupTitle("On-Demand Document Source");

        BasicTextItem docSrcRetrieveTls = new BasicTextItem("docsrcretrievetls");
        docSrcRetrieveTls.setTitle("On-Demand Document Source Retrieve");
        BasicTextItem docSrcRetrieveNoTls = new BasicTextItem("docsrcretrievenotls");
        docSrcRetrieveNoTls.setShowTitle(false);

        docSourceForm.setFields(docSrcRetrieveTls, docSrcRetrieveNoTls);


        // ------ Integrated Source / Repository Fields ------
        srcRepoForm.setGroupTitle("Integrated Source / Repository");

        BasicTextItem srcRepoRetrieveTls = new BasicTextItem("srcreporetrievetls");
        srcRepoRetrieveTls.setTitle("Integrated Source / Repository Retrieve");
        BasicTextItem srcRepoRetrieveNoTls = new BasicTextItem("srcreporetrievenotls");
        srcRepoRetrieveNoTls.setShowTitle(false);

        srcRepoForm.setFields(srcRepoRetrieveTls, srcRepoRetrieveNoTls);


        // ------ Document Recipient Fields ------
        docRecptForm.setGroupTitle("Document Recipient");

        BasicTextItem xdrPnRTls = new BasicTextItem("xdrpnrtls");
        xdrPnRTls.setTitle("XDR Provide and Register");
        BasicTextItem xdrPnRNoTls = new BasicTextItem("xdrpnrnotls");
        xdrPnRNoTls.setShowTitle(false);

        docRecptForm.setFields(xdrPnRTls, xdrPnRNoTls);


        // ------ Responding Gateway Fields ------
        respGatewayForm.setGroupTitle("Responding Gateway");

        TextItem homeCommunityId = new TextItem("hcid");
        homeCommunityId.setTitle("Home Community ID");
        homeCommunityId.setWidth(400);
        BasicTextItem xcqTls = new BasicTextItem("xcqtls");
        xcqTls.setTitle("Cross-Community Query");
        BasicTextItem xcqNoTls = new BasicTextItem("xcqnotls");
        xcqNoTls.setShowTitle(false);
        BasicTextItem xcrTls = new BasicTextItem("xcrtls");
        xcrTls.setTitle("Cross-Community Retrieve");
        BasicTextItem xcrNoTls = new BasicTextItem("xcrnotls");
        xcrNoTls.setShowTitle(false);
        BasicTextItem xcpdTls = new BasicTextItem("xcpdtls");
        xcpdTls.setTitle("Cross-Community Patient Discovery");
        BasicTextItem xcpdNoTls = new BasicTextItem("xcpdnotls");
        xcpdNoTls.setShowTitle(false);

        respGatewayForm.setFields(homeCommunityId, xcqTls, xcqNoTls, xcrTls, xcrNoTls, xcpdTls, xcpdNoTls);


        // ------ Initiating Gateway Fields ------
        initGatewayForm.setGroupTitle("Initiating Gateway");

        BasicTextItem igqTls = new BasicTextItem("igqtls");
        igqTls.setTitle("Initiating Gateway Query");
        BasicTextItem igqNoTls = new BasicTextItem("igqnotls");
        igqNoTls.setShowTitle(false);
        BasicTextItem igrTls = new BasicTextItem("igrtls");
        igrTls.setTitle("Initiating Gateway Retrieve");
        BasicTextItem igrNoTls = new BasicTextItem("igrnotls");
        igrNoTls.setShowTitle(false);

        initGatewayForm.setFields(igqTls, igqNoTls, igrTls, igrNoTls);


        // ------- Add all the forms to the Vlayout -------
        addMembers(docRegistryForm, docRepoForm, docSourceForm, srcRepoForm, docRecptForm, respGatewayForm, initGatewayForm);

    }

    public void fetchRelatedData(String currentSite){
        // create the search criteria for the SiteDS (nested) DataSource
        Criteria criteria = new Criteria("name", currentSite);
        ArrayList<TransactionBasicForm> array = new ArrayList<TransactionBasicForm>();
        array.add(docRegistryForm);
        array.add(docRepoForm);
        array.add(docSourceForm);
        array.add(srcRepoForm);
        array.add(docRecptForm);
        array.add(respGatewayForm);
        array.add(initGatewayForm);

        for (TransactionBasicForm currentForm : array) {
            currentForm.fetchRelatedData(criteria);
        }
    }



}
