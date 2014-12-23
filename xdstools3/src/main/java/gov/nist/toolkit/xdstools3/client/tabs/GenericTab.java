package gov.nist.toolkit.xdstools3.client.tabs;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import gov.nist.toolkit.xdstools3.client.customWidgets.WaitPanel;
import gov.nist.toolkit.xdstools3.client.customWidgets.design.Formatter;
import gov.nist.toolkit.xdstools3.client.customWidgets.design.IconLabel;

public abstract class GenericTab extends Tab implements TabInterface {
    private VLayout panel = new VLayout(10);
    private Label headerLabel = new Label();
    private String tabName;
    protected WaitPanel waitPanel=new WaitPanel();

    public GenericTab(String header){
        setTitle(header);
        setHeader(header);
        setContents(createContents());
        tabName=setTabName();
    }

    public VLayout getPanel() {
        return panel;
    }

    /**
     * This is the title of the tab, displayed in large characters at the top of the tab
     * @param s the title or header to be set, as String
     */
    public void setHeader(String s){
        headerLabel.setHeight(30);
        headerLabel.setContents(s);
        headerLabel.setStyleName("h3");
        panel.addMember(headerLabel);
        setPane(panel);
    }

    public IconLabel createSubtitle1(String s){
        return Formatter.createSubtitle1(s); // TODO May need to be transformed into direct call inside each tab
    }

    public void hideHeaderTitle(){
        panel.removeMember(headerLabel);
    }

    /**
     * Sets a tab contents
     * @param pane The tab contents
     */
    public void setContents(Widget pane){
        panel.addMember(pane);
        setPane(panel);
        getPane().setAlign(Alignment.CENTER);
    }



    /**
     * Returns tab's name name used for navigation.
     * These are defined in TabNamesUtil.
     *
     * @return tab name (String)
     *
     * @See TabNamesUtil
     */
    public String getTabName(){
        return tabName;
    }

    /**
     * Abstract method that builds the tab's widget content.
     * @return tab's content widget
     */
    protected abstract Widget createContents();

    /**
     * Method that sets the tab's name.
     * @return tab's name
     */
    protected abstract String setTabName();

    /**
     * Method that return a panel displaying a message asking to wait
     * while the system is processing.
     *
     * <strong>How to:</strong>
     * To display the panel, use {@link gov.nist.toolkit.xdstools3.client.customWidgets.WaitPanel#show()}
     * method before system start processing.
     * To hide the panel, use {@link gov.nist.toolkit.xdstools3.client.customWidgets.WaitPanel#hide()}
     * method when system finishes processing.
     *
     * @see gov.nist.toolkit.xdstools3.client.customWidgets.WaitPanel
     *
     * @return WaitPanel
     */
    protected WaitPanel getWaitPanel(){
        return waitPanel;
    }
}
