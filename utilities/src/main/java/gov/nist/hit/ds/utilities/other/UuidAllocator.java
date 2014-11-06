package gov.nist.hit.ds.utilities.other;

import com.sun.ebxml.registry.util.UUID;
import com.sun.ebxml.registry.util.UUIDFactory;

public class UuidAllocator {
    static UUIDFactory fact = null;

    static public String allocate() {
        if (fact == null)
            fact = UUIDFactory.getInstance();
        UUID uu = fact.newUUID();
        return "urn:uuid:" + uu;
    }

    static public String allocateOid() {
        if (fact == null)
            fact = UUIDFactory.getInstance();
        UUID uu = fact.newUUID();
        String upart = uu.toString().replaceAll("-", "");
        return "2.25." + upart;
    }

}
