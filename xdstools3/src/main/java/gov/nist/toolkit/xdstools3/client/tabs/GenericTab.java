package gov.nist.toolkit.xdstools3.client.tabs;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import gov.nist.toolkit.xdstools3.client.customWidgets.design.Formatter;
import gov.nist.toolkit.xdstools3.client.customWidgets.design.IconLabel;

public abstract class GenericTab extends Tab implements TabInterface {
    private VLayout panel = new VLayout(10);
    private Label headerLabel = new Label();
    private String tabName;

    public GenericTab(String header){
        setTitle(header);
        setHeader(header);
        setContents(createContents());
        tabName=setTabName();
    }

    public VLayout getPanel() {
        return panel;
    }

    // main header
    public void setHeader(String s){
        headerLabel.setContents(s);
        headerLabel.setStyleName("h3");
        panel.addMember(headerLabel);
        setPane(panel);
    }

    public IconLabel createSubtitle1(String s){
        return Formatter.createSubtitle1(s); // TODO May need to be transformed into direct call inside each tab
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

    protected abstract Widget createContents();

    protected abstract String setTabName();
}