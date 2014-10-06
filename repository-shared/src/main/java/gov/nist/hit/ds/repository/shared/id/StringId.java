package gov.nist.hit.ds.repository.shared.id;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Created by skb1 on 9/16/14.
 */
public abstract class StringId implements IsSerializable, Serializable {
    private static final long serialVersionUID = -46123676112710300L;

    public String id;

    public StringId() {}

    public StringId(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getId();
    }
}
