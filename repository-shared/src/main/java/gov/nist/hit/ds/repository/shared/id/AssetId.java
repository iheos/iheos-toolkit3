package gov.nist.hit.ds.repository.shared.id;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Created by bmajur on 8/28/14.
 */
public class AssetId extends StringId  implements IsSerializable, Serializable {
    private static final long serialVersionUID = -46123676112710301L;

    public AssetId() {
    }

    public AssetId(String id) {
        super(id);
    }
}
