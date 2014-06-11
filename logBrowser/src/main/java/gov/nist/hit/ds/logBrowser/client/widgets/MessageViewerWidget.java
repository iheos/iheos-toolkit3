package gov.nist.hit.ds.logBrowser.client.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MessageViewerWidget extends Composite {

    ScrollPanel headerPanel = new ScrollPanel();
    ScrollPanel messagePanel = new ScrollPanel();

    private SimpleEventBus eventBus;
    private String caption;
    private String ioHeaderId;


    /**
     *
     * @param caption
     */
   public MessageViewerWidget(SimpleEventBus eventBus, String caption, String ioHeaderId) {
       this.eventBus = eventBus;
       this.caption = caption;
       this.ioHeaderId = ioHeaderId;

     // All composites must call initWidget() in their constructors.
     initWidget(setupLayout());

   }


    public String getIoHeaderId() {
        return ioHeaderId;
    }

    public void setIoHeaderId(String id) {
        this.ioHeaderId = id;
    }



    protected TabLayoutPanel setupLayout() {
       TabLayoutPanel featureTlp = new TabLayoutPanel(20, Style.Unit.PX);

       featureTlp.add(headerPanel,caption + " Header");
       featureTlp.add(messagePanel,caption + " Message");

       return featureTlp;
   }

    public void setHeaderContent(Widget w) {
        headerPanel.clear();
        headerPanel.add(w);

    }
    public void setMessageContent(Widget w) {
        messagePanel.clear();
        messagePanel.add(w);

    }
 
}
