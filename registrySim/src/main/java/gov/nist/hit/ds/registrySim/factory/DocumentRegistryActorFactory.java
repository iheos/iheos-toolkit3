package gov.nist.hit.ds.registrySim.factory;

import gov.nist.hit.ds.actorTransaction.ATConfigLabels;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.initialization.environment.Environment;
import gov.nist.hit.ds.initialization.installation.ExternalCacheManager;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.factory.GenericActorSimBuilder;
import gov.nist.hit.ds.simSupport.factory.SimulatorFactory;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * Build an instance of the Document Registry Actor.
 * @author bmajur
 *
 */
public class DocumentRegistryActorFactory {
	static final String update_metadata_option = "Update_Metadata_Option";

	static final List<TransactionType> incomingTransactions = 
			Arrays.asList(
					TransactionType.REGISTER,
					TransactionType.STORED_QUERY,
					TransactionType.UPDATE
					);

	public Simulator buildNewSimulator(SimId simId, TlsType[] tlsTypes, AsyncType[] asyncTypes) throws IOException, XdsInternalException {
		SimulatorFactory factory = new SimulatorFactory();
		factory.buildSimulator(simId);
		
		GenericActorSimBuilder builder = factory.addActorSim(ActorType.REGISTRY, incomingTransactions, tlsTypes, asyncTypes);
		builder.addConfig(update_metadata_option, true);
		builder.addConfig(ATConfigLabels.extraMetadataSupported, true);
		
		/**
		 * Install codes file for default environment. This is the best we can do this deep in the code
		 * since this package does not depend on the Session package which has information regarding
		 * the current selected environment.  Better selection will have to come from the user
		 * interface.
		 */
		builder.addConfig(ATConfigLabels.codesEnvironment, 
				Installation.installation().getDefaultCodesFile().toString());
		
		factory.save();

		return factory.getSimulator();
	}

}
