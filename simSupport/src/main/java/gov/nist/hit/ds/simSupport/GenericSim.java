package gov.nist.hit.ds.simSupport;

/**
 * All simulators must implement this interface.
 * @author bill
 *
 */
public interface GenericSim {
	String getState();  //used to unit test sim loader
	void run();   // start the simulator
}
