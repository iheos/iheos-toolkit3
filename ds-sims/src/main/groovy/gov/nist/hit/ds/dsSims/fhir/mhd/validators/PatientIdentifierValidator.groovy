package gov.nist.hit.ds.dsSims.fhir.mhd.validators

import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.OidValidator
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

/**
 * Created by bmajur on 11/17/14.
 */
class PatientIdentifierValidator extends ValComponentBase {
    def identifier
    def simHandle

    PatientIdentifierValidator(SimHandle _simHandle, _identifier) {
        super(_simHandle.event)
        simHandle = _simHandle
        identifier = _identifier
    }

    @Validation(id="fhirpatient010", msg='Root is identifier', ref='')
    def fhirpatient010() { assertEquals('identifier', identifier.name())}

    @Validation(id='fhirpatient020', msg='Patient identifier has system', ref='')
    def fhirpatient020() { assertHasValue(identifier.system.@value.text())}

    @Validation(id='fhirpatient030', msg='Patient identifier system is oid', ref='')
    def fhirpatient030() {
        def text = identifier.system.@value.text()
        assertStartsWith(text, 'urn:oid:')
        if (text.startsWith('urn:oid:'))
            new OidValidator(simHandle, text.substring(8)).asSelf().run()
    }

    @Validation(id='fhirpatient040', msg='Patient identifier has value', ref='')
    def fhirpatient040() { assertHasValue(identifier.value.@value.text())}
}
