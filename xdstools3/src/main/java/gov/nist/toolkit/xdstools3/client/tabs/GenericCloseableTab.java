package gov.nist.toolkit.xdstools3.client.tabs;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import gov.nist.toolkit.xdstools3.client.eventBusUtils.CloseAllTabsEvent;
import gov.nist.toolkit.xdstools3.client.eventBusUtils.CloseOtherTabsEvent;
import gov.nist.toolkit.xdstools3.client.eventBusUtils.CloseTabEvent;
import gov.nist.toolkit.xdstools3.client.util.Util;

public abstract class GenericCloseableTab extends GenericTab {
    Menu tabMenu = new Menu();
    MenuItem closeTabBtn=new MenuItem("Close tab");
    MenuItem closeOtherTabsBtn=new MenuItem("Close other tabs");
    MenuItem closeAllTabBtn=new MenuItem("Close all tabs");

    public GenericCloseableTab(String header){
        super(header);
        setCanClose(true);
        this.setContextMenu(tabMenu);
        tabMenu.setItems(closeTabBtn, closeOtherTabsBtn, closeAllTabBtn);
        bindUI();
    }

    private void bindUI(){
        final Tab tab=this;
        closeTabBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent menuItemClickEvent) {
                Util.EVENT_BUS.fireEvent(new CloseTabEvent(tab));
            }
        });
        closeAllTabBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent menuItemClickEvent) {
                Util.EVENT_BUS.fireEvent(new CloseAllTabsEvent());
            }
        });
        closeOtherTabsBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent menuItemClickEvent) {
                Util.EVENT_BUS.fireEvent(new CloseOtherTabsEvent(tab));
            }
        });
    }

}
