package gov.nist.toolkit.xdstools3.client.tabs.docEntryEditorTab;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;

/**
 */
public class DocEntryEditorTab extends GenericCloseableTab {
    static String header = "Document Metadata Editor";
//    final static MetadataEditorApp app = new MetadataEditorApp();

    public DocEntryEditorTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
//        Canvas panel=new Canvas();
//        panel.addChild(new MetadataEditorApp().asWidget());
//        return app.asWidget();
        HTMLPane pane =new HTMLPane();
        pane.setShowEdges(false);
        pane.setContentsURL("http://transport-testing.nist.gov:12090/docentryeditor/");
        pane.setContentsType(ContentsType.PAGE);
        return pane;
    }
}
