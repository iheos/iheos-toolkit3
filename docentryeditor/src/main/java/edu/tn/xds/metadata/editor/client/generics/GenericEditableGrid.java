package edu.tn.xds.metadata.editor.client.generics;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import edu.tn.xds.metadata.editor.client.resources.AppImages;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic widget for Inline Grid editing on double click.
 * For each editable field of the grid the method @see addEditorColumnConfig should be called.
 *
 * <br/><br/>Example:
 *
 * <pre>
 * <code>
 *      InternationalStringProperties isprops = GWT.create(InternationalStringProperties.class);
 *
 *      List<ColumnConfig<InternationalString, ?>> columnsConfigs = new ArrayList<ColumnConfig<InternationalString, ?>>();
 *      ColumnConfig<InternationalString, LanguageCode> cc1 = new ColumnConfig<InternationalString, LanguageCode>(isprops.langCode(), 15, "Language Code");
 *      ColumnConfig<InternationalString, String> cc2 = new ColumnConfig<InternationalString, String>(isprops.value(), 85, "Title");
 *      columnsConfigs.add(cc1);
 *      columnsConfigs.add(cc2);
 *      ColumnModel<InternationalString> columnModel = new ColumnModel<InternationalString>(columnsConfigs);
 *
 *      GenericEditableGrid<InternationalString> grid=new GenericEditableGrid<InternationalString>(InternationalString.class,"Title",new ListStore<InternationalString>(isprops.key()), columnModel);
 *      // grid.setCheckBoxSelectionModel();
 *
 *      LanguageCodeComboBox languageCodeComboBox = new LanguageCodeComboBox();
 *      languageCodeComboBox.setAllowBlank(false);
 *      // languageCodeComboBox.setToolTip("...");
 *      TextField tf = new TextField();
 *      tf.setAllowBlank(false);
 *      // tf.setToolTip("...");
 *
 *      grid.addColumnEditorConfig(cc1,languageCodeComboBox);
 *      grid.addColumnEditorConfig(cc2,tf);
 * </code>
 * </pre>
 *
 * @see Grid
 *
 * Created by onh2 on 6/10/2014.
 */
public class GenericEditableGrid<M> extends Grid<M> {

    private ContentPanel pane = new ContentPanel();
    private VerticalLayoutContainer gridContainer = new VerticalLayoutContainer();
    protected GridInlineEditing<M> editing;

    private Class<M> clazzM;

    // Toolbar elements
    private final ToolBar toolBar = new ToolBar();
    private final TextButton newItemButton = new TextButton();
    private final TextButton deleteItemsButton = new TextButton();
    private final TextButton clearButton = new TextButton();
    private boolean checkBoxenabled=false;

    // TODO check if there is not a better way to check parameterized class type.
    public GenericEditableGrid(Class<M> parametrizedClass,String gridTitle,ListStore<M> listStore,ColumnModel<M> cm){
        super(listStore,cm);

        clazzM=parametrizedClass;
        pane.setHeadingText(gridTitle);

        this.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        this.getView().setAutoFill(true);
        this.setBorders(false);

        newItemButton.setIcon(AppImages.INSTANCE.add());
        newItemButton.setToolTip("Add an new element");
        toolBar.add(newItemButton);
        deleteItemsButton.setIcon(AppImages.INSTANCE.delete());
        deleteItemsButton.setToolTip("Delete selected element(s)");
        toolBar.add(deleteItemsButton);
        clearButton.setIcon(AppImages.INSTANCE.clear());
        clearButton.setToolTip("Clear all elements");
        toolBar.add(clearButton);

        gridContainer.add(toolBar);
        gridContainer.add(this);
        pane.add(gridContainer);

        setEditable();

        bindUI();
    }

    protected void setEditable() {
        // EDITING//
        editing = new GridInlineEditing<M>(this);
        editing.setClicksToEdit(ClicksToEdit.TWO);
        editing.addCompleteEditHandler(new CompleteEditEvent.CompleteEditHandler<M>() {
            @Override
            public void onCompleteEdit(
                    CompleteEditEvent<M> completeEditEvent) {
                getStore().commitChanges();
            }
        });
    }

    private void bindUI() {
        newItemButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent selectEvent) {
                editing.cancelEditing();
                M element = GWT.create(clazzM);
                getStore().add(0, element);
                int index=0;
                if(checkBoxenabled){
                    index=1;
                }
                editing.startEditing(new Grid.GridCell(getStore().indexOf(
                        element), index));
            }
        });
        deleteItemsButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                final ConfirmMessageBox d=new ConfirmMessageBox("Confirm delete action","Are you sure you want to delete these values?");
                d.show();
                d.addHideHandler(new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        if(d.getHideButton()==d.getButtonById(Dialog.PredefinedButton.YES.name())){
                            for(M e:getSelectionModel().getSelectedItems()){
                                getStore().remove(e);
                            }
                        }
                    }
                });
            }
        });
        clearButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                final ConfirmMessageBox d = new ConfirmMessageBox("Confirm clear action", "Are you sure you want to delete all values from this grid?");
                d.show();
                d.addHideHandler(new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        if (d.getHideButton() == d.getButtonById(Dialog.PredefinedButton.YES.name())) {
                            getStore().clear();
                        }
                    }
                });
            }
        });

    }

    /**
     * This Method enables the grid's selection checkbox column (for multiselection).
     */
    public void setCheckBoxSelectionModel(){
        checkBoxenabled = true;
        List<ColumnConfig<M, ?>> columnsConfigs = new ArrayList<ColumnConfig<M, ?>>();
        IdentityValueProvider<M> identityValueProvider = new IdentityValueProvider<M>();
        CheckBoxSelectionModel<M> selectColumn = new CheckBoxSelectionModel<M>(
                identityValueProvider);
        selectColumn.setSelectionMode(Style.SelectionMode.MULTI);
        columnsConfigs.add(selectColumn.getColumn());
        columnsConfigs.addAll(cm.getColumns());

        this.cm=new ColumnModel<M>(columnsConfigs);

        this.setSelectionModel(selectColumn);

        setEditable();
    }

    /**
     * Needs to be used to get the Grid Editable. It configures how the columns will be edited.
     *
     * @param columnConfig
     * Grid's Column Configuration which will be associated to a type of editable field
     * @param field
     * Editable field which will be used to edit the grid's Column
     */
    public <N> void addColumnEditorConfig(ColumnConfig<M, N> columnConfig, Field<N> field){
        editing.addEditor(columnConfig,field);
    }

    public <N,O> void addColumnEditorConfig(ColumnConfig<M, N> columnConfig, Converter<N, O> converter, Field<O> field) {
        editing.addEditor(columnConfig,converter,field);
    }

    @Override
    public Widget asWidget() {
        return pane.asWidget();
    }
}
