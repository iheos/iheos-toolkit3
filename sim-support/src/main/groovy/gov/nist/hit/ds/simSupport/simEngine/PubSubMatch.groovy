package gov.nist.hit.ds.simSupport.simEngine;

import java.lang.reflect.Method;

/**
 * This is a data object that holds the result of a Publisher/Subscriber match. What the
 * SimEngine does can be thought of as matching producers to consumers or publishers 
 * to subscribers.  
 * @author bmajur
 *
 */
public class PubSubMatch {
	Method pubMethod;
	Object pubObject;
	Method subMethod;
	Object subObject;
	
	public String toString() {
		Class[] ptypes = subMethod.getParameterTypes();
		String ptype = (ptypes != null && ptypes.length > 0) ? ptypes[0].simpleName : "??";
		return new StringBuffer()
				.append("..takes ")
				.append("<")
//				.append(subMethod.displayName)
				.append(ptype)
				.append("> ")
				.append("from <")
				.append(pubObject.class.name)
				.append("#")
				.append(pubMethod.name)
				.append(">")
				.toString();
	}
}
