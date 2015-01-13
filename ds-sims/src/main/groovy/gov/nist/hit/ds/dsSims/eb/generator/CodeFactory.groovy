package gov.nist.hit.ds.dsSims.eb.generator

import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import groovy.transform.ToString

/**
 * Created by bmajur on 7/14/14.
 */
class CodeFactory {

    @ToString
    class Code {
        def code
        def scheme
        def display
    }

    CodeFactory() { }

    CodeFactory(def xmlText) { loadCodes(xmlText) }

    def loadCodes(def xmlText) {
        def xml = new XmlSlurper().parseText(xmlText)

        xml.CodeType.find {
            it.@name == 'confidentialityCode'
        }.Code.each {
            def code = new Code()
            code.code = it.@code
            code.scheme = it.@codingScheme
            code.display = it.@display
            confCodes << code
        }

        xml.CodeType.find {
            it.@name == 'healthcareFacilityTypeCode'
        }.Code.each {
            def code = new Code()
            code.code = it.@code
            code.scheme = it.@codingScheme
            code.display = it.@display
            hcftCodes << code
        }

        xml.CodeType.find {
            it.@name == 'practiceSettingCode'
        }.Code.each {
            def code = new Code()
            code.code = it.@code
            code.scheme = it.@codingScheme
            code.display = it.@display
            psCodes << code
        }

        xml.CodeType.find {
            it.@name == 'eventCodeList'
        }.Code.each {
            def code = new Code()
            code.code = it.@code
            code.scheme = it.@codingScheme
            code.display = it.@display
            eventCodes << code
        }

        xml.CodeType.find {
            it.@name == 'folderCodeList'
        }.Code.each {
            def code = new Code()
            code.code = it.@code
            code.scheme = it.@codingScheme
            code.display = it.@display
            folderCodes << code
        }

        xml.CodeType.find {
            it.@name == 'typeCode'
        }.Code.each {
            def code = new Code()
            code.code = it.@code
            code.scheme = it.@codingScheme
            code.display = it.@display
            typeCodes << code
        }

        xml.CodeType.find {
            it.@name == 'classCode'
        }.Code.each {
            def code = new Code()
            code.code = it.@code
            code.scheme = it.@codingScheme
            code.display = it.@display
            classCodes << code
        }

        xml.CodeType.find {
            it.@name == 'formatCode'
        }.Code.each {
            def code = new Code()
            code.code = it.@code
            code.scheme = it.@codingScheme
            code.display = it.@display
            formatCodes << code
        }

        xml.CodeType.find {
            it.@name == 'contentTypeCode'
        }.Code.each {
            def code = new Code()
            code.code = it.@code
            code.scheme = it.@codingScheme
            code.display = it.@display
            contentTypeCodes << code
        }

        codes[classCode] = classCodes
        codes[eventCode] = eventCodes
        codes[formatCode] = formatCodes
        codes[hcftCode] = hcftCodes
        codes[psCode] = psCodes
        codes[typeCode] = typeCodes
        codes[folderCode] = contentTypeCodes
        codes[folderCode] = folderCodes
        codes[confCode] = confCodes

    }

    Code randomCode(type) {
        def values = codes[type]
        if (!values) throw new ToolkitRuntimeException("Code type ${type} does not exist.")
        Random rand = new Random()
        def index = rand.nextInt(values.size())
        return values[index]
    }

    static confCode = 'confCodes'
    static hcftCode = 'hcft'
    static psCode = 'practiceSettingCode'
    static eventCode = 'eventCode'
    static folderCode = 'codeList'
    static typeCode = 'typeCode'
    static classCode = 'classCode'
    static formatCode = 'formatCode'
    static contentTypeCode = 'contentTypeCode'

    def codes = [ : ]

    def confCodes = []
    def hcftCodes = []
    def psCodes = []
    def eventCodes = []
    def folderCodes = []
    def typeCodes = []
    def classCodes = []
    def formatCodes = []
    def contentTypeCodes = []

}
