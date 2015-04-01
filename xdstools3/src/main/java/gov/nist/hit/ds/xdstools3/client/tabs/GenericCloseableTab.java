package gov.nist.hit.ds.xdstools3.client.tabs;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import gov.nist.hit.ds.xdstools3.client.util.eventBus.CloseTabEvent;
import gov.nist.hit.ds.xdstools3.client.manager.Manager;
import gov.nist.hit.ds.xdstools3.client.util.eventBus.CloseAllTabsEvent;
import gov.nist.hit.ds.xdstools3.client.util.eventBus.CloseOtherTabsEvent;

/**
 * Structure for closeable tabs
 */
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
                Manager.EVENT_BUS.fireEvent(new CloseTabEvent(tab));
            }
        });
        closeAllTabBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent menuItemClickEvent) {
                Manager.EVENT_BUS.fireEvent(new CloseAllTabsEvent());
            }
        });
        closeOtherTabsBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent menuItemClickEvent) {
                Manager.EVENT_BUS.fireEvent(new CloseOtherTabsEvent(tab));
            }
        });
    }

}