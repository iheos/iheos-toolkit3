package gov.nist.hit.ds.repository.shared.id;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Created by skb1 on 9/16/14.
 */
public class RepositoryId extends StringId implements IsSerializable, Serializable {
    private static final long serialVersionUID = -46123676112710302L;


    public RepositoryId() {
    }

    public RepositoryId(String id) {
        super(id);
    }
}
