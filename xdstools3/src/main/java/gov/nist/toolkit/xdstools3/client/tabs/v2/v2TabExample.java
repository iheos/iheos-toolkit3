package gov.nist.toolkit.xdstools3.client.tabs.v2;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.events.PaneChangedEvent;
import com.smartgwt.client.widgets.layout.events.PaneChangedHandler;
import com.smartgwt.client.widgets.tab.Tab;
import gov.nist.toolkit.xdstools2.client.TabContainer;
import gov.nist.toolkit.xdstools2.client.adapter2v3.TopWindowPanel;
import gov.nist.toolkit.xdstools2.client.tabs.messageValidator.MessageValidatorTab;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

import java.util.logging.Logger;


/**
 * Created by dazais on 9/9/2014.
 */
public class v2TabExample extends Tab {
    static String header = "v2 tab";
    private TabContainer tabContainer;
    private MessageValidatorTab tabv2;
    private VLayout panel;


    public v2TabExample(TabContainer _tabContainer) {
        super(header);
        this.setCanClose(true);
        tabContainer = _tabContainer;

        if (tabContainer == null) System.out.println("tab container is null in Emptytab class");

        tabv2 = new MessageValidatorTab();

        // container must not be null else it generates errors in the rest of the code
        Logger.getLogger(this.getClass().getName()).info("tabContainer == null?: " +(tabContainer==null));
        System.out.println("tabContainer == null?: " +(tabContainer==null));
        tabv2.onTabLoad(tabContainer, true, null); //(TabContainer container, boolean select, String eventName) );

        // Set contents of the main panel
        panel = new VLayout(10);
        TopWindowPanel v2GWTPanel = tabv2.getTopPanel();
        updateV2Panel(v2GWTPanel);

        v2GWTPanel.addPaneChangedHandler(new PaneChangedHandler() {
            @Override
            public void onPaneChanged(PaneChangedEvent paneChangedEvent) {
                Logger.getLogger(this.getClass().getName()).info("PaneChangedEvent caught in v2TabExample");
                updateV2Panel(tabv2.getTopPanel());
            }
    });

    }

    private void updateV2Panel(TopWindowPanel v2Panel) {
        panel.clear();
        panel.addMember(v2Panel);
        this.setPane(panel);
    }

    public String getTabName() {
        return TabNamesUtil.getInstance().getv2TabCode();
    }
}
