package gov.nist.hit.ds.simSupport.validationEngine.fullTest

import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.simSupport.service.ServiceClass

/**
 * Created by bmajur on 7/1/14.
 */
class MyValidatorService implements ServiceClass {
    void run(params) {
        // grab the necessary parms defined for the service
        String msg = params['msg']

        // Create Event to log the running of this service
        Event event = new EventFactory().buildEvent(eventRepo)

        // pass on the input message to the validator
        event.inOut.reqBody = msg.getBytes ( )

        // run!
        new MyValidator(event, msg).runValidationEngine ( )
    }
}
