package gov.nist.hit.ds.dsSims.fhir.mhd.validators

import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

/**
 * Created by bmajur on 12/17/14.
 */
class PatientValidator extends ValComponentBase {
    def patient
    def simHandle
    def identifierCount

    PatientValidator(SimHandle _simHandle, _patient) {
        super(_simHandle.event)
        patient = _patient
        simHandle = _simHandle
    }

    @Validation(id='patient010', msg='Patient can have multiple identifiers', ref='')
    def patient010() {
        identifierCount = patient.identifier.size()
        infoFound("${identifierCount} identifiers")
    }

    boolean singleIdentifier() { identifierCount == 1 }
    boolean multipleIdentifiers() { identifierCount > 1 }
    boolean noIdentifiers() { identifierCount == 0 }

    @Guard(methodNames=['noIdentifiers'])
    @Validation(id='patient020', msg='Patient must have at least one identifer', ref='')
    def patient020() {
        fail('No Patient identifiers found.')
    }

    @Guard(methodNames=['singleIdentifier'])
    @Validation(id='patient030', msg='Patient with single identifer must be unlabeled or labeled with use=official', ref='')
    def patient030() {
        def identifier = patient.identifer
        if (identifier.use.size() == 0)
            infoFound('No "use" qualifier')
        else if (identifier.use.@value.text() != 'official')
            fail("Labeled with ${identifier.use.@value.text()} instead of official")
        else
            infoFound("Labeled with official")
    }

    @Guard(methodNames=['multipleIdentifiers'])
    @Validation(id='patient040', msg='Patient with multiple identifers must have one labeled with use=official', ref='')
    def patient040() {
        def identifiers = patient.identifier
        def official = identifiers.findAll { identifier -> identifier.use.@value == 'official'}
        if (official.size() == 1)
            infoFound("1 labeled official")
        else
            fail("Found ${official.size()} labeled official")
    }

    @Validation(id='patient050', msg='Verify patient identifier attributes', ref='')
    def patient050() {
        patient.identifier.each { new PatientIdentifierValidator(simHandle, it).asSelf().run() }
    }
}
