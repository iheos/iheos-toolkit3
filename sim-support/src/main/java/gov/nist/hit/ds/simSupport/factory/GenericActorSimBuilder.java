package gov.nist.hit.ds.simSupport.factory;

import gov.nist.hit.ds.actorTransaction.*;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.configElementTypes.BooleanActorSimConfigElement;
import gov.nist.hit.ds.simSupport.client.configElementTypes.EndpointActorSimConfigElement;
import gov.nist.hit.ds.simSupport.client.configElementTypes.TextActorSimConfigElement;
import gov.nist.hit.ds.simSupport.client.configElementTypes.TimeActorSimConfigElement;
import gov.nist.hit.ds.simSupport.endpoint.Endpoint;
import gov.nist.hit.ds.simSupport.simrepo.SimDb;
import gov.nist.hit.ds.toolkit.installation.Installation;

import java.util.Date;

/**
 * Tools for building a collection of commonly used ConfigElements for
 * Simulators. Holds configuration while other agents setAssertionGroup content.
 * @author bmajur
 *
 */
@Deprecated
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
		sConfig = new ActorSimConfig(actorType).
				setExpiration(SimDb.getNewExpiration(ActorSimConfig.class));
		configureBaseElements();
		return this;
	}	

	void configureBaseElements() {
		sConfig.add(
				new TimeActorSimConfigElement(ATConfigLabels.creationTime, new Date().toString())
				);
		sConfig.add(
				new TextActorSimConfigElement(ATConfigLabels.name, simId.getId()).setEditable(true)
				);
	}
	
	// TODO: this should verify correctness of config - sConfig created, simid set
	public ActorSimConfig getActorSimConfig() {
		return sConfig;
	}

    // There is new code that generates endpoints.  I forget where.
    @Deprecated
	public GenericActorSimBuilder addEndpoint(String actorShortName, TransactionType transType, TlsType tls, AsyncType async) {

		String contextName = Installation.installation().tkProps.get("toolkit.servlet.context", "xdstools3");

		Endpoint endpoint =  new Endpoint("http"
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
				new EndpointActorSimConfigElement(new EndpointLabel(transType,tls, async), endpoint).setEditable(true)
				);
		return this;
	}
	
	public GenericActorSimBuilder addConfig(String confName, boolean value) {
		sConfig.add(
				new BooleanActorSimConfigElement(confName, value).setEditable(true));
		return this;
	}

	public GenericActorSimBuilder addConfig(String confName, String value) {
		sConfig.add(
				new TextActorSimConfigElement(confName, value).setEditable(true));
		return this;
	}
	
}
