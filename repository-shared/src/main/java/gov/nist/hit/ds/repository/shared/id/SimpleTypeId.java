package gov.nist.hit.ds.repository.shared.id;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Created by skb1 on 10/2/14.
 */
public class SimpleTypeId extends StringId  implements IsSerializable, Serializable {
    private static final long serialVersionUID = -46123676112710303L;


    public SimpleTypeId() {
    }

    public SimpleTypeId(String id) {
        super(id);
    }
}