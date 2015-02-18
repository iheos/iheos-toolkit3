package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.types.*;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * Holds the configuration for the grids displaying a transaction.
 */
public class TransactionGrid extends ListGrid {

    public TransactionGrid(){
        super();

        // --- configuration ---
        setDataSource(TransactionDS.getInstance());

        setHeight(200);
        setCanEdit(true);
        setModalEditing(true);
        setEditEvent(ListGridEditEvent.CLICK);
        setListEndEditAction(RowEndEditAction.NEXT);
        setAutoSaveEdits(true);
        setAlternateRecordStyles(true);
        setGroupByField("actorType");
        setGroupStartOpen(GroupStartOpen.ALL);
        setCanRemoveRecords(true);


        // --- fields ----

        ListGridField siteName = new ListGridField("siteName");
        siteName.setHidden(true);

        ListGridField actorCode = new ListGridField("actorCode");
        actorCode.setHidden(true);

        ListGridField actorType = new ListGridField("actorType");
        actorType.setWidth(130);
        actorType.setRequired(true);

        ListGridField transactionCode = new ListGridField("transactionCode");
        transactionCode.setWidth(130);
        transactionCode.setRequired(true);

        ListGridField transactionName = new ListGridField("transactionName");
        transactionName.setHidden(true);

        ListGridField tls = new ListGridField("secure");
        tls.setRequired(true);

        ListGridField notls = new ListGridField("unsecure");

        ListGridField repositoryUniqueID = new ListGridField("repositoryUniqueID");
        repositoryUniqueID.setHidden(true);



        setFields(siteName, actorCode, actorType, transactionCode, transactionName, tls, notls, repositoryUniqueID);
    }

    // TODO should be written into separate files instead of one
    public void exportAsXML() {
        DSRequest dsRequestProperties = new DSRequest();
        dsRequestProperties.setExportToFilesystem(true);
        dsRequestProperties.setExportPath("/resources/datasources/endpoints");
        dsRequestProperties.setExportFilename("write_test.xml");
        dsRequestProperties.setExportAs(ExportFormat.XML);

        exportData(dsRequestProperties);
    }
}
