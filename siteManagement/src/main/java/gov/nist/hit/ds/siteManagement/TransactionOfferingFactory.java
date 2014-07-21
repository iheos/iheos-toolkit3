package gov.nist.hit.ds.siteManagement;

import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.actorTransaction.TransactionTypeFactory;
import gov.nist.hit.ds.siteManagement.client.Site;
import gov.nist.hit.ds.siteManagement.client.TransactionBean;
import gov.nist.hit.ds.siteManagement.client.TransactionOfferings;

import java.util.ArrayList;
import java.util.List;

import static gov.nist.hit.ds.actorTransaction.TransactionTypeFactory.*;

/**
 * For a given collection of Sites, build a transaction offering map. 
 * This is used to figure out what sites offer a particular transaction.
 * @author bill
 *
 */
public class TransactionOfferingFactory {
	Sites sites;
	TransactionOfferings to;
	
	
	public TransactionOfferingFactory(Sites sites) {
		this.sites = sites;
		this.to = new TransactionOfferings();
		build();
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("Regular\n");
		for (TransactionType tt : to.map.keySet()) {
			if (tt == null) { 
				continue;
			}
			buf.append(tt.getShortName()).append(": ");
			List<Site> ss = to.map.get(tt);
			for (Site s : ss) {
				buf.append(" ").append(s.getName());
			}
			buf.append("\n");
		}
		
		buf.append("Secure\n");
		for (TransactionType tt : to.tmap.keySet()) {
			if (tt == null) {
				continue;
			}
			buf.append(tt.getShortName()).append(": ");
			List<Site> ss = to.tmap.get(tt);
			for (Site s : ss) {
				buf.append(" ").append(s.getName());
			}
			buf.append("\n");
		}
		
		return buf.toString();
	}
	
	void build() {
				
		for (Site s : sites.getAllSites().asCollection()) {
			if (s.isAllRepositories())
				continue;
			
			for (TransactionBean tb : s.transactions().transactions) {
				if (!tb.hasEndpoint())
					continue;
				TransactionType tt = tb.getTransactionType();
				if (tb.isSecure) {
					List<Site> ss = to.tmap.get(tt);
					if (ss == null) {
						ss = new ArrayList<Site>();
						to.tmap.put(tt, ss);
					}
					if (!ss.contains(s))
					ss.add(s);
				} else {
					List<Site> ss = to.map.get(tt);
					if (ss == null) {
						ss = new ArrayList<Site>();
						to.map.put(tt, ss);
					}
					if (!ss.contains(s))
					ss.add(s);
				}
			}
			

			for (TransactionBean tb : s.repositories().transactions) {
				if (!tb.hasEndpoint())
					continue;
				TransactionType tt;
				if (tb.isRetrieve()) {
					tt = TransactionTypeFactory.find("retrieve");
				} else 
					tt = tb.getTransactionType();
				
				if (tb.isSecure) {
					List<Site> ss = to.tmap.get(tt);
					if (ss == null) {
						ss = new ArrayList<Site>();
						to.tmap.put(tt, ss);
					}
					if (!ss.contains(s))
					ss.add(s);
				} else {
					List<Site> ss = to.map.get(tt);
					if (ss == null) {
						ss = new ArrayList<Site>();
						to.map.put(tt, ss);
					}
					if (!ss.contains(s))
					ss.add(s);
				}
			}
        }
		to.map.get("a");
		to.tmap.get("a");
		int a = 1;
	}
	
	public TransactionOfferings get() {
		return to;
	}

}
