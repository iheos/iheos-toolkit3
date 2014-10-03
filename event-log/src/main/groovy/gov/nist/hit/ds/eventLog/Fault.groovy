package gov.nist.hit.ds.eventLog

import groovy.util.logging.Log4j

/**
 * Created by bmajur on 4/29/14.
 */
@Log4j
class Fault {
    String faultMsg = null
    String faultCode = null
    String faultTransaction = null
    String faultDetail = null

    def Fault() {}

    def Fault(String msg, String code, String transaction, String detail) {
        faultMsg = msg
        faultCode = code
        faultTransaction = transaction
        faultDetail = detail

        log.debug("Fault: ${toString()}")
    }

    String toString() {
        "Transaction=${faultTransaction}\nCode=${faultCode}\nMsg=${faultMsg}\nDetail=${faultDetail}"
    }
}
