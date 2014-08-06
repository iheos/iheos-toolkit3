package gov.nist.toolkit.axis2SoapClient.impl;

import gov.nist.toolkit.axis2SoapClient.impl.customSSL.AuthSSLProtocolSocketFactory;
import gov.nist.toolkit.axis2SoapClient.validation.RequestParams;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperationClientRequestBuilder {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public OperationClientRequestBuilder() {
	}

	public MessageContext buildRequest(RequestParams params, SOAPEnvelope envelope) throws AxisFault {
		// creating message context
		MessageContext outMsgCtx = new MessageContext();
		outMsgCtx.setEnvelope(envelope);
		Options opts = outMsgCtx.getOptions();

		opts.setTo(params.to);
		opts.setAction(params.action);

		if (params.async) {
			opts.setReplyTo(params.replyTo);
			opts.setUseSeparateListener(true);
		}

		if (params.MTOM) {
			opts.setProperty(Constants.Configuration.ENABLE_MTOM, Boolean.TRUE);
		}

		if (params.requestSpecificSSLCredential) {
			try {
				Protocol protocol = getAuthHttpsProtocol(params);
				opts.setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER, protocol);
			} catch (IOException e) {
				throw new AxisFault("Failed to create custom Protocol for TLS\n", e);
			}
		}

		return outMsgCtx;
	}

	// This code is used to bypass the use of javax.net.ssl.keyStore and similar
	// JVM level controls on the certs used and specify certs on a
	// per-connection basis.
	@SuppressWarnings("deprecation")
	Protocol getAuthHttpsProtocol(RequestParams params) throws MalformedURLException, IOException {
		String keyStoreFile = "file:" + params.keystorePath;
		String keyStorePass = params.keystorePass;
		String trustStoreFile = "file:" + params.truststorePath;
		String trustStorePass = params.truststorePass;
		int tlsPort = new URL(params.to.getAddress()).getPort();

		return new Protocol("https", new AuthSSLProtocolSocketFactory(new URL(keyStoreFile), keyStorePass, new URL(
				trustStoreFile), trustStorePass), tlsPort);
	}

}
