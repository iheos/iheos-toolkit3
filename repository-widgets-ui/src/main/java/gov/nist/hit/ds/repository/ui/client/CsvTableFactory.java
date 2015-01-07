package gov.nist.hit.ds.repository.ui.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.DefaultCellTableBuilder;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import fr.mikrosimage.gwt.client.IndexedColumn;
import fr.mikrosimage.gwt.client.ResizableDataGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skb1 on 7/23/14.
 */
public class CsvTableFactory {
    // TODO: See if this class can be use generic templates in place for references such as: ListDataProvider<List<SafeHtml>>. The issue is that built-in enhancements such as column header resize capabilities will be encapsulated in one place otherwise they need to be taken care of for other ListDataProvider types.

    public static final int PAGE_SIZE = 512;



    public void updateList(int startRow, String [][]csv, List<List<SafeHtml>> rows) {

        int rowLen = csv.length;
        int colLen = csv[0].length;

        for (int r = startRow; r < rowLen; r++) {
//			        List<String> textRow = Arrays.asList(csv[r]);
            if (csv[r]!=null) {
                int textRowSz =  csv[r].length;  //textRow.size();
                if (textRowSz>0) {
                    List<SafeHtml> htmlRow = new ArrayList<SafeHtml>(textRowSz);
                    for (int cx=0; cx<textRowSz; cx++) {
//                        SafeHtmlBuilder shb = new SafeHtmlBuilder();
                        String val =  csv[r][cx];    //textRow.get(cx);
//                                logger.info("val LB: " + val);
                        htmlRow.add(makeSafeHtml(val, false));
                    }
                    rows.add(htmlRow);
                }
            }

        }
    }


    /**
     * Creates a simple cell table
     * @param dataProvider
     * @param csv
     * @return
     */
    public CellTable<List<SafeHtml>> createCellTable(ListDataProvider<List<SafeHtml>> dataProvider , String [][]csv) {
        // Create a CellTable (based on Stack ans. 15122103).
        // CellTable<List<String>> table = new CellTable<List<String>>();
        CellTable<List<SafeHtml>> table = new CellTable<List<SafeHtml>>(PAGE_SIZE);

        table.setSelectionModel(new SingleSelectionModel<List<SafeHtml>>());

        // Get the rows as List
        int rowLen = csv.length;
        int colLen = csv[0].length;
        List<List<SafeHtml>> rows = new ArrayList<List<SafeHtml>>(rowLen);

        updateList(1, csv,rows);

        // Create table columns
        for (int c = 0; c < colLen; c++) {
            table.addColumn(new IndexedColumn(c),
                    new TextHeader(csv[0][c]));
        }


        dataProvider.setList(rows);

        dataProvider.addDataDisplay(table);
        return table;
    }



    /**
     * Creates a normal data grid with fixed column headers
     * @param dataProvider
     * @param csv
     * @return
    */
    public DataGrid<List<SafeHtml>> createDataGrid(ListDataProvider<List<SafeHtml>> dataProvider , String [][]csv) {
        DataGrid<List<SafeHtml>> table = new DataGrid<List<SafeHtml>>(PAGE_SIZE);

        table.setTableBuilder(new DefaultCellTableBuilder<List<SafeHtml>>(table));
        table.setSelectionModel(new SingleSelectionModel<List<SafeHtml>>());

        // Get the rows as List
        int rowLen = csv.length;
        int colLen = csv[0].length;
        List<List<SafeHtml>> rows = new ArrayList<List<SafeHtml>>(rowLen);

        updateList(1, csv,rows);

        // Create table columns
        for (int c = 0; c < colLen; c++) {
            table.addColumn(new IndexedColumn(c),
                    new TextHeader(csv[0][c]));
        }

        dataProvider.setList(rows);

        dataProvider.addDataDisplay(table);

        return table;
    }


    /**
     * Creates a data grid where its column headers can be resized by the UI
     * @param dataProvider
     * @param csv
     * @param columnHeaderWidthsJson
     * @return
     */
    public DataGrid<List<SafeHtml>> createDataGrid(ListDataProvider<List<SafeHtml>> dataProvider, String [][]csv, String columnHeaderWidthsJson) {

        ResizableDataGrid<List<SafeHtml>> table = new ResizableDataGrid<List<SafeHtml>>(PAGE_SIZE);


        table.setTableBuilder(new DefaultCellTableBuilder<List<SafeHtml>>(table));
        table.setSelectionModel(new SingleSelectionModel<List<SafeHtml>>());
        addColumnHeaders(dataProvider, csv, columnHeaderWidthsJson, table);


        return table;
    }

    public void addColumnHeaders(ListDataProvider<List<SafeHtml>> dataProvider, String[][] csv, String columnHeaderWidthsJson, ResizableDataGrid<List<SafeHtml>> table) {
        // Get the rows as List
        int rowLen = csv.length;
        int colLen = csv[0].length;
        List<List<SafeHtml>> rows = new ArrayList<List<SafeHtml>>(rowLen);

        updateList(1, csv,rows);

        JSONObject headerWidths = null;

        if (columnHeaderWidthsJson!=null) {
            headerWidths = getColumnWidths(columnHeaderWidthsJson);
        }

        // Create table columns
        for (int c = 0; c < colLen; c++) {
            table.addColumn(new IndexedColumn(c), table.new DataGridResizableHeader(csv[0][c],new IndexedColumn(c)));

            // Set predefined column header widths
            try {
                if (headerWidths!=null && headerWidths.get(csv[0][c])!=null && headerWidths.get(csv[0][c]).isString()!=null) {
                    table.setColumnWidth(c,headerWidths.get(csv[0][c]).isString().stringValue());
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }


        dataProvider.setList(rows);

        dataProvider.addDataDisplay(table);
    }


    private JSONObject getColumnWidths(String columnHeaderWidthsJson) {
        try {
            if (columnHeaderWidthsJson!=null) {
                JSONValue jsonValue = JSONParser.parseStrict(columnHeaderWidthsJson);

                JSONObject jsonObj = jsonValue.isObject();

                if (jsonObj != null) {
                    return jsonObj;
                }
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }
    /**
     *
     * @param v The text to be SafeHtmlBuilder
     * @param constant Keep text as-is (no URL-link scanning or other enhancements)
     * @return
     --
    public SafeHtmlBuilder makeSafeHtml(String v, boolean constant) {
        SafeHtmlBuilder shb = new SafeHtmlBuilder();
        if (v!=null) {

            if (constant) {
                shb.appendHtmlConstant(v);
            } else if (v.toLowerCase().contains("http://") || v.toLowerCase().startsWith("https://")) {
                String[] values = v.split(" ");
                int cx=0;
                for (String val : values) {
                    cx++;
                    if (val.toLowerCase().startsWith("http://") || v.toLowerCase().startsWith("https://")) {
                        shb.appendHtmlConstant("<a href='"
                                + val
                                + "' target='_blank'>" // Open link in new tab
                        );
                        shb.appendEscaped(val);
                        shb.appendHtmlConstant("</a>&nbsp;");

                    } else {
                        shb.appendEscaped(val);
                        if (cx<values.length)
                            shb.appendHtmlConstant("&nbsp;");
                    }
                }
            } else {
                shb.appendEscaped(v);
            }

        }  else {
            shb.appendEscaped("&nbsp;");
        }
        return  shb;
    }
    *
     *
     */

    public SafeHtml makeSafeHtml(String v, boolean constant) {
        SafeHtmlBuilder shb = new SafeHtmlBuilder();
        if (v!=null) {

            if (constant) {
                shb.appendHtmlConstant(v);
            } else if (v.toLowerCase().contains("http://") || v.toLowerCase().startsWith("https://")) {
                String[] values = v.split(" ");
                int cx=0;
                for (String val : values) {
                    cx++;
                    if (val.toLowerCase().startsWith("http://") || v.toLowerCase().startsWith("https://")) {
                        shb.appendHtmlConstant("<a href='"
                                + val
                                + "' target='_blank'>" // Open link in new tab
                        );
                        shb.appendEscaped(val);
                        shb.appendHtmlConstant("</a>&nbsp;");

                    } else {
                        shb.appendEscaped(val);
                        if (cx<values.length)
                            shb.appendHtmlConstant("&nbsp;");
                    }
                }
            } else {
                shb.appendEscaped(v);
            }

        }  else {
            shb.appendEscaped("&nbsp;");
        }
        return shb.toSafeHtml();
    }


    public static SimplePager getPager() {
        SimplePager pager = new SimplePager();
        pager.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.TOP);


        pager.getElement().getStyle().setPosition(Style.Position.RELATIVE);
        pager.getElement().getStyle().setMarginLeft(42, Style.Unit.PCT);
        pager.getElement().getStyle().setBackgroundColor("lavender");

        return pager;
    }
}
