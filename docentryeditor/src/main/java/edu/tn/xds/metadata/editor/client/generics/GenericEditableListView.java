package edu.tn.xds.metadata.editor.client.generics;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onh2 on 6/11/2014.
 */
public abstract class GenericEditableListView<M, N> extends GenericEditableGrid<M> {
    private ColumnConfig<M, N> cc1;

    public GenericEditableListView(/*Class<M> parametrizedClass,*/ String listTitle, ListStore<M> listStore, ValueProvider<? super M, N> valueProvider) {
        super(/*parametrizedClass,*/ listTitle, listStore, new ColumnModel<M>(new ArrayList<ColumnConfig<M, ?>>()));

        List<ColumnConfig<M, ?>> columnsConfigs = new ArrayList<ColumnConfig<M, ?>>();
        cc1 = new ColumnConfig<M, N>(valueProvider, 1000);
        columnsConfigs.add(cc1);

        this.cm = new ColumnModel<M>(columnsConfigs);

        this.setEditable();

        this.setHideHeaders(true);
    }

    public void addEditorConfig(Field<N> field) {
        editing.addEditor(cc1, field);
        field.addHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    // TODO try to override completeEditing on enter key after a validation failure
//                    editing.completeEditing();
                }

            }
        }, KeyDownEvent.getType());
    }

    public <O> void addEditorConfig(Converter<N, O> converter, final Field<O> field) {
        editing.addEditor(cc1, converter, field);
    }

    /**
     * Should not be used, use addEditorConfig(Field<N> field) instead.
     *
     * @param columnConfig Grid's Column Configuration which will be associated to a type of editable field
     * @param field        Editable field which will be used to edit the grid's Column
     */
    @Deprecated
    @Override
    public <N> void addColumnEditorConfig(ColumnConfig<M, N> columnConfig, final Field<N> field) {
        super.addColumnEditorConfig(columnConfig, field);
        field.addHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    field.validate();
                    if (field.isValid()) {
                        editing.completeEditing();
                        editing.completeEditing();
                        editing.completeEditing();
                    }
                }

            }
        }, KeyDownEvent.getType());
    }

    public void setOneClickToEdit() {
        setEditable(ClicksToEdit.ONE);
    }
}
