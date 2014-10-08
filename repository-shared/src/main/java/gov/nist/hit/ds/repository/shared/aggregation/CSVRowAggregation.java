package gov.nist.hit.ds.repository.shared.aggregation;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.repository.shared.data.CSVRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an aggregation of CSV rows. The rows may be selective in that the representation is based on some criteria and may not reflect the actual order as it appears in the source CSV data file.
 * Created by skb1 on 9/16/14.
 */
public abstract class CSVRowAggregation implements IsSerializable, Serializable {
    private static final long serialVersionUID = -46123676112710403L;
    private CSVRow header;
    private List<CSVRow> rows = new ArrayList<CSVRow>();

    public List<CSVRow> getRows() {
        return rows;
    }

    public void setRows(List<CSVRow> rows) {
        this.rows = rows;
    }
    public CSVRow getHeader() {
        return header;
    }

    public void setHeader(CSVRow header) {
        this.header = header;
    }

    protected CSVRowAggregation() {
    }
}
