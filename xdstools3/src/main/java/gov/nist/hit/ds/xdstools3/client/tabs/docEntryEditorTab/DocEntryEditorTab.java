package gov.nist.hit.ds.xdstools3.client.tabs.docEntryEditorTab;

import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.widgets.HTMLPane;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableTab;

public class DocEntryEditorTab extends GenericCloseableTab {
    static String header = "Document Metadata Editor";

    public DocEntryEditorTab() {
        super(header);
        HTMLPane pane = new HTMLPane();
        pane.setShowEdges(false);
        pane.setContentsURL("http://ihexds.nist.gov:12093/docentryeditor-integ/");
        //http://hit-dev.nist.gov:12090/docentryeditor-integ/ - this URL has a cert issue
        pane.setContentsType(ContentsType.PAGE);
        this.setPane(pane);
    }

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getDocumentMetadataEditorTabCode();
    }
}
