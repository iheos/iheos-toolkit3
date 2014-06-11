package edu.tn.xds.metadata.editor.client.editor.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import edu.tn.xds.metadata.editor.client.editor.properties.DTMProperties;
import edu.tn.xds.metadata.editor.client.generics.GenericEditableListView;
import edu.tn.xds.metadata.editor.shared.model.DTM;
import edu.tn.xds.metadata.editor.shared.model.NameValueDTM;
import edu.tn.xds.metadata.editor.shared.model.String256;

import java.util.logging.Logger;

/**
 * <p>
 * <b>This class represents the widget which matches NameValue model type</b> <br>
 * </p>
 */
public class NameValueDTMEditorWidget extends Composite implements Editor<NameValueDTM> {
    private static Logger logger = Logger.getLogger(NameValueDTMEditorWidget.class.getName());

    ListStoreEditor<DTM> values;
    @Ignore
    GenericEditableListView<DTM,String> listView;

    public NameValueDTMEditorWidget(String widgetTitle) {

        final DTMProperties props = GWT.create(DTMProperties.class);

        listView=new GenericEditableListView<DTM,String>(DTM.class,widgetTitle,new ListStore<DTM>(props.key()),props.dtm());

        TextField tf=new TextField();
        tf.setAllowBlank(false);
        tf.setToolTip("This value is required. The format of these values is defined as following: YYYY[MM[DD[hh[mm[ss]]]]]; YYYY is the four digit year (ex: 2014); MM is the two digit month 01-12, where January is 01, December is 12; DD is the two digit day of the month 01-31; HH is the two digit hour, 00-23, where 00 is midnight, 01 is 1 am, 12 is noon, 13 is 1 pm; mm is the two digit minute, 00-59; ss is the two digit seconds, 00-59");
        tf.setEmptyText("YYYY[MM[DD[hh[mm[ss]]]]] (ex: 201103160830)");
        listView.addEditorConfig(tf);
        values=new ListStoreEditor<DTM>(listView.getStore());

        initWidget(listView.asWidget());
    }

}
