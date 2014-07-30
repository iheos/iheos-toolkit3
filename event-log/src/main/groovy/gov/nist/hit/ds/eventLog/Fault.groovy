package gov.nist.hit.ds.eventLog

import groovy.util.logging.Log4j

/**
 * Created by bmajur on 4/29/14.
 */
@Log4j
class Fault {
    String faultMsg = null
    String faultCode = null
    String faultActor = null
    String faultDetail = null

    def Fault() {}

    def Fault(String msg, String code, String actor, String detail) {
        faultMsg = msg
        faultCode = code
        faultActor = actor
        faultDetail = detail

        log.debug("Fault: ${toString()}")
    }

    String toString() {
        "Actor=${faultActor}\nCode=${faultCode}\nMsg=${faultMsg}\nDetail=${faultDetail}"
    }
}
