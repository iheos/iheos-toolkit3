package gov.nist.hit.ds.simSupport;

/**
 * All simulators must implement this interface.
 * TODO: This will likely get deleted soon
 * @author bill
 *
 */
public interface GenericSim {
	String getState();  //used only to unit test sim loader
	void run();   // start the simulator
}
