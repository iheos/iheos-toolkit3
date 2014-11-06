package gov.nist.hit.ds.simSupport.factory;

import gov.nist.hit.ds.actorTransaction.*;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.configElementTypes.BooleanSimConfigElement;
import gov.nist.hit.ds.simSupport.client.configElementTypes.TransactionSimConfigElement;
import gov.nist.hit.ds.simSupport.client.configElementTypes.TextSimConfigElement;
import gov.nist.hit.ds.simSupport.client.configElementTypes.TimeSimConfigElement;
import gov.nist.hit.ds.simSupport.client.configElementTypes.TransactionSimConfigElement;
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue;
import gov.nist.hit.ds.simSupport.simrepo.SimDb;
import gov.nist.hit.ds.toolkit.installation.Installation;

import java.util.Date;

/**
 * Tools for building a collection of commonly used ConfigElements for
 * Simulators. Holds configuration while other agents setAssertionGroup content.
 * @author bmajur
 *
 */

public class GenericActorSimBuilder {
	ActorSimConfig sConfig;   // elements being constructed
	SimId simId;  // needed to build endpoints
	
	public GenericActorSimBuilder(SimId simId) {
		this.simId = simId;
	}
	
	/**
	 * Build a new ActorSim of type actorType.  The SimId belongs to the Simulator
	 * that this ActorSim will be part of.
	 * @param newId
	 * @param actorType
	 * @return
	 * @throws ToolkitRuntimeException 
	 */
	public GenericActorSimBuilder buildGenericConfiguration(ActorType actorType)  {
		sConfig = new ActorSimConfig(actorType);
		configureBaseElements();
		return this;
	}	

	void configureBaseElements() {
		sConfig.add(
				new TimeSimConfigElement(ATConfigLabels.creationTime, new Date().toString())
				);
		sConfig.add(
				new TextSimConfigElement(ATConfigLabels.name, simId.getId()).setEditable(true)
				);
	}
	
	// TODO: this should verify correctness of config - sConfig created, simid set
	public ActorSimConfig getActorSimConfig() {
		return sConfig;
	}

	public GenericActorSimBuilder addConfig(String confName, boolean value) {
		sConfig.add(
				new BooleanSimConfigElement(confName, value).setEditable(true));
		return this;
	}

	public GenericActorSimBuilder addConfig(String confName, String value) {
		sConfig.add(
				new TextSimConfigElement(confName, value).setEditable(true));
		return this;
	}
	
}
