package gov.nist.hit.ds.dsSims.eb.metadata.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class ObjectRefs implements IsSerializable  {
	public List<ObjectRef> objectRefs = new ArrayList<ObjectRef>();
	
	public ObjectRefs() {
	}
	
	public ObjectRefs(AnyIds aids) {
		for (AnyId aid : aids.ids) {
			objectRefs.add(new ObjectRef(aid));
		}
	}

	public ObjectRefs(ObjectRef ref) {
		objectRefs.add(ref);
	}
}
