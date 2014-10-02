package gov.nist.toolkit.soapClientAPI;

/**
 * This object holds request configuration parameters in java properties Here
 * are the properties supported so far :
 * 
 * 
 * REQUIRED:
 * 
 * - MODE=async/sync : synchronous or asynchronous message exchange.
 * Synchronous means that the request and response are sent on the same channel
 * (regular http exchange pattern). Asynchronous means the receiver acknowledge 
 * reception of the message and then responds on a second channel defined in the
 * <ReplyTo> header.
 * 
 * OPTIONAL :
 * 
 * - TO='the url endpoint to send to'.
 * 
 * - ACTION='any string corresponding to the soap action'.
 * 
 * - REPLY_TO='the endpoint to send back to'. Only possible for asynchronous request.
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
