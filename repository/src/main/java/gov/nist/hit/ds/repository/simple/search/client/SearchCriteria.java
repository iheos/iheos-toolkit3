package gov.nist.hit.ds.repository.simple.search.client;


import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

/*
	 All Search criteria constructions are based on bottom-up approach.
	  The idea is to build parts and assemble them together.
	  Example: 
		 build sub criteria A
		 build sub criteria B
		 build final criteria based on A and B	 
 */

public class SearchCriteria implements IsSerializable, Serializable {
	

	/**
	 * @author Sunil.Bhaskarla
	 */
	private static final long serialVersionUID = 3700511838872506571L;
	private ArrayList<SearchTerm> searchTerms;
	private Criteria criteria;
	private ArrayList<SearchCriteria> searchCriteria;
	private ArrayList<String> properties;
	private boolean deleted;
	private int id;

	public static enum Criteria {
		AND(){
			@Override
			public String toString() {
				return " and ";	
			}
			
		},
		OR(){
			@Override
			public String toString() {
				return " or ";	
			}
			
		};
	}
	
	public SearchCriteria() {}
	
	public SearchCriteria(Criteria c) {		
		this.searchTerms = new ArrayList<SearchTerm>();
		this.searchCriteria = new ArrayList<SearchCriteria>();
		setCriteria(c);
		this.properties = new ArrayList<String>();		
	}

	public void append(SearchTerm st) {		
		// st.setId(this.searchTerms.size());
		this.searchTerms.add(st);
	}

	public void append(SearchCriteria sc) {
		// sc.setId(this.searchCriteria.size());
		this.searchCriteria.add(sc);
	}
	
	@Override
	public String toString() {
		String criteria = "";
		try {
			if (!searchTerms.isEmpty()) {
				int stLen = searchTerms.size();
				for (int cx=0; cx<stLen;cx++) {
					SearchTerm st = searchTerms.get(cx);
					String propName = st.getPropName();
					if (!properties.contains(propName)) {
						properties.add(propName);
					}
					criteria += st.toString() + ((cx<stLen-1)? getCriteria().toString():"");
				}
			
				
			} 
			if (!searchCriteria.isEmpty()) {
				if (!searchTerms.isEmpty()) {
					criteria += searchCriteria.get(0).getCriteria().toString();
				}
				int scLen = searchCriteria.size();
				for (int cx=0; cx<scLen;cx++) {
					criteria += "(" + searchCriteria.get(cx).toString() + ((cx<scLen-1)?") "+ getCriteria().toString() +"  ":")")  ;
				}			
			}
			
		} catch (Exception e) {
			
			System.out.println("Criteria construction error " + e.toString());
			criteria = "";
		}
	
		return criteria ;

		
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	public ArrayList<SearchTerm> getSearchTerms() {
		return searchTerms;
	}


	public ArrayList<SearchCriteria> getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(ArrayList<SearchCriteria> searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public ArrayList<String> getProperties() {
		if (properties.isEmpty()) {
			this.toString();
		}
		return properties;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
}