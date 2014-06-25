package edu.tn.xds.metadata.editor.client.generics;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onh2 on 6/11/2014.
 */
public class GenericEditableListView<M,N> extends GenericEditableGrid<M> {
    private ColumnConfig<M, N> cc1;

    public GenericEditableListView(Class<M> parametrizedClass,String listTitle,ListStore<M> listStore,ValueProvider<? super M, N> valueProvider){
        super(parametrizedClass,listTitle,listStore,new ColumnModel<M>(new ArrayList<ColumnConfig<M, ?>>()));

        //ColumnConfig(ValueProvider<? super M, N> valueProvider, int width, String header)
        List<ColumnConfig<M, ?>> columnsConfigs = new ArrayList<ColumnConfig<M, ?>>();
        cc1 = new ColumnConfig<M, N>(valueProvider, 1000, "");
        columnsConfigs.add(cc1);
        ColumnModel<M> columnModel = new ColumnModel<M>(columnsConfigs);

        this.cm=columnModel;

        this.setEditable();

        this.setHideHeaders(true);
    }

    public void addEditorConfig(Field<N> field){
        editing.addEditor(cc1,field);
    }

    public <O> void addEditorConfig(Converter<N,O> converter,Field<O> field){
        editing.addEditor(cc1,converter,field);
    }

    /**
     * Should not be used, use addEditorConfig(Field<N> field) instead.
     *
     * @param columnConfig
     *     Grid's Column Configuration which will be associated to a type of editable field
     * @param field
     *     Editable field which will be used to edit the grid's Column
     */
    @Deprecated
    @Override
    public <N> void addColumnEditorConfig(ColumnConfig<M, N> columnConfig, Field<N> field) {
        super.addColumnEditorConfig(columnConfig, field);
    }

}
