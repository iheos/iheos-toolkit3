package gov.nist.hit.ds.repository.rpc.search.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.repository.shared.SearchCriteria;

import java.io.Serializable;

public class QueryParameters implements IsSerializable, Serializable {

	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 841897853812501709L;

	private String name;
	private SearchCriteria searchCriteria;
	private String[][] selectedRepos;
	private Boolean advancedMode;
	

	public QueryParameters() {		
	}
	
	public QueryParameters(String[][] selectedRepos, SearchCriteria sc) {
		setSelectedRepos(selectedRepos);
		setSearchCriteria(sc);
	}
	

	public String[][] getSelectedRepos() {
		return selectedRepos;
	}

	public void setSelectedRepos(String[][] selectedRepos) {
		this.selectedRepos = selectedRepos;
	}

	public SearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public Boolean getAdvancedMode() {
		return advancedMode;
	}

	public void setAdvancedMode(Boolean advancedMode) {
		this.advancedMode = advancedMode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
