package gov.nist.hit.ds.repository.shared.data;

import gov.nist.hit.ds.repository.shared.id.AssetId;

/**
 * This class represents a CSV row along with any metadata such as the row number.
 * Created by skb1 on 9/16/14.
 */
public class CSVRow {
    private String[] columns;
    private int rowNumber;
    private AssetId assetId;


    public CSVRow(int columnSize) {
        this.columns = new String[columnSize];
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumnValueByIndex(int idx, String value) {
        columns[idx] = value;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }


    public int getRowNumber() {
        return rowNumber;
    }

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
