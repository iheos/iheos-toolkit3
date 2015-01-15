package gov.nist.toolkit.xdstools3.client.customWidgets;

import com.smartgwt.client.widgets.HTMLPane;

/**
 * Widget that displays a wait message with a spinning wheel.
 * Needs to be added to a container.
 *
 * Use #show method to display the widget before the system starts processing.
 * Use #hide method to hide the widget once the system finishes processing.
 * Use #setWaitMessage method to change the message displayed by the widget.
 *
 * Created by onh2 on 12/23/2014.
 */
public class WaitPanel extends HTMLPane {
    /**
     * Default widget constructor.
     * The widget will display the spinning wheel with the
     * following message:
     *      <pre>"Waiting for validation result…"</pre>
     */
    public WaitPanel(){
        this("Waiting for validation result…");
    }

    /**
     * Custom widget constructor that will create a widget that
     * displays a specific message next to the spinning wheel.
     *
     * @param message custom wait message
     */
    public WaitPanel(String message){
        setWaitMessage(message);
        setHeight(62);
        setMargin(10);
    }

    /**
     * Method that changes the wait message of the widget.
     *
     * @param message custom wait message
     */
    public void setWaitMessage(String message){
        setContents("<div class='wait-message'><img src='images/icons/wait.gif' style='width:24px;'/> "+message+"</div>");
    }
}
