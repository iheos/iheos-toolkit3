package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.types.ExportFormat;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Holds the configuration for the grids displaying a transaction's properties.
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


        // Repository UID, homeCommunityID - test
        //ListGridRecord record = new ListGridRecord();
        //Record ruid = this.getRecordList().find("transactionCode", "Repository Unique ID");


        setFields(siteName, actorCode, actorType, transactionCode, transactionName, tls, notls, repositoryUniqueID);
    }


    @Override
    protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {
        if (getFieldName(colNum).equals("actorType")) {
            if (record.getAttributeAsString("Actor Type") == "Document Registry"   //== "Repository Unique ID"
           // && record.getAttributeAsString("unsecure") == "GREYED-OUT"
            ){
                System.out.println("found matches in smartgwt grid");

                return "background-color: blue;";
            } else {
                return super.getBaseStyle(record, rowNum, colNum);
            }
        } else {
            return super.getBaseStyle(record, rowNum, colNum);
        }
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
