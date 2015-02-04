package gov.nist.hit.ds.docentryeditor.client.generics.abstracts;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;

import java.util.Map;
import java.util.logging.Logger;

public abstract class AbstractView<P extends AbstractPresenter<?>> implements IsWidget {

    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    protected Widget ui;
    protected P presenter;
    protected Map<String, Widget> pathToWidgetsMap;
    ContentPanel cp;

    // instance
    public AbstractView() {
    }

    public void init() {
        cp = new ContentPanel();
        cp.setHeaderVisible(false);
        cp.setBorders(false);
        pathToWidgetsMap = getPathToWidgetsMap();
        ui = buildUI();
        cp.setWidget(ui);
        bindUI();

        // FIXME find a way to resolve resizing issues with fields Map solution is supposed to work but I don't know how...
//        ResizeHandler resizeHandler=new ResizeHandler() {
//            @Override
//            public void onResize(ResizeEvent event) {
//                for(Widget w : pathToWidgetsMap.values()){
////                    w.setWidth("200");
//                    cp.forceLayout();
//                    cp.setResize(true);
//                }
//            }
//        };
//        cp.addResizeHandler(resizeHandler);
//        Window.addResizeHandler(resizeHandler);
    }

    protected abstract Map<String, Widget> getPathToWidgetsMap();


    public void start() {
    }

    // abstract
    abstract protected Widget buildUI();

    abstract protected void bindUI();


    // impl
    @Override
    public Widget asWidget() {
        if (ui == null || cp == null)
            init();
        return cp;
    }

    // getter / setter
    public P getPresenter() {
        return presenter;
    }

    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }
}
