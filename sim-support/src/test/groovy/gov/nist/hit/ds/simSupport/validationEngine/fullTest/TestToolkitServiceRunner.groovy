package gov.nist.hit.ds.simSupport.validationEngine.fullTest

import gov.nist.hit.ds.simSupport.service.ServiceClass

/**
 * Created by bmajur on 7/1/14.
 */

class TestToolkitServiceRunner {

    def run(serviceName, paramMap) {
        Class<ServiceClass> serviceClass = TestServiceDefinitions.Services[serviceName]
        def object = serviceClass.newInstance()
        object.invokeMethod('run', paramMap)
    }
}



