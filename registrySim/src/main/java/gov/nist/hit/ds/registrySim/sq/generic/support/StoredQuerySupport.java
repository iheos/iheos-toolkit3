package gov.nist.hit.ds.registrySim.sq.generic.support;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.registrysupport.logging.LoggerException;

import java.util.ArrayList;

public class StoredQuerySupport {
	public IAssertionGroup er;
	public SqParams params;
	public StringBuffer query;
	public QueryReturnType returnType;
	private QueryReturnType original_query_type;  // storage to allow temporary settings of return_leaf_class
	public boolean has_validation_errors = false;
	public boolean has_alternate_validation_errors = false;
	public boolean is_secure;
	public boolean runEndProcessing = true;
	
	public void noEndProcessing() {
		runEndProcessing = false;
	}

	/**
	 * Constructor
	 * @param response
	 * @param log_message
	 * @throws LoggerException 
	 */
	public StoredQuerySupport(IAssertionGroup response)  {
		this.er = response;
		init();
	}

	/**
	 * Constructor
	 * @param params (SqParams)
	 * @param return_objects (boolean true = LeafClass)
	 * @param response (Response class)
	 * @param log_message (Message)
	 * @param is_secure
	 */
	public StoredQuerySupport(SqParams params, QueryReturnType return_objects, IAssertionGroup response, boolean is_secure) {
		this.er = response;
		this.params = params;
		this.is_secure = is_secure;
		this.returnType = return_objects;
		init();
	}
	
	public boolean isLeafClass() {
		return returnType == QueryReturnType.LEAFCLASS;
	}
	
	public boolean isLeafClassWithDocument() {
		return returnType == QueryReturnType.LEAFCLASSWITHDOCUMENT;
	}
	
	public boolean isObjectRef() {
		return returnType == QueryReturnType.OBJECTREF;
	}
	
	void init() {
		query = new StringBuffer();
		has_validation_errors = false;
	}

	public void forceLeafClassQueryType() {
		original_query_type = returnType;
		returnType = QueryReturnType.LEAFCLASS;
	}

	public void forceObjectRefQueryType() {
		original_query_type = returnType;
		returnType = QueryReturnType.OBJECTREF;
	}

	public void restoreOriginalQueryType() {
		returnType = original_query_type;

	}

	boolean isAlternativePresent(String[] alternatives) {
		if (alternatives == null)
			return false;
		for (String alternative : alternatives) {
			Object value = params.getParm(alternative);
			if (value != null)
				return true;
		}
		return false;
	}

	String valuesAsString(String mainName, String...alternatives) {
		StringBuffer buf = new StringBuffer();
		buf.append("[");

		if (mainName != null)
			buf.append(mainName);

		if (alternatives != null)
			for (int i=0; i<alternatives.length; i++)
				buf.append(" ").append(alternatives[i]);

		buf.append("]");

		return buf.toString();
	}

	// general
	public void validate_parm(String name, boolean required, boolean multiple, boolean is_string, boolean is_code, boolean and_or_ok, String... alternatives) {
		Object value = params.getParm(name);

//		System.out.println("validate_parm: name=" + name + " value=" + value + " required=" + required + " multiple=" + multiple + " is_string=" + is_string + " is_code=" + is_code + " alternatives=" + valuesAsString(null, alternatives));

		if (value == null && alternatives == null) {
			if (required ) {
				er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("Parameter " + name + " is required but not present in query", "ITI TF-2a: 3.18.4.1.2.3.7"), "StoredQuery.java");
				this.has_validation_errors = true;
				return;
			} 
			return;
		}

		if (value == null && alternatives != null) {
			System.out.println("looking for alternatives");
			if (! isAlternativePresent(alternatives)) {
				if ( ! has_alternate_validation_errors) {
					er.err(XdsErrorCode.Code.XDSRegistryError, 
							new ErrorContext("One of these parameters must be present in the query: " + valuesAsString(name, alternatives), 
									"ITI TF-2a: 3.18.4.1.2.3.7"), 
							"StoredQuery.java");
					has_alternate_validation_errors = true;  // keeps from generating multiples of this message
				}
				has_validation_errors = true;
				return;
			}
		}

		if (value == null)
			return;

		if (is_code) {
 			if ( !(value instanceof SQCodedTerm)) {
				er.err(XdsErrorCode.Code.XDSRegistryError, 
						new ErrorContext("Parameter, " + name + ", must be a coded term", 
								"ITI TF-2a: 3.18.4.1.2.3.7"), 
						"StoredQuery.java");
				this.has_validation_errors = true;
				return;
 			}
 			
 			if ( (value instanceof SQCodeAnd) && !and_or_ok) {
				er.err(XdsErrorCode.Code.XDSRegistryError, 
						new ErrorContext("Parameter, " + name + ", is coded with AND/OR semantics which are not allowed on this parameter", 
								"ITI TF-2a: 3.18.4.1.2.3.7"), 
						"StoredQuery.java");
				this.has_validation_errors = true;
				return;
 			}
				
		} else {

			if (multiple && !(value instanceof ArrayList)) {
				er.err(XdsErrorCode.Code.XDSRegistryError, 
						new ErrorContext("Parameter, " + name + ", accepts multiple values but (  ) syntax is missing", 
								"ITI TF-2a: 3.18.4.1.2.3.7"), 
						"StoredQuery.java");
				this.has_validation_errors = true;
				return;
			}
			if (!multiple && (value instanceof ArrayList)) {
				er.err(XdsErrorCode.Code.XDSRegistryError, 
						new ErrorContext("Parameter, " + name + ", accepts single value value only but (  )  syntax is present", 
								"ITI TF-2a: 3.18.4.1.2.3.7"), 
						"StoredQuery.java");
				this.has_validation_errors = true;
				return;
			}
			if (multiple && (value instanceof ArrayList) && ((ArrayList<?>) value).size() == 0) {
				er.err(XdsErrorCode.Code.XDSRegistryError, 
						new ErrorContext("Parameter, " + name + ", (  )  syntax is present but list is empty", 
								"ITI TF-2a: 3.18.4.1.2.3.7"), 
						"StoredQuery.java");
				this.has_validation_errors = true;
				return;
			}

			if ( ! (value instanceof ArrayList) )
				return;

			ArrayList<?> values = (ArrayList<?>) value;

			for (int i=0; i<values.size(); i++) {
				Object a_o = values.get(i);
				if (	is_string && 
						!(a_o instanceof String) && 
						!(     (a_o instanceof ArrayList)   &&   
								((ArrayList<?>)a_o).size() > 0    &&   
								( ((ArrayList<?>)a_o).get(0) instanceof String) 
						)
				) {
					er.err(XdsErrorCode.Code.XDSRegistryError, 
							new ErrorContext("Parameter, " + name + ", is not coded as a string (is type " + a_o.getClass().getName() + ") (single quotes missing?)", 
									"ITI TF-2a: 3.18.4.1.2.3.7"), 
							"StoredQuery.java");
					this.has_validation_errors = true;
				}
				if (!is_string && !(a_o instanceof Integer)) {
					er.err(XdsErrorCode.Code.XDSRegistryError, 
							new ErrorContext("Parameter, " + name + " is not coded as a number (is type " + a_o.getClass().getName() + ") (single quotes present)", 
									"ITI TF-2a: 3.18.4.1.2.3.7"), 
							"StoredQuery.java");
					this.has_validation_errors = true;
				}
			}
		}

	}

	public void validate_parm(String name, boolean required, boolean multiple, boolean is_string, String same_size_as, String... alternatives) {
		Object value = params.getParm(name);

		System.out.println("validate_parm: name=" + name + " value=" + value + " required=" + required + " multiple=" + multiple + " is_string=" + is_string + " same_size_as=" + same_size_as + " alternatives=" + valuesAsString(null, alternatives));

		if (value == null && alternatives == null) {
			if (required ) {
				er.err(XdsErrorCode.Code.XDSRegistryError, 
						new ErrorContext("Parameter " + name + " is required but not present in query", 
								"ITI TF-2a: 3.18.4.1.2.3.7"), 
						"StoredQuery.java");
				this.has_validation_errors = true;
				return;
			} 
			return;
		}

		if (value == null && alternatives != null) {
			System.out.println("looking for alternatives");
			if (! isAlternativePresent(alternatives)) {
				if ( ! has_alternate_validation_errors) {
					er.err(XdsErrorCode.Code.XDSRegistryError, 
							new ErrorContext("One of these parameters must be present in the query: " + valuesAsString(name, alternatives), 
									"ITI TF-2a: 3.18.4.1.2.3.7"), 
							"StoredQuery.java");
					has_alternate_validation_errors = true;  // keeps from generating multiples of this message
				}
				has_validation_errors = true;
				return;
			}
		}

		if (value == null)
			return;

		if (multiple && !(value instanceof ArrayList)) {
			er.err(XdsErrorCode.Code.XDSRegistryError, 
					new ErrorContext("Parameter, " + name + ", accepts multiple values but (  ) syntax is missing", 
							"ITI TF-2a: 3.18.4.1.2.3.7"), 
					"StoredQuery.java");
			this.has_validation_errors = true;
			return;
		}
		if (!multiple && (value instanceof ArrayList)) {
			er.err(XdsErrorCode.Code.XDSRegistryError, 
					new ErrorContext("Parameter, " + name + ", accepts single value value only but (  )  syntax is present", 
							"ITI TF-2a: 3.18.4.1.2.3.7"), 
					"StoredQuery.java");
			this.has_validation_errors = true;
			return;
		}
		if (multiple && (value instanceof ArrayList) && ((ArrayList<?>) value).size() == 0) {
			er.err(XdsErrorCode.Code.XDSRegistryError, 
					new ErrorContext("Parameter, " + name + ", (  )  syntax is present but list is empty", 
							"ITI TF-2a: 3.18.4.1.2.3.7"), 
					"StoredQuery.java");
			this.has_validation_errors = true;
			return;
		}

		if ( ! (value instanceof ArrayList) )
			return;

		ArrayList<?> values = (ArrayList<?>) value;

		for (int i=0; i<values.size(); i++) {
			Object a_o = values.get(i);
			if (	is_string && 
					!(a_o instanceof String) && 
					!(     (a_o instanceof ArrayList)   &&   
							((ArrayList<?>)a_o).size() > 0    &&   
							( ((ArrayList<?>)a_o).get(0) instanceof String) 
					)
			) {
				er.err(XdsErrorCode.Code.XDSRegistryError, 
						new ErrorContext("Parameter, " + name + ", is not coded as a string (is type " + a_o.getClass().getName() + ") (single quotes missing?)", 
								"ITI TF-2a: 3.18.4.1.2.3.7"), 
						"StoredQuery.java");
				this.has_validation_errors = true;
			}
			if (!is_string && !(a_o instanceof Integer)) {
				er.err(XdsErrorCode.Code.XDSRegistryError, 
						new ErrorContext("Parameter, " + name + " is not coded as a number (is type " + a_o.getClass().getName() + ") (single quotes present)", 
								"ITI TF-2a: 3.18.4.1.2.3.7"), 
						"StoredQuery.java");
				this.has_validation_errors = true;
			}
		}

		if (same_size_as == null)
			return;

		Object same_as_value = params.getParm(same_size_as);
		if ( !(same_as_value instanceof ArrayList)) {
			er.err(XdsErrorCode.Code.XDSRegistryError, 
					new ErrorContext("Parameter, " + same_size_as + " must have same number of values as parameter " + name, 
							"ITI TF-2a: 3.18.4.1.2.3.7"), 
					"StoredQuery.java");
			this.has_validation_errors = true;
			return;
		}
		ArrayList<?> same_as_values = (ArrayList<?>) same_as_value;

		if ( !(value instanceof ArrayList)) {
			er.err(XdsErrorCode.Code.XDSRegistryError, 
					new ErrorContext("Parameter, " + same_size_as + " must have same number of values as parameter " + name, 
							"ITI TF-2a: 3.18.4.1.2.3.7"), 
					"StoredQuery.java");
			this.has_validation_errors = true;
			return;
		}

		if (same_as_values.size() != values.size()) {
			er.err(XdsErrorCode.Code.XDSRegistryError, 
					new ErrorContext("Parameter, " + same_size_as + " must have same number of values as parameter " + name, 
							"ITI TF-2a: 3.18.4.1.2.3.7"), 
					"StoredQuery.java");
			this.has_validation_errors = true;
			return;
		}

	}

	public SqParams getParams() {
		return params;
	}

	public void setParams(SqParams params) {
		this.params = params;
	}
	
	static final String[] patientIdParms = {
		"$XDSDocumentEntryPatientId",
		"$XDSSubmissionSetPatientId",
		"$XDSFolderPatientId"
	};
	
	public boolean hasPatientIdParameter() {
		for (String parm : patientIdParms) {
			if (params.hasParm(parm))
				return true;
		}
		return false;
	}


}
