package gov.nist.hit.ds.repository.simple.search.client;


import gov.nist.hit.ds.repository.api.PropertyKey;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;


public class SearchTerm implements IsSerializable, Serializable {
	/**
	 *
	 * @author Sunil.Bhaskarla
	 */
	private static final long serialVersionUID = -6204673800003937099L;
	private String assetType;
    private String propName;
    private Operator operator;
    private String[] values;
    private boolean deleted;
    private int id;
    
   				
	public static enum Operator {
		EQUALTO("equal to") {
			@Override
			public String toString() {
				return " = ";	
			}
		},
		EQUALTOANY("in") {
			@Override
			public String toString() {
				return " in ";	
			}
		},
		NOTEQUALTO("not equal to") {
			@Override
			public String toString() {
				return " != ";	
			}
		},
		NOTEQUALTOANY("not equal to any") {
			@Override
			public String toString() {
				return " not in ";	
			}
		},		
		LESSTHAN("less than") {
			@Override
			public String toString() {
				return " < ";	
			}
		},
		LESSTHANOREQUALTO("less than or equal to") {
			@Override
			public String toString() {
				return " <= ";	
			}
		},
		GREATERTHAN("greater than") {
			@Override
			public String toString() {
				return " > ";	
			}
		},
		GREATERTHANOREQUALTO("greater than or equal to") {
			@Override
			public String toString() {
				return " >= ";	
			}
		},LIKE("like") {
			@Override
			public String toString() {
				return " like ";	
			}
		},UNSPECIFIED("is unspecified") {
			@Override
			public String toString() {
				return " is null ";	
			}
		};
		
    	private String displayName;
		
		private Operator(String displayName) {
			setDisplayName(displayName);
		}
		
		private void setDisplayName(String displayName) {
			this.displayName = displayName;
		}		
		
		public String getDisplayName() {
			return this.displayName;
		}
		
	}
    
    
	public SearchTerm() {}
	
	public SearchTerm(PropertyKey key, Operator op, String[] values) {
		super();
		if (key.isInternalUseOnly()) {
			setPropName(key.toString());
		} else {
			setPropNameQuoted(key.toString());
		}
		this.operator = op;
		this.values = values;

	}
	
	public SearchTerm(PropertyKey key, Operator op, String value) {
		super();
		if (key.isInternalUseOnly()) {
			setPropName(key.toString());
		} else {
			setPropNameQuoted(key.toString());
		}
		this.operator = op;
		this.values = new String[]{value};
	}
	
	public SearchTerm(String propName, Operator op, String[] values) {
		super();
		setPropNameQuoted(propName);
		this.operator = op;
		this.values = values;
	}
	
	public SearchTerm(String propName, Operator op, String value) {
		super();
		setPropNameQuoted(propName);
		this.operator = op;
		this.values = new String[]{value};
	}
		
	
	public String getAssetType() {
		return assetType;
	}
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getPropName() {		
		return propName;
	}
	
	public void setPropNameQuoted(String propName) {
		setPropName(PnIdentifier.getQuotedIdentifer(propName));
	}
	
	public void setPropName(String propName) {		
		this.propName = propName; // Preserve case as the getProperty method is case sensitive
	}
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}



	public Operator getOperator() {
		return operator;
	}



	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	
	@Override
	public String toString() {

		String propName = getPropName();
		
		if (Operator.EQUALTOANY.equals(getOperator())
			|| Operator.NOTEQUALTOANY.equals(getOperator())
			|| Operator.LIKE.equals(getOperator())) {
			return propName + getOperator().toString() + getValueAsCsv();
		} 
		
		if (null == values[0]) {
			return propName + " is null ";
		} else if (Operator.UNSPECIFIED.equals(getOperator())) {
			return propName + getOperator().toString();
		} else {
			return propName + getOperator().toString() + "'" + values[0] + "' "; 
		}		
	}
	
	private String getValueAsCsv() {
		String csv = "";
		int valueLen =  getValues().length;
		
		for (int cx=0; cx<valueLen; cx++) {
			csv += "'" + values[cx] + "'" + ((cx<valueLen-1) ?",":"");
		}
		return "(" + csv + ")";
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