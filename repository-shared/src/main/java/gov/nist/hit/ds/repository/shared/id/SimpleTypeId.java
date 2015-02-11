package gov.nist.hit.ds.repository.shared.id;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Created by skb1 on 10/2/14.
 */
public class SimpleTypeId extends StringId implements IsSerializable, Serializable {
    private static final long serialVersionUID = -46123676112710303L;

    private StringId id2;

    public SimpleTypeId() {
    }

    public SimpleTypeId(String keyword) {
        super(keyword);
    }

    /**
     *
     * @param keyword
     * @param domain
     */
    public SimpleTypeId(String keyword, String domain) {
        super(keyword);
        setId2(new StringId(domain));
    }


    public StringId getId2() {
        return id2;
    }

    public void setId2(StringId id2) {
        this.id2 = id2;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof SimpleTypeId) {
            SimpleTypeId s = (SimpleTypeId)obj;
            return getId().toString().equals(s.getId().toString()) && getId2().toString().equals(s.getId2().toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (getId()!=null && getId2()==null)
            return getId().toString().hashCode();

        if (getId()==null && getId2()!=null)
            return getId2().toString().hashCode();


        if (getId()!=null && getId2()!=null)
            return (getId().toString() + getId2().toString()).hashCode();


        return 0;
    }
}
