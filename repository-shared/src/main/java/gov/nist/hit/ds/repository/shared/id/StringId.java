package gov.nist.hit.ds.repository.shared.id;

/**
 * Created by skb1 on 9/16/14.
 */
public abstract class StringId {
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
