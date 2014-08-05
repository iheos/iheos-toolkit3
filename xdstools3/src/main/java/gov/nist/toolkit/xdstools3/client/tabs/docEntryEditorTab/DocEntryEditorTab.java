package gov.nist.toolkit.xdstools3.client.tabs.docEntryEditorTab;

import com.google.gwt.user.client.ui.Widget;
import edu.tn.xds.metadata.editor.client.MetadataEditorApp;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;

/**
 */
public class DocEntryEditorTab extends GenericCloseableTab {
    static String header = "Document Metadata Editor";
    final static MetadataEditorApp app = new MetadataEditorApp();

    public DocEntryEditorTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
//        Canvas panel=new Canvas();
//        panel.addChild(new MetadataEditorApp().asWidget());
        return app.asWidget();
    }
}
