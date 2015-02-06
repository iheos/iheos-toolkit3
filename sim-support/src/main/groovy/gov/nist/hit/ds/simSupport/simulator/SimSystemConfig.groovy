package gov.nist.hit.ds.simSupport.simulator

import gov.nist.hit.ds.toolkit.installation.Installation

/**
 * Created by bmajur on 9/24/14.
 */
class SimSystemConfig {
//    def host
//    def port
//    def tlsPort
//    def service
//
//    def SimSystemConfig() {
//        host = Installation.installation().propertyServiceManager().getToolkitHost()
//        port = Installation.installation().propertyServiceManager().getToolkitPort()
//        tlsPort = Installation.installation().propertyServiceManager().getToolkitTlsPort()
//        service = 'xdstools3'
//    }

    String getHost() { Installation.installation().propertyServiceManager().getToolkitHost()}
    String getPort() { Installation.installation().propertyServiceManager().getToolkitPort()}
    String getTlsPort() { Installation.installation().propertyServiceManager().getToolkitTlsPort()}
    String getService() { 'xdstools3'}

    String toString() { "SimSystemConfig: host=${host} port=${port} tlsPort=${tlsPort} service=${service}"}
}
