package gov.nist.hit.ds.axis2SoapClientFacade.api;

/**
 * This object holds request configuration parameters in java properties Here
 * are the properties supported so far :
 * 
 * MODE=async/sync : synchronous or async TO='the url endpoint to send to'
 * ACTION='any string corresponding to the soap action'
 * 
 * OPTIONAL REQUEST_SPECIFIC_SSL_CREDENTIAL=true/false : set to true if we need
 * to pass SSL credentials. In this case, the 4 following parameters need to be then added:  
 * KEYSTORE_PATH='path to keystore'
 * KEYSTORE_PASS='pass'
 * TRUSTSTORE_PATH='path to truststore'
 * TRUSTSTORE_PASS='pass'
 * 
 * IF MODE==async, we need to define the endpoint to send the response to:
 * REPLY_TO='the endpoint to send back to'
 * 
 */

public class RequestConfig extends java.util.Properties {

	private static final long serialVersionUID = 5086339236442906272L;

}
