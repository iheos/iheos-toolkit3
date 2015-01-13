package gov.nist.hit.ds.dsSims.fhir.mhd.validators

import gov.nist.hit.ds.dsSims.fhir.mhd.SubmitModel
import gov.nist.hit.ds.dsSims.fhir.schema.FhirSchemaValidator
import gov.nist.hit.ds.dsSims.fhir.utilities.JsonToXmlValidator
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.Transaction
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.toolkit.Toolkit
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException

/**
 * Created by bmajur on 12/7/14.
 */
class FeedTransaction implements Transaction {
    SimHandle simHandle

    FeedTransaction(SimHandle _simHandle) { simHandle = _simHandle }

    @Override
    ValidationStatus validateRequest() {
        assert simHandle
        assert simHandle.event.inOut.reqBody
        String reqString = new String(simHandle.event.inOut.reqBody)
        reqString = reqString.trim()
        if (reqString.startsWith('<')) {
            // XML
            validateFeed(reqString, true)
        } else if (reqString.startsWith('{')) {
            // JSON
            JsonToXmlValidator jxval = new JsonToXmlValidator(simHandle, reqString).asPeer()
            jxval.run()
            String xmlString = jxval.xml()
            simHandle.event.artifacts.add('XML', xmlString)
            validateFeed(xmlString, false)
        } else {
            throw new ToolkitRuntimeException('Parse failed - do not understand format - XML or JSON required')
        }
        return simHandle.event.hasErrors() ? ValidationStatus.ERROR : ValidationStatus.OK
    }

    def validateFeed(String reqString, boolean isXml) {
        // Schema validation
        def schema = new FhirSchemaValidator ( simHandle, reqString, Toolkit.schemaFile ( ), true )
        schema.setName('Schema')
        schema.asPeer ( ).run ( )

        // build model description to support later calls
        def dr = new XmlSlurper().parseText(reqString)
        SubmitModel sm = buildSubmitModel(isXml, dr)

        // Validation model
        def validator = new SubmitModelValidator(simHandle, sm)
        validator.setName("Model Validation")
        validator.asPeer ( ).run ( )

        // validate necessary HTTP headers
        def hdr = new SubmitHeaderValidator ( simHandle, sm )
        hdr.setName('HTTP Headers')
        hdr.asPeer ( ).run ( )

        // validate the structure of the DocumentManifest resource
        if ( sm.docManifests.size ( ) == 1 ) {
            def man = new MhdDocManValidator(simHandle, sm.docManifests[0])
            man.setName('Manifest')
            man.asPeer().run()
        }

        // validate the structure of each DocumentReference resource
        sm.docReferenceMap.entrySet().each {
            def val = new MhdDocRefValidator(simHandle, it.value)
            val.setName("DocReference - ${it.key}")
            val.asPeer().run()
        }
    }


    SubmitModel buildSubmitModel(isXml, bundle) {
        if (bundle instanceof String) bundle = new XmlSlurper().parseText(bundle)
        SubmitModel sm = new SubmitModel()
        sm.isXml = isXml
        sm.bundle = bundle
        bundle.entry.each {
            if (it.content.DocumentManifest.size() > 0) sm.docManifests << it.content.DocumentManifest
            if (it.content.DocumentReference.size() > 0) {
                def id = it.id.text()
                if (id) sm.docReferenceMap[id] = it.content.DocumentReference
            }
            if (it.content.Binary.size() > 0) {
                def id = it.id.text()
                if (id) sm.binaryMap[id] = it.content.Binary
            }
        }
        return sm
    }

    @Override
    ValidationStatus validateResponse() {
        return null
    }

    @Override
    ValidationStatus acceptRequest() {
        return null
    }

    @Override
    ValidationStatus sendRequest() {
        return null
    }

}
