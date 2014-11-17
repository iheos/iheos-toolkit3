package gov.nist.hit.ds.simServlet

import spock.lang.Specification

/**
 * Created by bmajur on 11/9/14.
 */
class Play extends Specification {
    def x = '''
<Bundle>
<DocumentReference>
    <contained>
        <Patient id="patient">
          <identifier>
            <system value="urn:oid:1.3.6.1.4.1.21367.2005.3.7"/>
            <value value="1234"/>
          </identifier>
        </Patient>
    </contained>

    <subject>
        <reference value="#patient"/>
    </subject>
</DocumentReference>
</Bundle>
'''

    def dr

    def setup() {
        def bundle = new XmlSlurper().parseText(x)
        dr = bundle.DocumentReference
    }

    def 'Patient scanable'() {
        when: ''''''
        def patient = dr.contained.Patient

        then:
        patient.size() == 1
        patient.identifier.system.@value.text().startsWith('urn:oid')
        patient.identifier.value.@value.text() != ''
    }

}
