package gov.nist.hit.ds.simSupport.client;

import gov.nist.hit.ds.simSupport.client.SimulatorConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Holder of a simulator definition.  Simulators are identified by their
 * SimId.  But, since sometimes it is necessary to create combimed simulators, a
 * combined Repository/Registry for example, this class does not have an assigned
 * SimId.  Instead, the individual simulator configurations, represented by instances
 * of the class SimulatorConfig, each have SimIds.
 * @author bill
 *
 */
public class Simulator  implements Serializable, IsSerializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8914156242225793229L;
	List<SimulatorConfig> configs;
	
	public Simulator() {
		configs = new ArrayList<SimulatorConfig>();
	}
	
	public Simulator(List<SimulatorConfig> configs) {
		this.configs = configs;
	}
	
	public Simulator(SimulatorConfig config) {
		configs = new ArrayList<SimulatorConfig>();
		configs.add(config);
	}
	
	public List<SimulatorConfig> getConfigs() {
		return configs;
	}
	
	public int size() { return configs.size(); }
	
	public SimulatorConfig getConfig(int i) { return configs.get(i); }
	
	public List<SimId> getIds() {
		List<SimId> ids = new ArrayList<SimId>();
		for (SimulatorConfig c : configs)
			ids.add(c.id);
		return ids;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		for (SimulatorConfig config : configs) {
			buf.append(config.toString());
		}
		
		return buf.toString();
	}
}
