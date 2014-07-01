package gov.nist.hit.ds.simSupport.validationEngine.fullTest

/**
 * Created by bmajur on 7/1/14.
 */

// This is different from ServiceDefinitions in that this is parked in
// the test class path and ServiceDefinitions is in the production
// classpath.
class TestServiceDefinitions {
    static Services = [
          // serviceName => ServiceClass (must implement ServiceClass interface)
            'MyValidator' : MyValidatorService.class,
            'ToyService' :  ToyService.class,

    ]
}
