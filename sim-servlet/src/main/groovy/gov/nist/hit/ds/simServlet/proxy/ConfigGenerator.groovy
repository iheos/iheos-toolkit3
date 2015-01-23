package gov.nist.hit.ds.simServlet.proxy

import groovy.xml.StreamingMarkupBuilder

/**
 * Parse actors directory and generate proxy config file
 * Created by bmajur on 1/20/15.
 */


class ConfigGenerator {
    def forwardingRulePrototype = '''
    <forwarding-rule name="green-to-red_host" port="10000" blockRequest="false"
      blockResponse="false" inboundTLS="false" outboundTLS="false" host="green" method="*">
      <targetport>9080</targetport>
      <targethost>10.242.43.11</targethost>
      <interceptors>
        <interceptor id="urlRewriter" name="Simple URL Rewriter"/>
      </interceptors>
    </forwarding-rule>
'''

    def footer = '''
  <global>
    <adjustHostHeader>false</adjustHostHeader>
  </global>
  <gui>
    <autotrackexchange>true</autotrackexchange>
    <indentMessage>true</indentMessage>
  </gui>
  <proxy active="false" authentication="false">
    <proxy-host/>
    <proxy-port>0</proxy-port>
    <proxy-username/>
    <proxy-password/>
  </proxy>
  '''
    def open = '<configuration><rules>'
    def close = '</rules></configuration>'

    def actorsDir = new File('/Users/bmajur/tmp/toolkit2/actors')

    // site name => site config file
    def siteFileMap(actorsDir) {
        actorsDir.listFiles().findAll { siteFile ->
            siteFile.name.endsWith('xml')
        }.collectEntries { siteFile ->
            def siteName = baseName(siteFile)
            [(siteName): siteFile]
        }
    }

    def baseName(File file) { file.name.lastIndexOf('.').with {it != -1 ? file.name[0..<it] : file.name} }

    def pnrEndpoint(siteXml) {  // file, inputstream, URL, Reader
        def site = new XmlSlurper().parse(siteXml)
        site.transaction.find { it.@name=='pr.b' && it.@secure=='0'}?.text()
    }


    // proxyPropertiesFile - systemname.pnr -> proxyport
    // actorsDir
    // return xml string of proxy config
    String run(Properties proxyProperties, File actorsDir) {
        def siteFileMap = siteFileMap(actorsDir)  // sitename => config file
        def forwardingRules = genForwardingRules(proxyProperties, siteFileMap)
        def config = []
        config << open
        config << forwardingRules
        config << footer
        config << close
        config.flatten().join()
    }

    def propertiesFromFile(File input) {
        Properties properties = new Properties()
        properties.load(new FileReader(input))
        properties
    }

    // proxyMap is siteName => proxyPort
    def genForwardingRules(proxyMap, siteFileMap) {
        def rules = []
        siteFileMap.each { siteName, siteFile ->
            def pnrEndpoint = pnrEndpoint(siteFile)
            def siteReference = "${siteName}.pnr.notls"
            println "siteReference is ${siteReference}"
            def proxyPort = proxyMap[siteReference]
            if (proxyPort)
                rules << genForwardingRule(proxyPort, pnrEndpoint)
        }
        rules
    }

    def genForwardingRule(proxyPort, endpoint) {
        def (host, port) = hostAndPort(endpoint)
        def rule = new XmlSlurper().parse(new StringReader(forwardingRulePrototype))
        rule.@host = host
        rule.@name = host
        rule.@port = proxyPort
        rule.targetport[0] = port
        rule.targethost[0] = host
        def outputBuilder = new StreamingMarkupBuilder()
        String result = outputBuilder.bind{ mkp.yield rule }
        return result
    }

    def hostAndPort(url) {
        def parts = url.split('/')
        def hp = parts[2]
        hp.split(':')
    }

    static void main(String[] args) {
        if (args.length != 3) {
            println "Usage: ConfigGenerator port-assignments(properties) actors-dir output-file"
            System.exit(-1)
        }
        File portAssignmentsFile = new File(args[0])
        File actorsDir = new File(args[1])
        File outputFile = new File(args[2])

        if (!portAssignmentsFile.exists()) {
            println "File ${portAssignmentsFile} does not exist"
            System.exit(-1)
        }
        if (!actorsDir.isDirectory()) {
            println "${actorsDir} is not a directory or does not exist"
            System.exit(-1)
        }

        Properties portAssignments = new Properties()
        portAssignments.load(new FileReader(portAssignmentsFile))

        String proxyConfig = new ConfigGenerator().run(portAssignments, actorsDir)
        def writer = new FileWriter(outputFile)
        writer.write(proxyConfig)
        writer.close()
    }

}
