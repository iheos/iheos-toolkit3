package gov.nist.toolkit.xdstools3.client.tabs.docEntryEditorTab;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

public class DocEntryEditorTab extends GenericCloseableTab {
    static String header = "Document Metadata Editor";

    public DocEntryEditorTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        HTMLPane pane =new HTMLPane();
        pane.setShowEdges(false);
        pane.setContentsURL("http://transport-testing.nist.gov:12090/docentryeditor/");
        pane.setContentsType(ContentsType.PAGE);
        return pane;
    }

    @Override
    protected String setTabName() {
        return TabNamesUtil.getDocumentMetadataEditorTabCode();
    }
}
