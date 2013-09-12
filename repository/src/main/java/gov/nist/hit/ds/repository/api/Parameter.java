/**
 * 
 */
package gov.nist.hit.ds.repository.api;

import gov.nist.hit.ds.repository.api.RepositorySource.Access;

/**
 * @author Sunil.Bhaskarla
 *
 */
public class Parameter {

	private String description = "";
	
	public Parameter() {
		super();
	}
	public Parameter(String description) {
		super();
		if (description!=null) {
			this.description = description;
		}
	}

	public void assertNotNull(Object param) throws RepositoryException {
		assertParam(param);
	}
	
	public void assertEquals(Object param, Object value) throws RepositoryException {
		
		assertParam(param);
		
		
		if (param instanceof Access) {
			Access acs = (Access)param;
			Access val = (Access)value;
					
			assertParam(value);
			
			if (! (acs.equals(val))) {
				
				throw new RepositoryException(getDiagnostic(RepositoryException.PERMISSION_DENIED + ": found: " + val.toString() +"; expecting: " + acs.toString()));
			}
			return;
		}
		
			
		if (param!=null && null!=value) {
			if (param instanceof String) {
				String valStr = (String)value;
				if (!((String)param).equals(valStr)) {
					throw new RepositoryException(getDiagnostic(RepositoryException.REPOSITORY_API_REQUIREMENT_FAIL
							+ "Provided value ["+ valStr.toString() +"] does not match requirements."));		
				}
			}
			
			if (value instanceof Boolean) {
				boolean val = ((Boolean)value).booleanValue();
				if (new Boolean(param.toString()).booleanValue()!=val) {
					throw new RepositoryException(getDiagnostic(RepositoryException.REPOSITORY_API_REQUIREMENT_FAIL 
							+ ": "+ RepositoryException.INVALID_ARGUMENT));
				}
			}
		} else {
			throw new NullPointerException(getDiagnostic(RepositoryException.REPOSITORY_API_REQUIREMENT_FAIL
					+ ": A null pointer exception has been caught."));
		}
		
	}

	public void assertParam(Object param) throws RepositoryException {
		if (param==null) {
			throw new RepositoryException(getDiagnostic(RepositoryException.REPOSITORY_API_REQUIREMENT_FAIL
					+  ": Provided param cannot be null."));
		}
		if (param instanceof String) {
			if ("".equals(((String) param))) {
				throw new RepositoryException(getDiagnostic(RepositoryException.REPOSITORY_API_REQUIREMENT_FAIL
						+  ": Provided param cannot be empty."));
			}
		}
	}
	
	public static boolean isNullish(Object param) {
		if (param==null || "".equals(((String) param))) {
			return true;
		}
		return false;
	}
	
	private String getDiagnostic(String msg) {
		if (getDescription()!=null && getDescription().length()>0)
			return msg + ". Parameter description: " + getDescription();
		else 
			return msg;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
}
