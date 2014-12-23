package gov.nist.toolkit.xdstools3.client.customWidgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.HTMLPane;

/**
 * Created by onh2 on 12/23/2014.
 */
public class WaitPanel extends HTMLPane {
    public WaitPanel(){
        this("Waiting for validation result…");
    }

    public WaitPanel(String message){
        setContents("<div id='loading'><img src='images/icons/wait.gif' style='width:24px;'/> "+message+"</div>");
        setHeight(42);
    }

    public void show(){
        DOM.setStyleAttribute(RootPanel.get("loading").getElement(),"display","block");
    }

    public void hide(){
        DOM.setStyleAttribute(RootPanel.get("loading").getElement(),"display","none");
    }
}
