package gov.nist.hit.ds.simSupport.endpoint

/**
 * Created by bmajur on 6/6/14.
 */
class EndpointValue {
    String value

    EndpointValue(String value) { this.value = value }
    String getValue() { return value }

    String toString() { value }
}
