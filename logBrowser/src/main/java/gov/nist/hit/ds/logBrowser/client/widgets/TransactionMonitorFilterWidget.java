package gov.nist.hit.ds.logBrowser.client.widgets;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sun.org.apache.xpath.internal.operations.Bool;
import gov.nist.hit.ds.logBrowser.client.event.NewTxMessageEvent;
import gov.nist.hit.ds.logBrowser.client.event.NewTxMessageEventHandler;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryService;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryServiceAsync;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm;

import java.util.List;
import java.util.logging.Logger;

public class TransactionMonitorFilterWidget extends Composite {
	/**
	 *
	 * @author Sunil.Bhaskarla
	 */
	private static Logger logger = Logger.getLogger(TransactionMonitorFilterWidget.class.getName());

	final public RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);
	private SimpleEventBus eventBus;
    private SearchWidget searchWidget = null;


    private TransactionMonitorWidget txMonitorLive;
    private TransactionMonitorWidget txFilter;

    /*
    Sample 2-way Exchange pattern txDetail:
    20140326160812,"RESPONSE", "","500","localhost","localhost:8080","localhost:8001^ProxyRuleMappingName: localcap","text/html","","65","0"
     */




    public TransactionMonitorFilterWidget(SimpleEventBus eventBus)  {
	    this.eventBus = eventBus;

        // All composites must call initWidget() in their constructors.
	     initWidget(setupMainPanel());

    }



    protected Widget setupMainPanel() {
        SplitLayoutPanel mainSplitPanel = new SplitLayoutPanel(3);

        mainSplitPanel.addSouth(setupMonitor(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE), Math.round(.5 * Window.getClientHeight()));

        mainSplitPanel.add(setupFilterBasedResultsPanel()); // Filter selection and results stack panel

        return mainSplitPanel;
    }


    protected Widget setupFilterBasedResultsPanel() {

        // SplitLayoutPanel filterSplitPanel = new SplitLayoutPanel(2);

        // Create a new stack layout panel.
        StackLayoutPanel stackPanel = new StackLayoutPanel(Style.Unit.EM);
        //stackPanel.setHeight("100%");
        stackPanel.setWidth("100%");

        // Add filter section
        Widget filterHeader = createHeaderWidget("Filter",null, null);
        stackPanel.add(setupFilter(), filterHeader, 2);

        // filterSplitPanel.addWest(stackPanel, Math.round(.3 * Window.getClientWidth()));
        // stackPanel = new StackLayoutPanel(Style.Unit.EM);
        // stackPanel.setWidth("100%");

        HTML anchor = new HTML("[Show Message Detail] [Clear]" );
        anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getTxMonitorLive().showTxDetail();
            }
        });
        // Add monitor section
        Widget resultsHeader = createHeaderWidget("Transaction Monitor",
                anchor, null);
        stackPanel.add(setupMonitor(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE), resultsHeader, 2);

        // filterSplitPanel.add(stackPanel);

        return stackPanel;
    }

    private Widget setupMonitor(Boolean enableListener, Boolean stackPanelWrapper, Boolean showTxDetail) {

        TransactionMonitorWidget txMonitor = new TransactionMonitorWidget(eventBus,enableListener,showTxDetail);
        txMonitor.getElement().getStyle()
                .setProperty("border", "none");


        if (!stackPanelWrapper) {
            setTxMonitorLive(txMonitor);
            return txMonitor;
        } else {
            setTxFilter(txMonitor);
        }

        StackLayoutPanel stackPanel = new StackLayoutPanel(Style.Unit.EM);
        //stackPanel.setHeight("100%");
        stackPanel.setWidth("100%");

        Widget liveHeader = createHeaderWidget("Filtered Transactions", new Anchor("[Clear]"  ,""), null);

        stackPanel.add(txMonitor, liveHeader, 2);

        return stackPanel;

    }

    private Widget setupFilter() {
        ScrollPanel searchPanel = new ScrollPanel(); 				// Search parameters

        searchWidget = new SearchWidget(eventBus, new SearchWidget.Option[]{
                SearchWidget.Option.QUICK_SEARCH,
                SearchWidget.Option.TWO_SEARCH_TERMS_PER_ROW
                // SearchWidget.Option.SEARCH_CRITERIA_REPOSITORIES,
                // SearchWidget.Option.CRITERIA_BUILDER_MODE
        });

        searchWidget.getElement().getStyle()
                .setProperty("border", "none");
        searchWidget.getElement().getStyle()
                .setPaddingLeft(3, Style.Unit.PX);

        searchPanel.add(searchWidget);

        eventBus.addHandler(NewTxMessageEvent.TYPE, new NewTxMessageEventHandler() {
            @Override
            public void onNewTxMessage(NewTxMessageEvent event) {

                // Get criteria and append the location and check the searchresultiterator to see if it exists/count match
                // call method with assetNode list to populate the filtered list

                activateFilter(event.getValue(), event.getAssetNode().getLocation());
            }
        });

        return searchPanel;
    }

    protected void activateFilter(final int idx, String location) {

        if (location==null) {
            logger.severe("Asset location is missing!");
            return;
        }

        int itemCt = searchWidget.getReposRight().getItemCount();

        final VerticalPanel resultPanel = searchWidget.getResultPanel();

        resultPanel.clear();

        if (itemCt==0) {
            setFilterError("No filter has been applied or the repository selection is missing!");
            return;
        } else {

            //resultPanel.add(new HTML("&nbsp;"));
            //resultPanel.add(new HTML("Searching..."));


            // Wrap into new sc
            SearchCriteria subCriteria = new SearchCriteria(SearchCriteria.Criteria.AND);
            subCriteria.append(new SearchTerm(PropertyKey.LOCATION,SearchTerm.Operator.EQUALTO,location));

            SearchCriteria criteria = new SearchCriteria(SearchCriteria.Criteria.AND);
            criteria.append(searchWidget.getSc());
            criteria.append(subCriteria);

            String[][] selectedRepos = searchWidget.getSelectedRepos(itemCt);

            reposService.searchHit(selectedRepos, criteria, Boolean.TRUE, new AsyncCallback<Boolean>() {

                public void onFailure(Throwable arg0) {
                    setFilterError("Filter error: " + arg0.getMessage());
                }

                public void onSuccess(Boolean hit) {
                    if (hit) {
                        //resultPanel.add(new HTML("hit "+idx));
                        //getTxFilter().setTxRowParentId(getTxMonitorLive().getTxRowParentId());
                        //getTxFilter().setTxRowAssetNode(getTxMonitorLive().getTxRowAssetNode());
                        List<AssetNode> anList = getTxMonitorLive().getTxRowAssetNode().get(new Integer(idx));
                        //resultPanel.add(new HTML("---"+ (anList==null) + " " + getTxMonitorLive().getTxRowAssetNode().size()));
                        getTxFilter().popTx(anList);
                    }
                }
            });


            // ((ListBox)event.getSource()).setEnabled(false);
        }

    }



    private void setFilterError(String msg) {
        searchWidget.getResultPanel().clear();
        searchWidget.getResultPanel().add(new HTML("<font color='red'>" + msg + "</font>"));

    }



            private Widget createHeaderWidget(String text, Widget additionalLink, ImageResource image) {
        // Add the image and text to a horizontal panel
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setHeight("100%");
        hPanel.setSpacing(0);
        hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        // hPanel.add(new Image(image));
        HTML headerText = new HTML(text + "&nbsp;");

        headerText.setStyleName("cw-StackPanelHeader");
        hPanel.add(headerText);

        if (additionalLink!=null) {
            hPanel.add(additionalLink);
        }

        SimplePanel sp =  new SimplePanel(hPanel);
        sp.addHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //
            }
        }, ClickEvent.getType() );

        return sp;
    }

    public TransactionMonitorWidget getTxMonitorLive() {
        return txMonitorLive;
    }

    public void setTxMonitorLive(TransactionMonitorWidget txMonitorLive) {
        this.txMonitorLive = txMonitorLive;
    }

    public TransactionMonitorWidget getTxFilter() {
        return txFilter;
    }

    public void setTxFilter(TransactionMonitorWidget txFilter) {
        this.txFilter = txFilter;
    }


}
