package gov.nist.hit.ds.dsSims.eb.generator

/**
 * Created by bmajur on 7/14/14.
 */
class PatternGenerator {
    def rimGenerator = new RimGenerator()
    def buffer = []

    def documentEntry(id) {
        def de = protoDocumentEntry()
        de['id'] = id
        return de
    }

    def atts(spec) { spec['attributes'] }

    def setHeader(spec, name, value) {
        spec[name] = value
        return spec
    }

    def addAtt(spec, att) {
        atts(spec) << att
        return spec
    }

    def delAtt(spec, name) {
        def attr = spec.attributes
        def attr1 = attr.grep { it['name'] != name }
        spec.attributes = attr1
        return spec
    }

    def emptyDocumentEntry(id) {
        [type: 'DocumentEntry',
         id: id, mimeType: 'text/xml',
         attributes: []
        ]
    }

    def emptyAssociation(id, type, source, target) {
        [
                type: 'Association',
                'id': id,
                atype: type,
                'source': source,
                'target': target,
                attributes: []
        ]
    }

    def emptySubmissionSet(id) {
        [
                type: 'SubmissionSet',
                'id': id,
                attributes: []
        ]
    }

    def protoDocumentEntry() {
        def codeFactory = new CodeFactory(this.class.getClassLoader().getResourceAsStream('codes.xml').text)
        def classCode = codeFactory.randomCode(CodeFactory.classCode)
        def confCode = codeFactory.randomCode(CodeFactory.confCode)
        def eventCode = codeFactory.randomCode(CodeFactory.eventCode)
        def formatCode = codeFactory.randomCode(CodeFactory.formatCode)
        def hcftCode = codeFactory.randomCode(CodeFactory.hcftCode)
        def psCode = codeFactory.randomCode(CodeFactory.psCode)
        def typeCode = codeFactory.randomCode(CodeFactory.typeCode)

        def spec =

    [type: 'DocumentEntry',
         id: 'DocumentEntry1', mimeType: 'text/xml',
    attributes:
        [
            // Slots
            [name: 'creationTime', values: ['200412251159']],
            [name: 'hash', values: ['da39a3ee5e6b4b0d3255bfef95601890afd80709']],
            [name: 'languageCode', values: ['en-us']],
            [name: 'legalAuthenticator', values: ['^Welby^Marcus^^^Dr^MD']],
            [name: 'repositoryUniqueId', values: ['1.42']],
            [name: 'size', values: ['2']],
            [name: 'sourcePatientId', values: ['BR14']],
            [name: 'sourcePatientInfo', values:
                    [
                        'PID-3|DTP-1^^^&1.3.6.1.4.1.21367.2005.3.7&ISO',
                        'PID-5|DICTAPHONE^ONE^^^',
                        'PID-7|19650120',
                        'PID-8|M',
                        'PID-11|100 Main St^^BURLINGTON^MA^01803^USA'
                    ]
            ],
            [name: 'URI', values: ['http://example.com']],

            // displayName and description
            [name: 'name', values: ['Summary']],
            [name: 'description', values: ['My summary']],

            // Classifications
            [name: CodeFactory.classCode, code: classCode.code, system: classCode.scheme, display: classCode.display],
            [name: CodeFactory.confCode, code: confCode.code, system: confCode.scheme, display: confCode.display],
            [name: CodeFactory.eventCode, code: eventCode.code, system: eventCode.scheme, display: eventCode.display],
            [name: CodeFactory.formatCode, code: formatCode.code, system: formatCode.scheme, display: formatCode.display],
            [name: CodeFactory.hcftCode, code: hcftCode.code, system: hcftCode.scheme, display: hcftCode.display],
            [name: CodeFactory.psCode, code: psCode.code, system: psCode.scheme, display: psCode.display],
            [name: CodeFactory.typeCode, code: typeCode.code, system: typeCode.scheme, display: typeCode.display],

            // Authors

            // ExternalIdentifiers
            [name: 'de.patientId', value: '76cc765a442f410^^^&1.3.6.1.4.1.21367.2005.3.7&ISO'],
            [name: 'de.uniqueId', value: '1,3,4,23234,1'],
        ]
    ]

    return spec
    }


}
