package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Created by dazais on 3/3/2015.
 */
public class TransactionWidget extends VLayout {

    public TransactionWidget(){
        setAlign(Alignment.CENTER);
        setMembersMargin(10);
        setLayoutTopMargin(10);
        setLayoutBottomMargin(10);
        setLayoutRightMargin(10);


        // ------ Document Registry fields ------
        final DynamicForm docRegistryForm = new TransactionBasicForm();
        docRegistryForm.setGroupTitle("Document Registry");

        TextItem pidFeedHost = new TextItem("pid-feed-host");
        pidFeedHost.setTitle("Patient Identity Feed");
        pidFeedHost.setHint("Host");
        pidFeedHost.setWidth(400);
        TextItem pidFeedPort = new TextItem("pid-feed-port");
        pidFeedPort.setShowTitle(false);
        pidFeedPort.setHint("Port");
        pidFeedPort.setWidth(100);
        BasicTextItem registerTls = new BasicTextItem("r-tls");
        registerTls.setTitle("Register");
        BasicTextItem registerNoTls = new BasicTextItem("r-notls");
        registerNoTls.setShowTitle(false);
        BasicTextItem sqTls = new BasicTextItem("sq-tls");
        sqTls.setTitle("Stored Query");
        BasicTextItem sqNoTls = new BasicTextItem("sq-notls");
        sqNoTls.setShowTitle(false);
        BasicTextItem updateTls = new BasicTextItem("update-tls");
        updateTls.setTitle("Update");
        BasicTextItem updateNoTls = new BasicTextItem("update-notls");
        updateNoTls.setShowTitle(false);
        BasicTextItem mpqTls = new BasicTextItem("mpq-tls");
        mpqTls.setTitle("Multi-Patient Query");
        BasicTextItem mpqNoTls = new BasicTextItem("mpq-notls");
        mpqNoTls.setShowTitle(false);

        docRegistryForm.setFields(pidFeedHost, pidFeedPort, registerTls, registerNoTls, sqTls, sqNoTls, updateTls, updateNoTls,
                mpqTls, mpqNoTls);


        // ------ Document Repository fields ------
        final DynamicForm docRepoForm = new TransactionBasicForm();
        docRepoForm.setGroupTitle("Document Repository");

        TextItem repoUuid = new TextItem("repo-uuid");
        repoUuid.setTitle("Repository Unique ID");
        repoUuid.setWidth(400);
        BasicTextItem retrieveTls = new BasicTextItem("retrieve-tls");
        retrieveTls.setTitle("Retrieve");
        BasicTextItem retrieveNoTls = new BasicTextItem("retrieve-notls");
        retrieveNoTls.setShowTitle(false);
        BasicTextItem pnrTls = new BasicTextItem("pnr-tls");
        pnrTls.setTitle("Provide and Register");
        BasicTextItem pnrNoTls = new BasicTextItem("pnr-notls");
        pnrNoTls.setShowTitle(false);

        docRepoForm.setFields(repoUuid, retrieveTls, retrieveNoTls, pnrTls, pnrNoTls);


        // ------ On-Demand Document Source Fields ------
        final DynamicForm docSourceForm = new TransactionBasicForm();
        docSourceForm.setGroupTitle("On-Demand Document Source");

        BasicTextItem docSrcRetrieveTls = new BasicTextItem("doc-src-retrieve-tls");
        docSrcRetrieveTls.setTitle("On-Demand Document Source Retrieve");
        BasicTextItem docSrcRetrieveNoTls = new BasicTextItem("doc-src-retrieve-notls");
        docSrcRetrieveNoTls.setShowTitle(false);

        docSourceForm.setFields(docSrcRetrieveTls, docSrcRetrieveNoTls);


        // ------ Integrated Source / Repository Fields ------
        final DynamicForm srcRepoForm = new TransactionBasicForm();
        srcRepoForm.setGroupTitle("Integrated Source / Repository");

        BasicTextItem srcRepoRetrieveTls = new BasicTextItem("src-repo-retrieve-tls");
        srcRepoRetrieveTls.setTitle("Integrated Source / Repository Retrieve");
        BasicTextItem srcRepoRetrieveNoTls = new BasicTextItem("src-repo-retrieve-notls");
        srcRepoRetrieveNoTls.setShowTitle(false);

        srcRepoForm.setFields(srcRepoRetrieveTls, srcRepoRetrieveNoTls);


        // ------ Document Recipient Fields ------
        final DynamicForm docRecptForm = new TransactionBasicForm();
        docRecptForm.setGroupTitle("Document Recipient");

        BasicTextItem xdrPnRTls = new BasicTextItem("xdr-pnr-tls");
        xdrPnRTls.setTitle("XDR Provide and Register");
        BasicTextItem xdrPnRNoTls = new BasicTextItem("xdr-pnr-notls");
        xdrPnRNoTls.setShowTitle(false);

        docRecptForm.setFields(xdrPnRTls, xdrPnRNoTls);


        // ------ Responding Gateway Fields ------
        final DynamicForm respGatewayForm = new TransactionBasicForm();
        respGatewayForm.setGroupTitle("Responding Gateway");

        TextItem homeCommunityId = new TextItem("hc-id");
        homeCommunityId.setTitle("Home Community ID");
        homeCommunityId.setWidth(400);
        BasicTextItem xcqTls = new BasicTextItem("xcq-tls");
        xcqTls.setTitle("Cross-Community Query");
        BasicTextItem xcqNoTls = new BasicTextItem("xcq-notls");
        xcqNoTls.setShowTitle(false);
        BasicTextItem xcrTls = new BasicTextItem("xcr-tls");
        xcrTls.setTitle("Cross-Community Retrieve");
        BasicTextItem xcrNoTls = new BasicTextItem("xcr-notls");
        xcrNoTls.setShowTitle(false);
        BasicTextItem xcpdTls = new BasicTextItem("xcpd-tls");
        xcpdTls.setTitle("Cross-Community Patient Discovery");
        BasicTextItem xcpdNoTls = new BasicTextItem("xcpd-notls");
        xcpdNoTls.setShowTitle(false);

        respGatewayForm.setFields(homeCommunityId, xcqTls, xcqNoTls, xcrTls, xcrNoTls, xcpdTls, xcpdNoTls);


        // ------ Initiating Gateway Fields ------
        final DynamicForm initGatewayForm = new TransactionBasicForm();
        initGatewayForm.setGroupTitle("Initiating Gateway");

        BasicTextItem igqTls = new BasicTextItem("igq-tls");
        igqTls.setTitle("Initiating Gateway Query");
        BasicTextItem igqNoTls = new BasicTextItem("igq-notls");
        igqNoTls.setShowTitle(false);
        BasicTextItem igrTls = new BasicTextItem("igr-tls");
        igrTls.setTitle("Initiating Gateway Retrieve");
        BasicTextItem igrNoTls = new BasicTextItem("igr-notls");
        igrNoTls.setShowTitle(false);

        initGatewayForm.setFields(igqTls, igqNoTls, igrTls, igrNoTls);


        // ------- Add all the forms to the Vlayout -------
        addMembers(docRegistryForm, docRepoForm, docSourceForm, srcRepoForm, docRecptForm, respGatewayForm, initGatewayForm);

    }
}
