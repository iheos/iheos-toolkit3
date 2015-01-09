package gov.nist.toolkit.xdstools3.client.customWidgets.links;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

/**
 * Created by dazais on 6/16/2014.
 */
public class LinkPIDGenerator {

    public LinkPIDGenerator() {
        // External link to Patient ID Generator
        LinkItem linkItem = new LinkItem("pidGenerator");
        linkItem.setLinkTitle("Patient ID generator");
        linkItem.setStartRow(false);
        linkItem.setShowTitle(false);
        linkItem.setHeight(15);
        linkItem.setVAlign(VerticalAlignment.CENTER);
        linkItem.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                Window.open("http://ihexds.nist.gov:12080/xdstools/pidallocate", "", "");
            }
        });
    }
}
