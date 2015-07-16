package gov.nist.hit.ds.fhirSims.mhd.validators

import OidValidator
import SimHandle
import ValComponentBase
import Guard
import Validation

/**
 * Created by bmajur on 12/7/14.
 */
class MasterIdentifierValidator extends ValComponentBase {
    def dr
    def simHandle

    MasterIdentifierValidator(SimHandle _simHandle, _dr) {
        super(_simHandle.event)
        dr = _dr
        simHandle = _simHandle
    }

    def uniqueIdPresent() { dr.masterIdentifier.size() > 0}

    @Guard(methodNames=['uniqueIdPresent'])
    @Validation(id='mhdmi010', msg='Validating masterIdentifier', ref='')
    def mhdmi010() { infoFound(true)}

    @Guard(methodNames=['uniqueIdPresent'])
    @Validation(id='mhdmi020', msg='MasterIdentifier.system coded', ref='')
    def mhdmi020() { assertHasValue(dr.masterIdentifier.system.@value.text())}

    @Guard(methodNames=['uniqueIdPresent'])
    @Validation(id='mhdmi030', msg='MasterIdentifier.system has correct value', ref='')
    def mhdmi030() { assertEquals('urn:ietf:rfc:3986', dr.masterIdentifier.system.@value.text())}

    @Guard(methodNames=['uniqueIdPresent'])
    @Validation(id='mhdmi040', msg='MasterIdentifier.value is OID', ref='')
    def mhdmi040() {
        def text = dr.masterIdentifier.value.@value.text()
        assertStartsWith(text, 'urn:oid:')
        new OidValidator(simHandle, text.substring(8)).asSelf(this).run()
    }
}
