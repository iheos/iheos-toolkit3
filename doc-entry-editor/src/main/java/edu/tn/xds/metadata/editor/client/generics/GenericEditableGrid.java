package edu.tn.xds.metadata.editor.client.generics;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import edu.tn.xds.metadata.editor.client.resources.AppImages;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Generic widget for Inline Grid editing on double click. For each editable
 * field of the grid the method
 * {@link #addColumnEditorConfig(ColumnConfig, Field) addColumnEditorConfig}
 * should be called.
 * <p/>
 * <br/>
 * <br/>
 * Example:
 * <p/>
 * <p/>
 * <pre>
 * <code>
 *     public class InternationalStringEditableGrid extends GenericEditableGrid<InternationalString> {
 *
 *          InternationalStringProperties isprops = GWT.create(InternationalStringProperties.class);
 *
 *          public InternationalStringEditableGrid(String gridTitle) {
 *              super(gridTitle, new ListStore<InternationalString>(isprops.key()));
 *          }
 *          @Override
 *          protected ColumnModel<InternationalString> buildColumnModel() {
 *              List<ColumnConfig<InternationalString, ?>> columnsConfigs = new ArrayList<ColumnConfig<InternationalString, ?>>();
 *              languageCodeColumnConfig = new ColumnConfig<InternationalString, LanguageCode>(isprops.langCode(), 15, "Language Code");
 *              titleColumnConfig = new ColumnConfig<InternationalString, String>(isprops.value(), 85, "Title");
 *              columnsConfigs.add(languageCodeColumnConfig);
 *              columnsConfigs.add(titleColumnConfig);
 *              return new ColumnModel<InternationalString>(columnsConfigs);
 *          }
 *          @Override
 *          protected void buildEditingFields() {
 *              LanguageCodeComboBox languageCodeComboBox = new LanguageCodeComboBox();
 *              BoundedTextField tf = new BoundedTextField();
 *              addColumnEditorConfig(languageCodeColumnConfig, languageCodeComboBox);
 *              addColumnEditorConfig(titleColumnConfig, tf);
 *          }
 *          @Override
 *          protected GridModelFactory<InternationalString> getModelFactory() {
 *              return InternationalStringFactory.instance;
 *          }
 *      }
 * </code>
 * </pre>
 *
 * @see Grid <p/>
 * Created by onh2 on 6/10/2014.
 */
public abstract class GenericEditableGrid<M> extends Grid<M> {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    // private Class<M> clazzM;

    private final VerticalLayoutContainer gridContainer = new VerticalLayoutContainer();
    private final ToolBar toolBar = new ToolBar();
    private final TextButton newItemButton = new TextButton();
    private final TextButton deleteItemsButton = new TextButton();
    private final TextButton clearButton = new TextButton();
    private final TextButton helpButton = new TextButton();
    private final ContentPanel pane = new ContentPanel();
    private final ToolTipConfig helpTooltipConfig = new ToolTipConfig();
    protected GridRowEditing<M> editing;
    boolean isAutoShow = true;
    // Toolbar elements
    private boolean hasToolbar = true;
    private int storeMaxLength = 0;

    private boolean checkBoxEnabled = false;
    private boolean hasHelpButtonEnabled = false;
    private boolean hasExtraWidget = false;

    /**
     * Grid constructor.
     * <code>new GenericEditableGrid("gridTitle", new ListStore<InternationalString>(GWT.create(InternationalStringProperties.class).key()));</code>
     *
     * @param gridTitle Grid's panel title
     * @param listStore Grid's ListStore
     */
    public GenericEditableGrid(/* Class<M> parametrizedClass, */String gridTitle, ListStore<M> listStore) {
        super(listStore, new ColumnModel<M>(new ArrayList<ColumnConfig<M, ?>>()));

        this.cm = buildColumnModel();

        // clazzM = parametrizedClass;
        pane.setHeadingText(gridTitle);

        this.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);

        this.getView().setAutoFill(true);
        this.setBorders(false);

        this.addStyleName("grid-minheight");
        this.setHeight(200);

        newItemButton.setIcon(AppImages.INSTANCE.add());
        newItemButton.setToolTip("Add an new element");
        toolBar.add(newItemButton);
        deleteItemsButton.setIcon(AppImages.INSTANCE.delete());
        deleteItemsButton.setToolTip("Delete selected element(s)");
        toolBar.add(deleteItemsButton);
        clearButton.setIcon(AppImages.INSTANCE.clear());
        clearButton.setToolTip("Clear all elements");
        toolBar.add(clearButton);
        helpButton.setIcon(AppImages.INSTANCE.help());
        helpButton.setTitle("Help?");

        helpTooltipConfig.setTitleText("Widget help?");
        helpTooltipConfig.setAnchor(Style.Side.LEFT);
        helpTooltipConfig.setCloseable(true);
        helpTooltipConfig.setMouseOffsetX(0);
        helpTooltipConfig.setMouseOffsetY(0);

        gridContainer.add(toolBar);
        gridContainer.add(super.asWidget(), new VerticalLayoutContainer.VerticalLayoutData(1, 1)); // VerticalLayoutData does not work here why?
        pane.setWidget(gridContainer);

        setEditable();
        buildEditingFields();

        // some tries to make grid fit panel's height
        this.getView().setAdjustForHScroll(false);
        this.getView().setForceFit(true);
        pane.forceLayout();
        gridContainer.forceLayout();

        bindUI();
    }

    /**
     * Abstract method that need to be implemented in childs to build the Grid's
     * ColumnModel.
     * For instance:
     * <code>
     *     private static ColumnConfig<InternationalString, LanguageCode> languageCodeColumnConfig;
     *     private static ColumnConfig<InternationalString, String> titleColumnConfig;
     *     @Override
     *     protected ColumnModel<InternationalString> buildColumnModel() {
     *          List<ColumnConfig<InternationalString, ?>> columnsConfigs = new ArrayList<ColumnConfig<InternationalString, ?>>();
     *          languageCodeColumnConfig = new ColumnConfig<InternationalString, LanguageCode>(isprops.langCode(), 15, "Language Code");
     *          titleColumnConfig = new ColumnConfig<InternationalString, String>(isprops.value(), 85, "Title");
     *          columnsConfigs.add(languageCodeColumnConfig);
     *          columnsConfigs.add(titleColumnConfig);
     *          return new ColumnModel<InternationalString>(columnsConfigs);
     *     }
     * </code>
     * @return Grid's ColumnModel<M>
     */
    protected abstract ColumnModel<M> buildColumnModel();

    /**
     * Abstract method that is supposed to build, and to each desired ColomnConfig
     * a Field widget to edit the column content in edition mode. This must be
     * implemented in childs classes.
     *
     * For instance:
     * <code>
     *     private static ColumnConfig<InternationalString, LanguageCode> languageCodeColumnConfig;
     *     private static ColumnConfig<InternationalString, String> titleColumnConfig;
     *     @Override
     *     protected void buildEditingFields() {
     *          LanguageCodeComboBox languageCodeComboBox = new LanguageCodeComboBox();
     *          BoundedTextField tf = new BoundedTextField();
     *          addColumnEditorConfig(languageCodeColumnConfig, languageCodeComboBox);
     *          addColumnEditorConfig(titleColumnConfig, tf);
     *     }
     * </code>
     */
    protected abstract void buildEditingFields();

    /**
     * Abstract method to get the right GridModelFactory for the EditableGrid.
     * <code>
     *     @Override
     *     protected GridModelFactory<InternationalString> getModelFactory() {
     *          return InternationalStringFactory.instance;
     *     }
     * </code>
     * @return GridModelFactory<M>
     */
    protected abstract GridModelFactory<M> getModelFactory();

    /**
     * Method to add a new widget to the EditbleGrid widget.
     * For exemple:
     * <code>
     *     @Override
     *     public Widget getDisplay() {
     *          FieldLabel codedTermFL = new FieldLabel(cb, "Select a coded term to add");
     *          codedTermFL.setLabelWidth(200);
     *          addWidget(codedTermFL);
     *          return super.getDisplay();
     *     }
     * </code>
     * @param widget
     */
    protected void addWidget(Widget widget) {
        hasExtraWidget = true;
        widget.addStyleName("topBorder");
        gridContainer.add(widget, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0, 4, 1, 4)));
    }

    /**
     * Method to add several widget to the EditableGrid widget.
     * It's the similar to addWidget(Widget widget) method but
     * with the possiblity to add more than one widget in parameter.
     *
     * @param widgets
     */
    protected void addWidgets(Widget... widgets) {
        hasExtraWidget = true;
        boolean firstDone = false;
        for (Widget w : widgets) {
            if (firstDone == false) {
                w.addStyleName("topBorder");
                firstDone = true;
            }
            gridContainer.add(w, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0, 4, 1, 4)));
        }
    }

    /**
     * Needs to be used to get the Grid Editable. It configures how the columns
     * will be edited.
     *
     * @param columnConfig Grid's Column Configuration which will be associated to a type
     *                     of editable field
     * @param field        Editable field which will be used to edit the grid's Column
     */
    protected  <N> void addColumnEditorConfig(ColumnConfig<M, N> columnConfig, Field<N> field) {
        editing.addEditor(columnConfig, field);
    }

    /**
     * Method that return the entire EditableGrid widget's UI. It should be used as asWidget()
     * method and have been added to avoid an behavior error due to asWidget() use elsewhere.
     *
     * @return entire EditableGrid widget's UI
     */
    public Widget getDisplay() {
        pane.setResize(true);
        return pane.asWidget();
    }

    /**
     * Method that bind the tooltips ui with custom actions
     */
    private void bindToolTips() {
        helpButton.getToolTip().addShowHandler(new ShowEvent.ShowHandler() {
            @Override
            public void onShow(ShowEvent event) {
                if (isAutoShow)
                    helpButton.getToolTip().hide();
            }
        });
        helpButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (isAutoShow) {
                    isAutoShow = false;
                    helpButton.getToolTip().show();
                } else {
                    helpButton.getToolTip().hide();
                }
            }
        });
        helpButton.getToolTip().addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                isAutoShow = true;
            }
        });
    }

    /**
     * Method that binds the UI with the right actions (button click handlers, event handlers,...)
     */
    private void bindUI() {
        // NEW BUTTON CLICK HANDLER
        newItemButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent selectEvent) {
                // try to fire an event
                if (getStore().size() < storeMaxLength || storeMaxLength == 0) {
                    editing.cancelEditing();
                    // this method should work I don't understand why there is a problem.
                    // M element = GWT.create(clazzM);
                    M element = getModelFactory().newInstance();
                    getStore().add(0, element);
                    int index = 0;
                    if (isCheckBoxEnabled()) {
                        index = 1;
                    }
                    editing.startEditing(new Grid.GridCell(getStore().indexOf(element), index));
                    if (getStore().size() >= storeMaxLength && storeMaxLength != 0) {
                        disableNewButton();
                    }
                } else {
                    MessageBox mb = new MessageBox("Error: list size limit reached",
                            "You can not add more items to that list. This list can contain only " + storeMaxLength
                                    + " items.");
                    mb.show();
                }
            }
        });
        // DELETE BUTTON CLICK HANDLER
        deleteItemsButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                final ConfirmMessageBox d = new ConfirmMessageBox("Confirm delete action",
                        "Are you sure you want to delete these values?");
                d.show();
                d.addHideHandler(new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        if (d.getHideButton() == d.getButtonById(Dialog.PredefinedButton.YES.name())) {
                            deleteItemAction();
                            if (getStore().size() < storeMaxLength && storeMaxLength != 0) {
                                enableNewButton();
                            }
                        }
                    }
                });
            }
        });
        // CLEAR BUTTON CLICK HANDLER
        clearButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                final ConfirmMessageBox d = new ConfirmMessageBox("Confirm clear action",
                        "Are you sure you want to delete all values from this grid?");
                d.show();
                d.addHideHandler(new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        if (d.getHideButton() == d.getButtonById(Dialog.PredefinedButton.YES.name())) {
                            clearStoreAction();
                            enableNewButton();
                        }
                    }
                });
            }
        });
    }

    /**
     * Method that performs the entire deletion of the grid's store.
     */
    protected void clearStoreAction() {
        getStore().clear();
        getStore().commitChanges();
    }

    /**
     * Method that performs the deletion of selected items in the grid's store.
     */
    protected void deleteItemAction() {
        for (M e : getSelectionModel().getSelectedItems()) {
            getStore().remove(e);
            getStore().commitChanges();
        }
    }

    /**
     * Method to disable the edition mode on the grid.
     * (not sure it is useful)
     */
    public void disableEditing() {
        disableToolbar();
        editing.clearEditors();
    }

    /**
     * Method to disable NEW button in toolbar.
     */
    public void disableNewButton() {
        newItemButton.disable();
    }

    /**
     * Method that disable and hide the entire grid's toolbar.
     */
    public void disableToolbar() {
        hasToolbar = false;
        toolBar.disable();
        toolBar.setVisible(false);
    }

    /**
     * Method that enables the NEW button
     */
    public void enableNewButton() {
        newItemButton.enable();
    }

    /**
     * Getter for the Grid's toolbar
     * @return grid's toolbar widget
     */
    public ToolBar getToolbar() {
        return toolBar;
    }

    /**
     * Method telling if the checkbox selection model have been enabled for the grid or not.
     * @return
     * true - if checkbox selection model is enabled
     * false - otherwise
     */
    public boolean isCheckBoxEnabled() {
        return checkBoxEnabled;
    }

    //--------------------------------------------------------------------------------------------------------
    //----  SHOULD NOT BE USED WITH GIRD ROW EDITING, ONLY WITH GRID INLINE EDITING
//    /**
//     * This Method enables the grid's selection checkbox column (for
//     * multiselection).
//     */
//    public void setCheckBoxSelectionModel() {
//        checkBoxEnabled = true;
//        List<ColumnConfig<M, ?>> columnsConfigs = new ArrayList<ColumnConfig<M, ?>>();
//        IdentityValueProvider<M> identityValueProvider = new IdentityValueProvider<M>();
//        CheckBoxSelectionModel<M> selectColumn = new CheckBoxSelectionModel<M>(identityValueProvider);
//        selectColumn.setSelectionMode(Style.SelectionMode.MULTI);
//        columnsConfigs.add(selectColumn.getColumn());
//        columnsConfigs.addAll(cm.getColumns());
//
//        this.cm = new ColumnModel<M>(columnsConfigs);
//
//        this.setSelectionModel(selectColumn);
//
//        this.getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<M>() {
//            @Override
//            public void onSelectionChanged(SelectionChangedEvent<M> event) {
//                // TODO whatever you have to do
//            }
//        });
//
//        setEditable();
//    }
    //--------------------------------------------------------------------------------------------------------

    /**
     * Method to make the grid editable on double click.
     */
    protected void setEditable() {
        // EDITING //
        editing = new GridRowEditing<M>(this);
        editing.setClicksToEdit(ClicksToEdit.TWO);
        editing.addCompleteEditHandler(new CompleteEditEvent.CompleteEditHandler<M>() {
            @Override
            public void onCompleteEdit(CompleteEditEvent<M> completeEditEvent) {
                getStore().commitChanges();
            }
        });
    }

    /**
     * Method to make the grid editable on a specific number of clicks.
     * @param clicksToEdit number of clicks to enter the grid's edition mode.
     */
    protected void setEditable(ClicksToEdit clicksToEdit) {
        // EDITING //
        editing = new GridRowEditing<M>(this);
        editing.setClicksToEdit(clicksToEdit);
        editing.addCompleteEditHandler(new CompleteEditEvent.CompleteEditHandler<M>() {
            @Override
            public void onCompleteEdit(CompleteEditEvent<M> completeEditEvent) {
                getStore().commitChanges();
            }
        });
    }

    /**
     * Method to make the grid's header visible.
     * @param visible true - header visible
     *                false - header invisible
     */
    public void setHeaderVisible(boolean visible) {
        pane.setHeaderVisible(visible);
    }

    /**
     * Method to give a custom height the grid.
     * @param height custom height in pixels
     */
    public void setHeight(int height) {
        gridContainer.setHeight(height);
        pane.setHeight(height);
    }

    /**
     * Method to set the maximum number of elements that can be stored in the grid.
     * @param storeMaxLength number of elements that can be stored in the grid
     */
    protected void setStoreMaxLength(int storeMaxLength) {
        this.storeMaxLength = storeMaxLength;
        // calculate custom height based on the number of elements the grid can store
        if (storeMaxLength == 1) {
            this.setHeight(70 + (hasToolbar == true ? 25 : 0) + (hasExtraWidget == true ? 20 : 0));
        } else {
            if (storeMaxLength < 11) {
                this.setHeight((20 * storeMaxLength) + (hasToolbar == true ? 20 : 0) + (hasExtraWidget == true ? 20 : 0));
            }
        }
    }

    /**
     * Set help content for the grid widget toolbar help button
     * @param helpContent
     */
    public void setToolbarHelpButton(String helpContent) {
        helpTooltipConfig.setBodyHtml(helpContent);
        if (!hasHelpButtonEnabled) {
            toolBar.add(helpButton);
            hasHelpButtonEnabled = true;
        }
        helpButton.setToolTipConfig(helpTooltipConfig);
        Draggable d = new Draggable(helpButton.getToolTip());
        d.setUseProxy(false);
        bindToolTips();
    }

    /**
     * Set help content for the grid widget toolbar help button
     * @param helpContent
     */
    public void setToolbarHelpButtonTooltip(ToolTipConfig helpContent) {
        helpTooltipConfig.setBodyHtml(helpContent.getBodyHtml());
        helpTooltipConfig.setTitleHtml(helpContent.getTitleHtml());
        if (!hasHelpButtonEnabled) {
            toolBar.add(helpButton);
            hasHelpButtonEnabled = true;
        }
        helpButton.setToolTipConfig(helpTooltipConfig);
        Draggable d = new Draggable(helpButton.getToolTip());
        d.setUseProxy(false);
        bindToolTips();
    }

    /**
     * Getter that return the maximum number of elements the grid can store.
     * @return maximum number of elements the grid can store.
     */
    public int getStoreMaxSize() {
        return storeMaxLength;
    }
}
