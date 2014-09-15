package gov.nist.toolkit.xdstools3.client.tabs.v2;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import gov.nist.toolkit.xdstools2.client.TabContainer;


/**
 * Created by dazais on 9/9/2014.
 */
public class v2TabExample extends Tab {
    static String header = "v2 tab";
    private TabContainer tabContainer;


    public v2TabExample(TabContainer _tabContainer) {
        //super(header);

        tabContainer = _tabContainer;

        if (tabContainer == null) System.out.println("tab container is null in Emptytab class");

        gov.nist.toolkit.xdstools2.client.tabs.messageValidator.MessageValidatorTab tabv2 = new gov.nist.toolkit.xdstools2.client.tabs.messageValidator.MessageValidatorTab();
        // container must not be null else it generates errors in the rest of the code
        tabv2.onTabLoad(tabContainer, true, null); //(TabContainer container, boolean select, String eventName) );

        VLayout panel = new VLayout(10);
        panel.addMember(tabv2.getTopPanel());
        this.setPane(panel);
    }

}
