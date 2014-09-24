package gov.nist.hit.ds.repository.shared.aggregation;

import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.shared.id.AssetId;

import java.util.HashMap;
import java.util.Map;

/**
 * {@inheritDoc}
 * Created by skb1 on 9/16/14.
 */
public class AssertionAggregation extends CSVRowAggregation {

    private Map<AssetId,AssetNode> assetNodeMap = new HashMap<AssetId,AssetNode>();


    public Map<AssetId, AssetNode> getAssetNodeMap() {
        return assetNodeMap;
    }

    public void setAssetNodeMap(Map<AssetId, AssetNode> assetNodeMap) {
        this.assetNodeMap = assetNodeMap;
    }
}
