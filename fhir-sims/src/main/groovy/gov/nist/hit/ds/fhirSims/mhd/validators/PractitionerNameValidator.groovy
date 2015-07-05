package gov.nist.hit.ds.fhirSims.mhd.validators

import SimHandle
import ValComponentBase
import Validation

/**
 * Created by bmajur on 11/16/14.
 */
class PractitionerNameValidator extends ValComponentBase {
    def coding

    PractitionerNameValidator(SimHandle simHandle, _name) {
        super(simHandle.event)
        coding = _name
    }

    @Validation(id='fhirpract010', msg='Practitioner root is name', ref='')
    def fhirpract010() { assertEquals('name', coding.name()) }

    @Validation(id='fhirpract020', msg='Practitioner has family name', ref='')
    def fhirpract020() {
        assertHasValue(coding.family.@value.text())
    }

    @Validation(id='fhirpract030', msg='Practitioner has given name', ref='')
    def fhirpract030() {
        assertHasValue(coding.given.@value.text())
    }

}
