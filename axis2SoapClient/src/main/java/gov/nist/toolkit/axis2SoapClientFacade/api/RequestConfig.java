package gov.nist.toolkit.axis2SoapClientFacade.api;

/**
 * This object holds request configuration parameters in java properties Here
 * are the properties supported so far :
 * 
 * REQUIRED:
 * 
 * - MODE=async/sync : synchronous or async TO='the url endpoint to send to'.
 * If MODE==async, we need to define the endpoint to send the response to:
 * REPLY_TO='the endpoint to send back to' 
 * 
 * - ACTION='any string corresponding to the soap action'
 * 
 * 
 * OPTIONAL :
 * 
 * - REQUEST_SPECIFIC_SSL_CREDENTIAL=true/false : set to true if we need to pass
 * SSL credentials.In this case, the 4 following parameters need to be then added:
 * KEYSTORE_PATH='path to keystore' 
 * KEYSTORE_PASS='pass'
 * TRUSTSTORE_PATH='path to truststore' 
 * TRUSTSTORE_PASS='pass'
 * 
 * - MTOM = When this property is set to True, any SOAP envelope, regardless of
 * whether it contains optimizable content or not, will be serialized as an MTOM
 * optimized MIME message.
 */

public class RequestConfig extends java.util.Properties {

	private static final long serialVersionUID = 5086339236442906272L;

}
