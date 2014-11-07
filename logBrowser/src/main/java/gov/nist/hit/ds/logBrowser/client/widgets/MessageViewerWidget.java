package gov.nist.hit.ds.logBrowser.client.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import gov.nist.hit.ds.repository.shared.data.AssetNode;

public class MessageViewerWidget extends Composite {

    ScrollPanel headerPanel = new ScrollPanel();
    ScrollPanel messagePanel = new ScrollPanel();

    private SimpleEventBus eventBus;
    private String caption;
    private String ioHeaderId;
    private String repId;
    private String repositorySrc;
    private AssetNode headerAssetNode;
    private AssetNode messageAssetNode;

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

//       featureTlp.add(messagePanel,caption + " Message"); // Update the index (1) below

        HTMLPanel tabTextPanel = new HTMLPanel("");
//        tabTextPanel.setWidth("150px");

//        Image img = new Image();
//        img.setUrl(GWT.getModuleBaseForStaticFiles() + "images/code_colored.png");



        tabTextPanel.add(new HTML(caption + " Message"));
//        HTML codeColorImg = new HTML(caption + " Message" + "<span style=\"width:32px;height:32px;\"><img border=0 height=16 width=16 src='" + GWT.getModuleBaseForStaticFiles() + "images/code_colored"
//                +  ".png'/></span>");

        /*
        img.setWidth("16px");
        img.setHeight("16px");
        img.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (getMessageAssetNode()!=null) {
                    eventBus.fireEvent(new OutOfContextAssetClickedEvent(getMessageAssetNode(),-1)); // the static -1 row number value is a bogus value to indicate a non-csv type
                }
            }
        });
        tabTextPanel.add(img);
        */

        featureTlp.add(messagePanel, tabTextPanel);

//       featureTlp.getTabWidget(1).addDomHandler(new DoubleClickHandler() {
//            @Override
//            public void onDoubleClick(DoubleClickEvent event) {
//                eventBus.fireEvent(new OutOfContextAssetClickedEvent(getMessageAssetNode(),-1)); // the static -1 row number value is a bogus value to indicate a non-csv type
//            }
//        }, DoubleClickEvent.getType());

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

    public String getRepId() {
        return repId;
    }

    public void setRepId(String repId) {
        this.repId = repId;
    }

    public String getRepositorySrc() {
        return repositorySrc;
    }

    public void setRepositorySrc(String repositorySrc) {
        this.repositorySrc = repositorySrc;
    }

    public AssetNode getHeaderAssetNode() {
        return headerAssetNode;
    }

    public void setHeaderAssetNode(AssetNode headerAssetNode) {
        this.headerAssetNode = headerAssetNode;

        if (headerAssetNode!=null && headerAssetNode.getTxtContent()!=null) {
            HTML txtContent = new HTML("<pre>" + headerAssetNode.getTxtContent() + "</pre>");
            setHeaderContent(txtContent);
        }
    }

    public AssetNode getMessageAssetNode() {
        return messageAssetNode;
    }

    public void setMessageAssetNode(AssetNode messageAssetNode) {
        this.messageAssetNode = messageAssetNode;

        if (messageAssetNode!=null && messageAssetNode.getTxtContent()!=null) {
            HTML txtContent = new HTML("<pre>" + messageAssetNode.getTxtContent() + "</pre>");
            setMessageContent(txtContent);
        }

    }
}
