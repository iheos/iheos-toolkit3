package gov.nist.hit.ds.registrySim.factory;

import gov.nist.hit.ds.actorTransaction.ATConfigLabels;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.factory.GenericSimulatorBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DocumentRegistryActorFactory {
	public static final String update_metadata_option = "Update_Metadata_Option";

	static final List<TransactionType> incomingTransactions = 
			Arrays.asList(
					TransactionType.REGISTER,
					TransactionType.STORED_QUERY,
					TransactionType.UPDATE
					);

	public Simulator buildNewSimulator(SimId simId, TlsType[] tlsTypes, AsyncType[] asyncTypes) {
		GenericSimulatorBuilder builder = new GenericSimulatorBuilder().buildGenericConfiguration(simId);
		for (int i=0; i<asyncTypes.length; i++) {
			AsyncType async = asyncTypes[i];
			for (int j = 0; j<tlsTypes.length; j++) {
				TlsType tls = tlsTypes[j];
				for (TransactionType transType : incomingTransactions) {
					builder.addEndpoint(ActorType.REGISTRY.getShortName(), 
							transType, 
							tls, 
							async);
				}
			}
		}
		builder.addConfig(update_metadata_option, true);
		builder.addConfig(ATConfigLabels.extraMetadataSupported, true);
		File codesFile = EnvSetting.getEnvSetting(simm.sessionId).getCodesFile();
		builder.addConfig(ATConfigLabels.codesEnvironment, codesFile.toString());

		return new Simulator(builder.get());
	}

}
