package gov.nist.hit.ds.repository.shared.data;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.repository.shared.id.AssetId;

import java.io.Serializable;

/**
 * This class represents a CSV row along with any metadata such as the row number.
 * Created by skb1 on 9/16/14.
 */
public class CSVRow implements IsSerializable, Serializable {
    private static final long serialVersionUID = -46123676112710402L;
    private String[] columns;
    private int rowNumber;
    private AssetId assetId;


    public CSVRow() {
    }

    public CSVRow(int columnSize) {
        this.columns = new String[columnSize];
    }

    public String[] getColumns() {
        return columns;
    }

    /**
     *
     * @param name
     * @return -1 if column name is not found.
     */
    public int getColumnIdxByName(String name) {
        if (name!=null && getColumns()!=null) {
            int cx=0;

            for (String col: getColumns()) {
                if (name.equalsIgnoreCase(col)) {
                    return cx;
                }
                cx++;
            }
        }
        return -1; // Not found
    }

    public void setColumnValueByIndex(int idx, String value) {
        columns[idx] = value;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }


    /**
     * Gets the row number of the CSV starting from the header row.
     */
    public int getRowNumber() {
        return rowNumber;
    }

    /**
     * Sets the row number of the CSV starting from the header row.
     * @param rowNumber
     */
    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public AssetId getAssetId() {
        return assetId;
    }

    public void setAssetId(AssetId assetId) {
        this.assetId = assetId;
    }

    public void setAssetId(String assetId1) {
        this.assetId = new AssetId(assetId1);
    }

}
