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

    // There is new code that generates endpoints.  I forget where.
    @Deprecated
	public GenericActorSimBuilder addEndpoint(String actorShortName, TransactionType transType, TlsType tls, AsyncType async) {

		String contextName = "xdstools3"; //Installation.installation().tkProps.label("toolkit.servlet.context", "xdstools3");

		EndpointValue endpoint =  new EndpointValue("http"
				+ ((tls.isTls()) ? "s" : "")
				+ "://" 
				+ Installation.installation().propertyServiceManager().getToolkitHost() 
				+ ":" 
				+ ((tls.isTls()) ? Installation.installation().propertyServiceManager().getToolkitTlsPort() : Installation.installation().propertyServiceManager().getToolkitPort()) 
				+ "/"  
				+ contextName  
				+ "/sim/" 
				+ simId 
				+ "/" +
				actorShortName    
				+ "/" 
				+ transType.getCode());
		sConfig.add(
				new TransactionSimConfigElement(new EndpointType(transType,tls, async), endpoint).setEditable(true)
				);
		return this;
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
