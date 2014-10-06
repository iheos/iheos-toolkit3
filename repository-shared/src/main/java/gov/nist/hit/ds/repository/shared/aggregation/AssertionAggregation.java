package gov.nist.hit.ds.repository.shared.aggregation;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.shared.id.AssetId;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * {@inheritDoc}
 * Created by skb1 on 9/16/14.
 */
public class AssertionAggregation extends CSVRowAggregation implements IsSerializable, Serializable {

    private static final long serialVersionUID = -46123676112710401L;

    private Map<AssetId,AssetNode> assetNodeMap = new HashMap<AssetId,AssetNode>();

    public AssertionAggregation() {
    }

    public Map<AssetId, AssetNode> getAssetNodeMap() {
        return assetNodeMap;
    }

    public void setAssetNodeMap(Map<AssetId, AssetNode> assetNodeMap) {
        this.assetNodeMap = assetNodeMap;
    }
}
