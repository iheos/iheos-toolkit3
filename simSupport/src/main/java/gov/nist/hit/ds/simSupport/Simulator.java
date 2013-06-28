package gov.nist.hit.ds.simSupport;

import gov.nist.hit.ds.simSupport.client.SimulatorConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

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
	
	public List<String> getIds() {
		List<String> ids = new ArrayList<String>();
		for (SimulatorConfig c : configs)
			ids.add(c.getId());
		return ids;
	}
}
