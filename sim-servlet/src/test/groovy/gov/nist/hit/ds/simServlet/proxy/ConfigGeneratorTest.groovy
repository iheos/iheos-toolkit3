package gov.nist.hit.ds.simServlet.proxy

import spock.lang.Specification

/**
 * Created by bmajur on 1/20/15.
 */
class ConfigGeneratorTest extends Specification {

    def 'Verify pnr endpoint extraction'() {
        setup:
        def site = '''
<site name="blue">
   <home>urn:oid:1.19.6.24.109.42.1.3</home>
   <transaction name="sq.b" secure="1">https://blue:9085/tf6/services/xdsregistryb</transaction>
   <transaction name="r.b" secure="0">http://blue:9080/tf6/services/xdsregistryb</transaction>
   <transaction name="pr.b" secure="1">https://blue:9085/tf6/services/xdsrepositoryb</transaction>
   <transaction name="pr.b" secure="0">http://blue:9080/tf6/services/xdsrepositoryb</transaction>
</site>'''

        when:
        def endpoint = new ConfigGenerator().pnrEndpoint(new StringReader(site))

        then:
        endpoint == 'http://blue:9080/tf6/services/xdsrepositoryb'
    }

    def 'Basename'() {
        when:
        def file = new File('foo.xml')

        then:
        new ConfigGenerator().baseName(file) == 'foo'
    }

    def 'Parse host and port'() {
        when:
        def url = 'http://blue:9080/tf6/services/xdsrepositoryb'

        then:
        new ConfigGenerator().hostAndPort(url) == [ 'blue', '9080']
    }

    def 'Gen forwarding rule'() {
        when:
        def endpoint = 'http://blue:9080/tf6/services/xdsrepositoryb'
        def host = 'blue'
        def port = '9080'
        def proxyPort = '12000'
        def update = new ConfigGenerator().genForwardingRule(proxyPort, endpoint)
        println update
        def rule = new XmlSlurper().parse(new StringReader(update))

        then:
        rule.@host == host
        rule.@name == host
        rule.@port == proxyPort
        rule.targetport[0] == port
        rule.targethost[0] == host
    }

//    def 'Gen proxy config'() {
//        setup:
//        def generator = new ConfigGenerator()
//        Properties portAssignments = new Properties()
//        def asgn = getClass().classLoader.getResource('proxy/proxy-ports.properties').text
//        portAssignments.load(new StringReader(asgn))
//        File actorsDir = new File(getClass().classLoader.getResource('proxy/actors').toURI())
//
//        when:
//        String proxyConfig = generator.run(portAssignments, actorsDir)
//        println proxyConfig
//        def config = new XmlSlurper().parse(new StringReader(proxyConfig))
//        def rule0 = config.rules[0].'forwarding-rule'[0]
//        def rule1 = config.rules[0].'forwarding-rule'[1]
//        def nist8 = (rule0.@name == 'nist8') ? rule0 : rule1
//        def blue = (rule0.@name == 'blue') ? rule0 : rule1
//
//        then:
//        nist8.@port.text() == '14206'
//        blue.@port.text()  == '29010'
//    }

//    def 'Scan actors dir'() {
//        when:
//        println new ConfigGenerator().siteFileMap(new File('/Users/bmajur/tmp/toolkit2/actors'))
//
//        then:
//        true
//    }


}
