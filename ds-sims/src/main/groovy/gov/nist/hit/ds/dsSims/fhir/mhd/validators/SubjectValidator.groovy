package gov.nist.hit.ds.dsSims.fhir.mhd.validators

import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation

/**
 * Created by bmajur on 12/7/14.
 */
class SubjectValidator  extends ValComponentBase {
    def dr
    def simHandle

    SubjectValidator(SimHandle _simHandle, _dr) {
        super(_simHandle.event)
        dr = _dr
        simHandle = _simHandle
    }

    def subjectPresent() { dr.subject.size() > 0 }

    def subjectRefValues() {
        dr.subject.collect { it.reference.@value.text() }
    }

    def subjectTags() { subjectRefValues().collect { it.substring(1)}}

    def containedPatients() {
        dr.contained.findAll {
            it.Patient.size() > 0
        }.collect { it.Patient }
    }

    def containedPatients(id) {
        containedPatients().findAll { it.@id.text() == id}
    }

    @Guard(methodNames=['subjectPresent'])
    @Validation(id='mhdsub160', msg='Validating subject', ref='')
    def mhdsub160() { infoFound(true)}

    @Guard(methodNames=['subjectPresent'])
    @Validation(id='mhdsub170', msg='subject is local reference (starts with #)', ref='')
    def mhdsub170() { subjectRefValues().each { assertStartsWith(it, '#') } }

    @Guard(methodNames=['subjectPresent'])
    @Validation(id='mhdsub180', msg='Subject references contained Patient', ref='')
    def mhdsub180() {
        def containedPatients = containedPatients()
        def patientReferences = subjectRefValues()

        patientReferences.each { String ref ->
            def label = ref.substring(1)
            def referencedPatient = containedPatients.find { patient -> patient.@id.text() == label }
            if (referencedPatient)
                infoFound("Found ${referencedPatient.@id.text()}")
            else
                fail("No contained Patient resource found with id ${label}")
        }
    }

    @Guard(methodNames=['subjectPresent'])
    @Validation(id='mhdsub190', msg='Validate contained Patient resources', ref='')
    def mhdsub190() {
        subjectTags().each {
            containedPatients(it).each { patient ->
                new PatientValidator(simHandle, patient).asSelf().run()
            }
        }
    }

}
