/**
 * 
 */
package gov.nist.hit.ds.repository.api;

import gov.nist.hit.ds.repository.api.RepositorySource.Access;

/**
 * API parameter validation tasks such as a null check or simple object level comparison can be performed using the Parameter class.
 * @author Sunil.Bhaskarla
 *
 */
public class Parameter {

	private String description = "";
	private String diagnostic = "";

    /**
     * This default constructor does not set the parameter description.
     * Use {@link gov.nist.hit.ds.repository.api.Parameter#setDescription(String)} to set the description.
     */
	public Parameter() {
		super();
	}

    /**
     * This constructor sets the description of the parameter, which is used for identification purposes in case an exception occurs.
     * @param description
     */
	public Parameter(String description) {
		super();
		if (description!=null) {
			this.description = description;
		}
	}

    /**
     * Asserts whether the provided parameter is not null, and non-empty if it were a {@code String}.
     * @param param
     * @throws RepositoryException
     */
	public void assertNotNull(Object param) throws RepositoryException {
		assertParam(param);
	}

    /**
     * Asserts whether both arguments are not null, and equal to the other in terms of the value it represents. It only supports types such as: {@code Access}, {@code String}, and {@code Boolean}.
     * @param param
     * @param value
     * @throws RepositoryException
     */
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
			} else if (value instanceof Boolean) {
				boolean val = ((Boolean)value).booleanValue();
				if (new Boolean(param.toString()).booleanValue()!=val) {
					throw new RepositoryException(getDiagnostic(RepositoryException.REPOSITORY_API_REQUIREMENT_FAIL
							+ ": Boolean does not match"));
				}
			} else {
                if (!param.equals(value)) {
                    throw new RepositoryException(getDiagnostic(RepositoryException.REPOSITORY_API_REQUIREMENT_FAIL
                            + ": Parameter does not match the value provided"));
                }
            }
		} else {
			throw new NullPointerException(getDiagnostic(RepositoryException.REPOSITORY_API_REQUIREMENT_FAIL
					+ ": A null pointer exception has been caught."));
		}
		
	}

    /**
     * Asserts whether the provided parameter is not null, and non-empty if it were a {@code String}. Has the same effect as of {@link Parameter#assertNotNull(Object)}.
     * @param param
     * @throws RepositoryException
     */
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

    /**
     *
     * @param param
     * @return Returns true if the object is null or an empty {@code String}.
     */
	public static boolean isNullish(Object param) {
		if (param==null || "".equals(((String) param))) {
			return true;
		}
		return false;
	}
	
	private String getDiagnostic(String msg) {
		if (getDescription()!=null && getDescription().length()>0)
			return msg + " " + getDescription() + " " + getDiagnostic();
		else 
			return msg;
	}

    /**
     * Gets the parameter description.
     * @return
     */
	public String getDescription() {
		return description;
	}

    /**
     * Sets the parameter description.
     * @param description
     */
	public void setDescription(String description) {
		this.description = description;
	}

    /**
     * Gets the diagnostic message that could help in solving the issue.
     * @return
     */
	public String getDiagnostic() {
		return diagnostic;
	}

    /**
     * Sets the diagnostic message that could help in solving the issue.
     * @return
     */
	public void setDiagnostic(String diagnostic) {
		this.diagnostic = diagnostic;
	}

	
	
}
