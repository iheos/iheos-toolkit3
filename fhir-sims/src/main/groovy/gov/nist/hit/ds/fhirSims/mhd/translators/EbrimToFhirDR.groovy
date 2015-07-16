package gov.nist.hit.ds.fhirSims.mhd.translators

import Metadata
import MetadataUtilities
import OMElement
import CodeableConcept
import Coding
import DateAndTime
import DateTime
import DocumentReference

/**
 * Created by bmajur on 2/23/15.
 */
class EbrimToFhirDR {
    Metadata metadata
    String id
    OMElement de
    DocumentReference dr
    @Delegate MetadataUtilities MetadataUtilities


    EbrimToFhirDR(Metadata _metadata, String _id) {
        metadata = _metadata
        id = _id
        de = metadata.getExtrinsicObject(id)
        MetadataUtilities = new MetadataUtilities(metadata)
    }

    DocumentReference run() {
        de = metadata.getExtrinsicObject(id)
        if (!de) return null
        dr = new DocumentReference()
        // TODO: author
        // TODO: comments

        // status
        dr.statusSimple = drStatus()

        classCodes(de).each { dr.setClass_(setCode(new CodeableConcept(), it)) }
        confCodes(de).each { setCode(dr.addConfidentiality(), it) }
        dr.setCreated(dtmToDateTime(m.getSlotValues(de, 'creationTime').first()))
        eventCodes(de).each { setCode(dr.addEvent(), it) }

    }

    def drStatus() {
        switch (status(de)) {
            case 'Approved': return DocumentReference.DocumentReferenceStatus.current; break
            case 'Deprecated': return DocumentReference.DocumentReferenceStatus.superceded; break
            default: return DocumentReference.DocumentReferenceStatus.current
        }
    }

    def dtmToDateAndTime(String dtm) {
        def year = ''
        def month = '01'
        def day = '01'
        def hour = '00'
        def minute = '00'
        def second = '00'
        try {
            year = dtm.substring(0, 4)
            month = dtm.substring(4, 6)
            day = dtm.substring(6, 8)
            hour = dtm.substring(8, 10)
            minute = dtm.substring(10, 12)
            second = dtm.substring(12, 14)
        } catch (StringIndexOutOfBoundsException e) {}
        "${year}-${month}-${day}T${hour}:${minute}:${second}"
    }

    DateTime dtmToDateTime(String dtm) {
        DateAndTime dat = new DateAndTime(dtmToDateAndTime(dtm))
        DateTime dt = new DateTime()
        dt.setValue(dat)
    }

    CodeableConcept setCode(CodeableConcept cc, String code) {
        def (value, display, system) = code.split('^')
        Coding t = cc.addCoding()
        t.codeSimple = value
        t.displaySimple = display
        t.systemSimple = system
        cc
    }
}
