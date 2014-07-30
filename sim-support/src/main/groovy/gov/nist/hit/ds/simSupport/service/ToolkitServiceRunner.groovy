package gov.nist.hit.ds.simSupport.service

/**
 * Created by bmajur on 7/1/14.
 */

class ToolkitServiceRunner {

    def run(serviceName, paramMap) {
        Class<IServiceClass> serviceClass = ServiceDefinitions.Services[serviceName]
        def object = serviceClass.newInstance()
        object.invokeMethod('run', paramMap)
    }
}



