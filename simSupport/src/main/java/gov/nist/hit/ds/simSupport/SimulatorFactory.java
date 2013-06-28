package gov.nist.hit.ds.simSupport;

import gov.nist.hit.ds.actorTransaction.ATFactory.TransactionType;
import gov.nist.hit.ds.simSupport.client.SimulatorConfig;

import java.util.List;

/**
 * This class exists only to expose methods in ActorFactory such as
 * the buildNewSimulator method which
 * would otherwise be buried in the abstract class ActorFactory.
 * @author bill
 *
 */
public class SimulatorFactory extends ActorFactory {
	
	@SuppressWarnings("unused")
	private SimulatorFactory() {
	}
	
	public SimulatorFactory(SimManager simManager) {
		super(simManager);
	}

	public Simulator buildNewSimulator(SimManager simm, String simtype) throws Exception {
		return buildNewSimulator(simm, simtype, true);
	}

	
	@Override
	protected Simulator buildNew(SimManager simm, boolean configureBase) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected void verifyActorConfigurationOptions(SimulatorConfig config) {
		// TODO Auto-generated method stub

	}

	@Override
	public Site getActorSite(SimulatorConfig asc, Site site) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionType> getIncomingTransactions() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
