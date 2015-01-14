package gov.nist.toolkit.xdstools3.client.tabs.docEntryEditorTab;

import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.widgets.HTMLPane;
import gov.nist.toolkit.xdstools3.client.manager.TabNamesManager;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;

public class DocEntryEditorTab extends GenericCloseableTab {
    static String header = "Document Metadata Editor";

    public DocEntryEditorTab() {
        super(header);
        HTMLPane pane = new HTMLPane();
        pane.setShowEdges(false);
        pane.setContentsURL("http://hit-dev.nist.gov:12090/docentryeditor/");
        pane.setContentsType(ContentsType.PAGE);
        this.setPane(pane);
    }

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getDocumentMetadataEditorTabCode();
    }
}
