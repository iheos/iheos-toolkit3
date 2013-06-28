package gov.nist.hit.ds.simSupport;

/**
 * This simulator is only used to test the simulator loading logic.
 * @author bill
 *
 */
public class DummySim implements GenericSim {

	@Override
	public String getState() {
		return "running";
	}

}
