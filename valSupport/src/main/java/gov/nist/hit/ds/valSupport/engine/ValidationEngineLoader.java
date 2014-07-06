package gov.nist.hit.ds.valSupport.engine;

/**
 * Load a validation procedure.  A validation procedure is a list
 * of validation/simulator classes that are to be executed in the
 * order listed in the procedure. Having a procedure defined in a 
 * text file allows new validation procedures to be installed
 * without rebuilding the toolkit.
 * 
 * A procedure is loaded by reading the config file (which contains
 * a list of classes).  An instance of each class is created and
 * added to the validation engine.  Each of the classes loaded must
 * implement the MessageValidator interface and have a no argument
 * constructor.
 * @author bmajur
 *
 */
public class ValidationEngineLoader {
	String procedureName;
	
	public ValidationEngineLoader(String procedureName) {
		this.procedureName = procedureName;
	}
	
	
}
