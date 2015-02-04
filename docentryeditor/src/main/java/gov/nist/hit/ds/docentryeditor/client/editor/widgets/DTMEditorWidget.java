package gov.nist.hit.ds.docentryeditor.client.editor.widgets;

import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import gov.nist.hit.ds.docentryeditor.shared.model.DTM;

/**
 * <p>
 * <b>This class represents the widget which matches DTM model type</b>
 * </p>
 */
@Deprecated
public class DTMEditorWidget extends Composite implements Editor<DTM> {
    VerticalLayoutContainer vcontainer = new VerticalLayoutContainer();
    String256EditorWidget dtm = new String256EditorWidget();

    public DTMEditorWidget() {
        initWidget(vcontainer);
        dtm.setWidth("auto");
        this.setWidth("auto");
        vcontainer.setWidth("auto");
        vcontainer.add(dtm, new VerticalLayoutData(1, -1));
    }
}
