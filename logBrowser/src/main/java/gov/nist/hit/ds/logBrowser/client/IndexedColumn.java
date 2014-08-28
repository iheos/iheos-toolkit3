package gov.nist.hit.ds.logBrowser.client;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;

import java.util.List;

/**
 * Created by skb1 on 7/28/14.
 */

public class IndexedColumn extends Column<List<SafeHtml>, SafeHtml> {
    private final int index;
    public IndexedColumn(int index) {
        // For use with String:
        // super(new TextCell());
        super(new SafeHtmlCell());
        this.index = index;
    }
    @Override
    public SafeHtml getValue(List<SafeHtml> object) {
        return object.get(this.index);
    }
}