package gov.nist.toolkit.xdstools3.client.tabs;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import gov.nist.toolkit.xdstools3.client.customWidgets.WaitPanel;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.HelpButton;
import gov.nist.toolkit.xdstools3.client.customWidgets.design.Formatter;
import gov.nist.toolkit.xdstools3.client.customWidgets.design.IconLabel;

public abstract class GenericTab extends Tab implements TabInterface {

    private VLayout mainPanel = new VLayout();
    private VLayout contentsPanel = new VLayout(10); // this is the left panel
    private VLayout helpPanel = new VLayout(); // this is the right panel
    private HLayout topPanel = new HLayout();  // contains contentsPanel and resultsPanel
    private VLayout resultsPanel = new VLayout(); // bottom label
    private Label headerLabel = new Label();
    private HLayout titleAndHelpButton = new HLayout();
    private HelpButton helpButton;
    private String tabName;

    public GenericTab(String header){
        setTitle(header);
        setHeader(header);
        setContents(createContents());
        createResultsPanel();
        tabName=setTabName();
        mainPanel.setLayoutMargin(10);
    }

    public VLayout getContentsPanel() {
        return contentsPanel;
    }


    /**
     * This is the title of the tab, displayed in large characters at the top of the tab
     * @param s the title or header of the tab
     */
    public void setHeader(String s){
        // create the header of the tab
        headerLabel.setHeight(30);
        headerLabel.setWidth(200);
        headerLabel.setContents(s);
        headerLabel.setStyleName("h3");

        // add a spacer to separate the title from the help button
        LayoutSpacer spacer = new LayoutSpacer();
        spacer.setHeight(30);

        // add components to main contentsPanel
        titleAndHelpButton.addMembers(headerLabel, spacer);
        titleAndHelpButton.setHeight(30);
        mainPanel.addMember(titleAndHelpButton);
        setPane(mainPanel);
    }

    public IconLabel createSubtitle1(String s){
        return Formatter.createSubtitle1(s); // TODO May need to be transformed into direct call inside each tab
    }

    /**
     * Sets a tab contents
     * @param pane The tab contents
     */
    public void setContents(Widget pane){
        contentsPanel.addMember(pane); // left contents panel, that contains for example form fields
        topPanel.addMembers(contentsPanel, helpPanel);
        mainPanel.addMember(topPanel);
        setPane(mainPanel);
        getPane().setAlign(Alignment.CENTER);
    }

    /**
     * Adds a panel for the validation results
     */
    public void createResultsPanel(){
        mainPanel.addMember(resultsPanel);
        setPane(mainPanel);
    }

    /**
     * Changes the contents of the result panel
     * @param canvas
     */
    public void setResultsPanel(Canvas canvas){
        resultsPanel.clear();
        resultsPanel.addMember(canvas);
    }

    public VLayout getResultsPanel(){
        return mainPanel;
    }

    /**
     * Removes the main header of a tab
     */
    public void removeHeaderTitle(){
        mainPanel.removeMember(titleAndHelpButton);
    }

    /**
     * Add a help button to the top right corner of the tab
     */
    public void setHelpButton(Canvas container, String contents){
        helpButton = new HelpButton(container, contents);
        titleAndHelpButton.addMember(helpButton);
        titleAndHelpButton.redraw();
    }

    /**
     * Hides the help button
     */
    public void hideHelpButton(){
        contentsPanel.hideMember(helpButton);
    }

    /**
     * Changes the contents of the Help Window, displayed when clicking on the Help Button
     * @param contents
     */
    public void setHelpWindowContents(String contents){
        helpButton.setHelpWindowContents(contents);
    }


    public VLayout getHelpPanel() {
        return helpPanel;
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

}
