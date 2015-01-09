package gov.nist.toolkit.xdstools3.client.tabs;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.toolkit.xdstools3.client.customWidgets.WaitPanel;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.HelpButton;
import gov.nist.toolkit.xdstools3.client.customWidgets.design.Formatter;
import gov.nist.toolkit.xdstools3.client.customWidgets.design.IconLabel;

public abstract class GenericToolTab extends GenericTab implements ToolTabInterface {

    private VLayout mainPanel = new VLayout();
    private VLayout helpPanel = new VLayout(); // this is the right panel
    private HLayout topPanel = new HLayout();  // contains contentsPanel and resultsPanel
    private VLayout contentsPanel = new VLayout(10); // form contents
    private VLayout resultsPanel = new VLayout(); // bottom label
    private Label headerLabel = new Label();
    private HLayout titleAndHelpButton = new HLayout();
    private HelpButton helpButton;
    protected WaitPanel waitPanel = new WaitPanel();

    public GenericToolTab(String header) {
        super(header);
        setHeader(header);

        // set the left canvas contents
        setFieldsCanvas(createContents());

        // create the validation results canvas
        createResultsPanel();

        // display attributes
        mainPanel.setLayoutMargin(10);
    }

    @Override
    public VLayout getContentsPanel() {
        return contentsPanel;
    }


    /**
     * This is the title of the tab, displayed in large characters at the top of the tab
     * @param s the title or header of the tab
     */
    @Override
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



    /**
     * Creates a subtitle. Other, smaller subtitle functions can be later created in the same manner.
     * @param s
    @Override
     */
    public IconLabel createSubtitle1(String s){
        return Formatter.createSubtitle1(s);
    }

    /**
     * Sets the contents of a tab, inside its left panel. This
     * is usually the fields intended to be filled out by the user.
     * @param pane The tab contents
     */
    public void setFieldsCanvas(Widget pane){
        // add the form to be filled out by the user, to the left contents panel
        contentsPanel.addMember(pane);
        // populate the top panel (contents panel | help panel)
        topPanel.addMembers(contentsPanel, helpPanel);
        // add the top panel to the overall tab canvas
        mainPanel.addMember(topPanel);
        setPane(mainPanel);
        // display features
        getPane().setAlign(Alignment.CENTER);
        topPanel.setLayoutBottomMargin(20);
    }

    /**
     * Adds a panel for the validation results
     */
    @Override
    public void createResultsPanel(){
        mainPanel.addMember(resultsPanel);
        setPane(mainPanel);
    }

    /**
     * Changes the contents of the result panel
     * @param canvas
     */
    @Override
    public void setResultsPanel(Canvas canvas){
        resultsPanel.clear();
        resultsPanel.addMember(canvas);
    }

    @Override
    public VLayout getResultsPanel(){
        return mainPanel;
    }

    /**
     * Removes the main header of a tab
     */
    @Override
    public void removeHeaderTitle(){
        mainPanel.removeMember(titleAndHelpButton);
    }

    /**
     * Add a help button to the top right corner of the tab
     */
    @Override
    public void setHelpButton(Canvas container, String contents){
        helpButton = new HelpButton(container, contents);
        titleAndHelpButton.addMember(helpButton);
        titleAndHelpButton.setHeight(30);
        titleAndHelpButton.redraw();
    }

    /**
     * Hides the help button
     */
    @Override
    public void hideHelpButton(){
        contentsPanel.hideMember(helpButton);
    }

    /**
     * Changes the contents of the Help Window, displayed when clicking on the Help Button
     * @param contents
     */
    @Override
    public void setHelpWindowContents(String contents){
        helpButton.setHelpWindowContents(contents);
    }

    @Override
    public VLayout getHelpPanel() {
        return helpPanel;
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
