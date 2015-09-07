package gov.nist.hit.ds.xdstools3.client.tabs;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.ui.client.widgets.EventAggregatorWidget;
import gov.nist.hit.ds.xdstools3.client.customWidgets.design.IconLabel;
import gov.nist.hit.ds.xdstools3.client.customWidgets.WaitPanel;
import gov.nist.hit.ds.xdstools3.client.customWidgets.buttons.HelpButton;
import gov.nist.hit.ds.xdstools3.client.customWidgets.design.Formatter;
import gov.nist.hit.ds.xdstools3.client.manager.Manager;

public abstract class GenericCloseableToolTab extends GenericCloseableTab implements ToolTabInterface {

    private VLayout mainPanel = new VLayout();
    private VLayout helpPanel = new VLayout(); // this is the right panel
    private HLayout topPanel = new HLayout();  // contains contentsPanel and resultsPanel
    private VLayout contentsPanel = new VLayout(10); // form contents
    private VLayout resultsPanel; // bottom label
    private Label headerLabel; // title label
    private HLayout titleAndHelpButton = new HLayout();
    private HelpButton helpButton;
    private EventAggregatorWidget eventMessageAggregatorWidget;
    protected WaitPanel waitPanel = new WaitPanel();


    // Initial event summary widget parameters
    String id = null;
    String type = "validators";
    String[] displayColumns = new String[]{"ID","STATUS","MSG"};



    public GenericCloseableToolTab(String header) {
        super(header);
        setHeader(header);

        // set the left canvas contents
        setFieldsCanvas(createContents());

        // create and populate the validation results panel with initialized EventAggregatorWidget
        resultsPanel = new VLayout();
        resultsPanel.addMember(setupEventMessagesWidget(EventAggregatorWidget.ASSET_CLICK_EVENT.OUT_OF_CONTEXT, "Sim", id, type, displayColumns));
        mainPanel.addMember(resultsPanel);
        setPane(mainPanel);

        // display attributes
        mainPanel.setLayoutMargin(20);
        resultsPanel.setLayoutMargin(10);
        }

    @Override
    public VLayout getContentsPanel() {
        return contentsPanel;
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
        return resultsPanel;
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

    public EventAggregatorWidget getEventMessageAggregatorWidget(){ return eventMessageAggregatorWidget; }

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
     * This is the title of the tab, displayed in large characters at the top of the tab. The intent is that a developer
     * can call this function if they wish to display a title at the top of the tab they are working on.
     * @param s the title or header of the tab
     */
    public void setHeader(String s){
        // create the header of the tab
        headerLabel = Formatter.createTabHeader(s);

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
     * Method that return a panel displaying a message asking to wait
     * while the system is processing.
     *
     * <strong>How to:</strong>
     * To display the panel, use {@link gov.nist.hit.ds.xdstools3.client.customWidgets.WaitPanel#show()}
     * method before system start processing.
     * To hide the panel, use {@link gov.nist.hit.ds.xdstools3.client.customWidgets.WaitPanel#hide()}
     * method when system finishes processing.
     *
     * @see gov.nist.hit.ds.xdstools3.client.customWidgets.WaitPanel
     *
     * @return WaitPanel
     */
    protected WaitPanel getWaitPanel(){
        return waitPanel;
    }


    /**
     * Initializes the Event Message Widget to be populated with the validation result
     */
    protected Widget setupEventMessagesWidget(EventAggregatorWidget.ASSET_CLICK_EVENT assetClickEvent, String externalRepositoryId, String eventAssetId, String type, String[] displayColumns) {

        try {
            // Initialize the widget
            eventMessageAggregatorWidget = new EventAggregatorWidget(Manager.EVENT_BUS, assetClickEvent, externalRepositoryId,eventAssetId,type,displayColumns);
            eventMessageAggregatorWidget.setSize("1110px", "600px");
            return eventMessageAggregatorWidget;

        } catch (Throwable t) {
            Window.alert("EventAggregatorWidget instance could not be created: " + t.toString());
        }
        return null;
    }

    /**
     * Display validation results and hides the wait panel
     *
     * @param assetNode Validation result from RPC validation
     */
    public void displayValidationResults(AssetNode assetNode) {
        if (!getResultsPanel().isVisible()){
            getResultsPanel().setVisible(true);
        }
        getEventMessageAggregatorWidget().setEventAssetNode(assetNode);
        getResultsPanel().redraw();
        getContentsPanel().hideMember(waitPanel);
    }
}
