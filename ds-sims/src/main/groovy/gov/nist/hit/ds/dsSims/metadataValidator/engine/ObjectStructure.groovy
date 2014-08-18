package gov.nist.hit.ds.dsSims.metadataValidator.engine

import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.dsSims.metadataValidator.RegistryValidationInterface
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase

/**
 * Created by bmajur on 8/4/14.
 */
class ObjectStructure extends ValComponentBase {
    Metadata m
    Event event
    ValidationContext vc
    RegistryValidationInterface rvi;

    public ObjectStructure(Event event, Metadata m, ValidationContext vc, RegistryValidationInterface rvi) {
        super(event)
        this.event = event
        this.m = m
        this.vc = vc
        this.rvi = rvi
    }

    @Override
    void run() {
        if (vc.skipInternalStructure)
            return;
        runValidationEngine()
    }
}
