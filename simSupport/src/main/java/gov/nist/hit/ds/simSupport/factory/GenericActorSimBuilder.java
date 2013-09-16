package gov.nist.hit.ds.simSupport.factory;

import gov.nist.hit.ds.actorTransaction.ATConfigLabels;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.EndpointLabel;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.BooleanActorSimConfigElement;
import gov.nist.hit.ds.simSupport.client.EndpointActorSimConfigElement;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.TextActorSimConfigElement;
import gov.nist.hit.ds.simSupport.client.TimeActorSimConfigElement;
import gov.nist.hit.ds.simSupport.sim.SimDb;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.util.Date;

public class GenericActorSimBuilder {
	ActorSimConfig sConfig;
	SimId simId;
	
	public GenericActorSimBuilder(SimId simId) {
		this.simId = simId;
	}
	
	/**
	 * Build a new ActorSim of type actorType.  The SimId belongs to the Simulator
	 * that this ActorSim will be part of.
	 * @param newId
	 * @param actorType
	 * @return
	 * @throws XdsInternalException 
	 */
	public GenericActorSimBuilder buildGenericConfiguration(ActorType actorType)  {
		sConfig = new ActorSimConfig(actorType).setExpiration(SimDb.getNewExpiration(ActorSimConfig.class));
		configureBaseElements();
		return this;
	}	

	void configureBaseElements() {
		sConfig.add(
				new TimeActorSimConfigElement(ATConfigLabels.creationTime, new Date())
				);
		sConfig.add(
				new TextActorSimConfigElement(ATConfigLabels.name, "Private").setEditable(true)
				);
	}
	
	public ActorSimConfig getActorSimConfig() {
		return sConfig;
	}
	
	public GenericActorSimBuilder addEndpoint(String actorShortName, TransactionType transType, TlsType tls, AsyncType async) {

		String contextName = Installation.installation().tkProps.get("toolkit.servlet.context", "xdstools3");

		String endpoint =  "http"
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
				+ transType.getCode();
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
