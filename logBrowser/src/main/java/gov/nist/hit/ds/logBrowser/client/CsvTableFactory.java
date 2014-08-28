package gov.nist.hit.ds.logBrowser.client;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.view.client.ListDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skb1 on 7/23/14.
 */
public class CsvTableFactory {

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
                        htmlRow.add(htmlBuilder(val).toSafeHtml());
                    }
                    rows.add(htmlRow);
                }
            }

        }
    }

    public CellTable<List<SafeHtml>> createCellTable(String [][]csv) {
        // Create a CellTable (based on Stack ans. 15122103).
        // CellTable<List<String>> table = new CellTable<List<String>>();
        CellTable<List<SafeHtml>> table = new CellTable<List<SafeHtml>>();

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

        // Create a list data provider.
        final ListDataProvider<List<SafeHtml>> dataProvider  = new ListDataProvider<List<SafeHtml>>();
        dataProvider.setList(rows);

        dataProvider.addDataDisplay(table);
        return table;
    }

    private SafeHtmlBuilder htmlBuilder(String v) {
        SafeHtmlBuilder shb = new SafeHtmlBuilder();
        if (v!=null) {

            if (v.startsWith("$htmlConstant")) {
                shb.appendHtmlConstant(v.substring("$htmlConstant".length()));
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


}
