package fr.mikrosimage.gwt.client;

import com.google.gwt.user.cellview.client.DataGrid;

/**
 *
 * skb added comment:
 * Resizable as in the ability to resize the column headers from the UI.
 * @param <T>
 */
public class ResizableDataGrid<T> extends DataGrid<T> {

    /**
     * Constructs a table with the given page size.
     *
     * @param pageSize the page size
     */
    public ResizableDataGrid(int pageSize) {
        super(pageSize);
    }

    public class DataGridResizableHeader extends ResizableHeader<T> {
        public DataGridResizableHeader(String title, IndexedColumn column /* Column<T, ?> column */) {
            super(title, ResizableDataGrid.this, column);
        }


        @Override
        protected int getTableBodyHeight() {
            return ResizableDataGrid.this.getTableBodyElement().getOffsetHeight();
        }
    }
}
