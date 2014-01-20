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
		EQUALTO() {
			@Override
			public String toString() {
				return " = ";	
			}
		},
		EQUALTOANY() {
			@Override
			public String toString() {
				return " in ";	
			}
		},
		NOTEQUALTO() {
			@Override
			public String toString() {
				return " != ";	
			}
		},
		NOTEQUALTOANY() {
			@Override
			public String toString() {
				return " not in ";	
			}
		},		
		LESSTHAN() {
			@Override
			public String toString() {
				return " < ";	
			}
		},
		LESSTHANOREQUALTO() {
			@Override
			public String toString() {
				return " <= ";	
			}
		},
		GREATERTHAN() {
			@Override
			public String toString() {
				return " > ";	
			}
		},
		GREATERTHANOREQUALTO() {
			@Override
			public String toString() {
				return " >= ";	
			}
		},LIKE() {
			@Override
			public String toString() {
				return " like ";	
			}
		};
	}
    
    
	public SearchTerm() {}
	
	public SearchTerm(PropertyKey key, Operator op, String[] values) {
		super();
		if (key.isInternalUseOnly()) {
			setPropName(key.toString());
		} else {
			setPropName(PnIdentifier.getQuotedIdentifer(key.toString()));
		}
		this.operator = op;
		this.values = values;

	}
	
	public SearchTerm(PropertyKey key, Operator op, String value) {
		super();
		if (key.isInternalUseOnly()) {
			setPropName(key.toString());
		} else {
			setPropName(PnIdentifier.getQuotedIdentifer(key.toString()));
		}
		this.operator = op;
		this.values = new String[]{value};
	}
	
	public SearchTerm(String propName, Operator op, String[] values) {
		super();
		setPropName(propName);
		this.operator = op;
		this.values = values;
	}
	
	public SearchTerm(String propName, Operator op, String value) {
		super();
		setPropName(propName);
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
		
		if (Operator.EQUALTOANY.equals(getOperator())) {
			return getPropName() + getOperator().toString() + getValueAsCsv();
		}
		
		String propName = getPropName();
		if (null == values[0]) {
			return propName + "is null ";
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