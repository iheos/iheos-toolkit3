package gov.nist.hit.ds.simSupport.simulator

import gov.nist.hit.ds.toolkit.installation.Installation

/**
 * Created by bmajur on 9/24/14.
 */
class SimSystemConfig {
    static String service = 'xdstools3' // set once - in IT tests and it is good for the duration of the test

    String getHost() { Installation.installation().propertyServiceManager().getToolkitHost()}
    String getPort() { Installation.installation().propertyServiceManager().getToolkitPort()}
    String getTlsPort() { Installation.installation().propertyServiceManager().getToolkitTlsPort()}

    String toString() { "SimSystemConfig: host=${host} port=${port} tlsPort=${tlsPort} service=${service}"}
}
