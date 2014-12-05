package gov.nist.hit.ds.dsSims.fhir.mhd.validators

import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

/**
 * Created by bmajur on 11/15/14.
 */
class CodingValidator extends ValComponentBase {
    def coding
    String elementName = 'coding'

    CodingValidator(SimHandle simHandle, _coding) {
        super(simHandle.event)
        coding = _coding
    }

    CodingValidator(SimHandle simHandle, _coding, _elementName) {
        super(simHandle.event)
        coding = _coding
        elementName = _elementName
    }

    @Validation(id='fhircode010', msg='Validating code root', ref='')
    def code010() { infoFound("Root should be ${elementName}"); assertEquals(elementName, coding.name()) }

    @Validation(id='fhircode020', msg='Code has value in system.@value', ref='')
    def code020() { assertHasValue(coding.system.@value.text())}

    @Validation(id='fhircode030', msg='Code has value in code.@value', ref='')
    def code030() { assertHasValue(coding.code.@value.text())}

    @Validation(id='fhircode040', msg='Code has value in display.@value', ref='')
    def code040() { assertHasValue(coding.display.@value.text())}
}
