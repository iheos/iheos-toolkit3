package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.simSupport.transaction.ValidatorDefLoader;
import gov.nist.hit.ds.valSupport.engine.MessageValidatorEngine;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SimChainLoader {
	String chainDefResource;

	public SimChainLoader(String chainDefResource) {
		this.chainDefResource = chainDefResource;
	}

	public SimChain load() throws IOException,
	ClassNotFoundException, NoSuchMethodException,
	InstantiationException, IllegalAccessException,
	InvocationTargetException, SimEngineSubscriptionException {
		List<SimStep> simSteps = new ArrayList<SimStep>();		
		
		ValidatorDefLoader vloader = new ValidatorDefLoader(chainDefResource);
		Properties data = vloader.getProperties();
		SimComponentParser parser = new SimComponentParser(data);
		for (Properties parmMap : parser.getComponentProperties()) {
			SimComponentLoader loader = new SimComponentLoader(parmMap.getProperty("class"), parmMap);
			SimComponent component = loader.load();

			SimStep step = new SimStep().
					setName(component.getName()).
					setSimComponent(component);
			simSteps.add(step);

		}

		return new SimChain().setSteps(simSteps);
	}
}