package gov.nist.toolkit.soapClientAPI;

/**
 * This object holds request configuration parameters in java properties Here
 * are the properties supported so far :
 * 
 * REQUIRED:
 * 
 * - MODE=async/sync : synchronous or async If MODE==async, we need to define
 * the endpoint to send the response to, either as a additional <ReplyTo>
 * header, either as a parameter : REPLY_TO='the endpoint to send back to'
 * 
 * - GENERATE_WSA = true/false if true, the underlying implementation is
 * responsible for creating addressing content if not present in the provided
 * headers. If false, you are on your own!
 * 
 * OPTIONAL :
 * 
 * - TO='the url endpoint to send to'. If not provided as header.
 * 
 * - ACTION='any string corresponding to the soap action'. If not provided as header.
 * 
 * - REQUEST_SPECIFIC_SSL_CREDENTIAL=true/false : set to true if we need to pass
 * SSL credentials.In this case, the 4 following parameters need to be then
 * added: KEYSTORE_PATH='path to keystore' KEYSTORE_PASS='pass'
 * TRUSTSTORE_PATH='path to truststore' TRUSTSTORE_PASS='pass'
 * 
 * - MTOM = When this property is set to True, any SOAP envelope, regardless of
 * whether it contains optimizable content or not, will be serialized as an MTOM
 * optimized MIME message. (MTOM contents are create with axiom using
 * DataHandler, see:
 * http://axis.apache.org/axis2/java/core/docs/mtom-guide.html#a22 for details)
 */

public class RequestConfig extends java.util.Properties {

	private static final long serialVersionUID = 5086339236442906272L;

}
