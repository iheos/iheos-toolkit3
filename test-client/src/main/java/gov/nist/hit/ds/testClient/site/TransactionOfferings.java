package gov.nist.hit.ds.testClient.site;

import gov.nist.hit.ds.actorTransaction.TransactionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is as close as toolkit comes to managing Actor configurations. ATFactory
 * defines the linkage between Transactions and Actors, this holds the 
 * transaction configurations (actual endpoints).
 * @author bill
 *
 */
public class TransactionOfferings  {
	// regular and tls
	public Map<TransactionType, List<gov.nist.hit.ds.testClient.site.Site>> map = new HashMap<TransactionType, List<gov.nist.hit.ds.testClient.site.Site>>();
	public Map<TransactionType, List<gov.nist.hit.ds.testClient.site.Site>> tmap = new HashMap<TransactionType, List<gov.nist.hit.ds.testClient.site.Site>>();
	
	public boolean hasTransaction(TransactionType tt, boolean isTLS) {
		if (isTLS) {
			return tmap.containsKey(tt);
		} else {
			return map.containsKey(tt);
		}
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("TransactionOfferings:\n");
		buf.append("Non-TLS\n");
		for (TransactionType t : map.keySet()) {
			buf.append("\t").append(t).append("\n");
		}
		buf.append("TLS\n");
		for (TransactionType t : tmap.keySet()) {
			buf.append("\t").append(t).append("\n");
		}
		
		return buf.toString();
	}
	
	public TransactionOfferings() {} // For GWT

}
