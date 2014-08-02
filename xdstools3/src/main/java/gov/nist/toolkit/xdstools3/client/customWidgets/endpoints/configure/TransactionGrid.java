package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.configure;

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
       // setAutoWidth();
        setCanEdit(true);
        setModalEditing(true);
        setEditEvent(ListGridEditEvent.CLICK);
        setListEndEditAction(RowEndEditAction.NEXT);
        setAutoSaveEdits(true);
        setAlternateRecordStyles(true);
        setGroupByField("actorName");
        setGroupStartOpen(GroupStartOpen.ALL);
        setCanRemoveRecords(true);


        // --- fields ----
        ListGridField tls = new ListGridField("secure");

        ListGridField transactionCode = new ListGridField("transactionCode");

        ListGridField notls = new ListGridField("unsecure");

        ListGridField actorName = new ListGridField("actorName");
        actorName.setHidden(true);

        setFields(actorName, transactionCode, tls, notls);
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
