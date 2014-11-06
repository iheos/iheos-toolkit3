package gov.nist.hit.ds.simSupport.factories

import gov.nist.hit.ds.simSupport.client.ActorSimConfig
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.factory.ActorFactory
import gov.nist.hit.ds.simSupport.factory.GenericActorSimBuilder
import gov.nist.hit.ds.siteManagement.client.Site
/**
 * Created by bmajur on 9/22/14.
 */
class DocumentRegistryActorFactory extends ActorFactory {
    @Override
    Site loadActorSite(ActorSimConfig asc, Site site) {
        return null
    }

    @Override
    void initializeActorSim(GenericActorSimBuilder genericBuilder, SimId simId) {

    }

    @Override
    boolean supportsAsync() {
        return false
    }
}
