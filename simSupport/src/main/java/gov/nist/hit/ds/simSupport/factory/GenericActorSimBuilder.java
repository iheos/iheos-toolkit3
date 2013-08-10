package gov.nist.hit.ds.simSupport.factory;

import gov.nist.hit.ds.actorTransaction.ATConfigLabels;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.EndpointLabel;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.ActorSimConfigElement;
import gov.nist.hit.ds.simSupport.client.ParamType;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.sim.SimDb;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.util.Date;

public class GenericActorSimBuilder {
	ActorSimConfig sConfig;
	ActorType actorType;
	Simulator sim;
	
	public GenericActorSimBuilder(Simulator sim) {
		this.sim = sim;
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
		this.actorType = actorType;
		sConfig = new ActorSimConfig(actorType).setExpiration(SimDb.getNewExpiration(ActorSimConfig.class));
		configureBaseElements();
		sim.add(sConfig);
		return this;
	}	

	void configureBaseElements() {
		sConfig.add(
				new ActorSimConfigElement().
				setName(ATConfigLabels.creationTime).
				setType(ParamType.TIME).
				setValue(new Date().toString())
				);
		sConfig.add(
				new ActorSimConfigElement().
				setName(ATConfigLabels.name).
				setType(ParamType.TEXT).
				setValue("Private").setEditable(true));
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
				+ sim.getId() 
				+ "/" +
				actorShortName    
				+ "/" 
				+ transType.getCode();
		sConfig.add(
				new ActorSimConfigElement().
				setName(new EndpointLabel(transType,tls, async).get()).
				setType(ParamType.ENDPOINT).
				setValue(endpoint).setEditable(true)
				);
		return this;
	}
	
	public GenericActorSimBuilder addConfig(String confName, boolean value) {
		sConfig.add(
				new ActorSimConfigElement().
				setName(confName).
				setValue(value).
				setType(ParamType.BOOLEAN).setEditable(true));
		return this;
	}

	public GenericActorSimBuilder addConfig(String confName, String value) {
		sConfig.add(
				new ActorSimConfigElement().
				setName(confName).
				setValue(value).
				setType(ParamType.TEXT).setEditable(true));
		return this;
	}
	
}
