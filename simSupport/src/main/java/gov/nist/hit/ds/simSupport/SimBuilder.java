package gov.nist.hit.ds.simSupport;

import gov.nist.hit.ds.actorTransaction.ATFactory;
import gov.nist.hit.ds.actorTransaction.ATFactory.ActorType;
import gov.nist.hit.ds.actorTransaction.ATFactory.TransactionType;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.SystemErrorRecorderBuilder;
import gov.nist.hit.ds.http.HttpEnvironment;
import gov.nist.hit.ds.http.HttpHeader;
import gov.nist.hit.ds.http.HttpHeader.HttpHeaderParseException;
import gov.nist.hit.ds.http.ParseException;
import gov.nist.hit.ds.simSupport.client.NoSimException;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.SimulatorConfig;
import gov.nist.hit.ds.simSupport.client.SimulatorConfigElement;
import gov.nist.hit.ds.soap.SoapEnvironment;
import gov.nist.hit.ds.soap.SoapFault;
import gov.nist.hit.ds.soap.exceptions.EndpointUnavailableException;
import gov.nist.hit.ds.soap.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import gov.nist.hit.ds.xdsException.XdsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.stax2.validation.ValidationContext;

public class SimBuilder {
	File warHome;
	File simDbDir;
	ServletContext servletContext;
	HttpServletRequest request;
	HttpServletResponse response;
	SoapEnvironment soapEnv;
	SimDb db;

	// properties extracted from endpoint
	String simid = null;
	String actor = null;
	String transaction = null;
	String validation = null;
	TransactionType transactionType = null;

	public SimBuilder(File warHome, File simDbDir, ServletContext servletContext) {
		this.warHome = warHome;
		this.simDbDir = simDbDir;
		this.servletContext = servletContext;
		HttpEnvironment httpEnv = new HttpEnvironment();
		httpEnv.setResponse(response);
		soapEnv = new SoapEnvironment(httpEnv);
	}

	/**
	 * Run transaction and send response.
	 * @param request
	 * @param response
	 */
	public void runTransaction(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		response.setContentType("application/soap+xml");
		String uri  = request.getRequestURI().toLowerCase();
		//		RegIndex regIndex = null;
		//		RepIndex repIndex = null;

		/*
		 * the following properties are set by this call:
		 * 	simid
		 * 	actor
		 * 	transaction
		 * 	validation
		 * 	transactionType
		 */
		try {
			parseEndpoint(uri);
		} catch (SoapFaultException e) {
			new SoapFault(soapEnv, e).send();
			return;
		}

		Date now = new Date();

		boolean transactionOk = true;

		MessageValidatorEngine mvc = new MessageValidatorEngine();
		//		Session session = new Session(warHome);
		try {

			// DB space for this simulator
			db = new SimDb(new SimId(simid), actor, transaction);
			request.setAttribute("SimDb", db);

			logRequest();

			SimManager mgr = new SimCache().getSimManagerForSession(session.id());
			SimulatorConfig asc = mgr.getSimConfig(simDbDir, simid);
			SimulatorConfig asc = new SimulatorFactory();

//			regIndex = getRegIndex(db, simid);
//			repIndex = getRepIndex(db, simid);



			ValidationContext vc = new ValidationContext();

			SimulatorConfigElement asce = asc.get(ActorFactory.codesEnvironment);
			if (asce != null)
				vc.setCodesFilename(asce.asString());



//			SimCommon common= new SimCommon(db, uri.startsWith("https"), vc, mvc, regIndex, repIndex, response);

			ErrorRecorder er = new SystemErrorRecorderBuilder().buildNewErrorRecorder();
			er.sectionHeading("Endpoint");
			er.detail("Endpoint is " + uri);
			mvc.addErrorRecorder("Web Service", er);

			SimLoader loader = new SimLoader(actor);
			GenericSim sim = loader.getSimulator();
			sim.run();

			/////////////////////////////////////////////////////////////
			//
			// run the simulator for the requested actor/transaction pair
			//
			//////////////////////////////////////////////////////////////

			if (ActorType.REGISTRY.getShortName().equals(actor)) {

				SimulatorConfigElement updateConfig = asc.get(RegistryActorFactory.update_metadata_option);
				boolean updateEnabled = updateConfig.asBoolean();
				if (transactionType.equals(TransactionType.UPDATE) && !updateEnabled) {
					sendSoapFault(response, "Metadata Update not enabled on this actor " + uri + endpointFormat);
					return;
				}

				response.setContentType("application/soap+xml");

				common.regIndex = regIndex;
				RegistryActorSimulator reg = new RegistryActorSimulator(common, db, asc);
				transactionOk = reg.run(transactionType, mvc, validation);

			}
			else if (ActorType.RESPONDING_GATEWAY.getShortName().equals(actor)) {
				response.setContentType("application/soap+xml");

				RGActorSimulator rg = new RGActorSimulator(common, db, asc);
				transactionOk = rg.run(transactionType, mvc, validation);

			}
			else if (ActorType.INITIATING_GATEWAY.getShortName().equals(actor)) {
				response.setContentType("application/soap+xml");

				IgActorSimulator ig = new IgActorSimulator(common, db, asc);
				transactionOk = ig.run(transactionType, mvc, validation);

			}
			else if (ActorType.REPOSITORY.getShortName().equals(actor)) {

				String repositoryUniqueId = "";

				SimulatorConfigElement configEle = asc.get("repositoryUniqueId");
				if (configEle == null || configEle.asString() == null || configEle.asString().equals("")) {

				} else {
					repositoryUniqueId = configEle.asString();
				}

				RepositoryActorSimulator rg = new RepositoryActorSimulator(repIndex, common, db, asc, response, repositoryUniqueId);
				transactionOk = rg.run(transactionType, mvc, validation);

			}
			else if (ActorType.DOCUMENT_RECIPIENT.getShortName().equals(actor)) {

				RecipientActorSimulator rg = new RecipientActorSimulator(common, db, asc, response);
				transactionOk = rg.run(transactionType, mvc, validation);

			}
			else {
				sendSoapFault(response, "Simulator: Do not understand endpoint " + uri + ". Actor " + actor + " is not understood. " + endpointFormat);
				mvc.run();
				closeOut(response);
				return;
			}





		} 
		catch (RuntimeException e) {
			sendSoapFault(response, ExceptionUtil.exception_details(e));
			logger.error(ExceptionUtil.exception_details(e));
			responseSent = true;
		} 
		catch (IOException e) {
			sendSoapFault(response, ExceptionUtil.exception_details(e));
			logger.error(ExceptionUtil.exception_details(e));
			responseSent = true;
		} 
		catch (HttpHeaderParseException e) {
			sendSoapFault(response, ExceptionUtil.exception_details(e));
			logger.error(ExceptionUtil.exception_details(e));
			responseSent = true;
		} catch (ClassNotFoundException e) {
			sendSoapFault(response, ExceptionUtil.exception_details(e));
			logger.error(ExceptionUtil.exception_details(e));
			responseSent = true;
		} catch (XdsException e) {
			sendSoapFault(response, ExceptionUtil.exception_details(e));
			logger.error(ExceptionUtil.exception_details(e));
			responseSent = true;
		} catch (NoSimException e) {
			sendSoapFault(response, ExceptionUtil.exception_details(e));
			logger.error(ExceptionUtil.exception_details(e));
			responseSent = true;
		} catch (ParseException e) {
			sendSoapFault(response, ExceptionUtil.exception_details(e));
			logger.error(ExceptionUtil.exception_details(e));
			responseSent = true;
		} 
		finally {
			mvc.run();
			closeOut(response);
		}

		mvc.run();


		// this should go away after repository code made to use deltas
		if (!transactionOk) {
			synchronized(this) {
				// delete memory copy of indexes so they don't get written out
				servletContext.setAttribute("Rep_" + simid, null);
				repIndex = null;
			}
		}




		List<String> flushed = new ArrayList<String>();
		int regCacheCount = 0;
		int repCacheCount = 0;
		try {

			// Update disk copy of indexes
			if (repIndex != null) {
				repIndex.save();
			}

			synchronized(this) {

				// check for indexes that are old enough they should be removed from cache
				for (@SuppressWarnings("unchecked")
				Enumeration<String> en = (Enumeration<String>) servletContext.getAttributeNames(); en.hasMoreElements(); ) {
					String name = en.nextElement();
					if (name.startsWith("Reg_")) {
						RegIndex ri = (RegIndex) servletContext.getAttribute(name);
						if (ri.cacheExpires.before(now)) {
							logger.info("Unloading " + name);
							servletContext.removeAttribute(name);
							flushed.add(name);
						} else
							regCacheCount++;
					}
					if (name.startsWith("Rep_")) {
						RepIndex ri = (RepIndex) servletContext.getAttribute(name);
						if (ri.cacheExpires.before(now)) {
							logger.info("Unloading " + name);
							servletContext.removeAttribute(name);
							flushed.add(name);
						} else
							repCacheCount++;
					}
				}

			}
		} catch (IOException e) {
			if (!responseSent)
				sendSoapFault(response, ExceptionUtil.exception_details(e));
			e.printStackTrace();
		}

		logger.debug(regCacheCount + " items left in the Registry Index cache");
		logger.debug(repCacheCount + " items left in the Repository Index cache");

	}

	/**
	 * Parse endpoint into it RESTful parts.
	 * Endpoint looks like
	 * 		 http://host:port/xdstools2/sim/simid/actor/transaction[/validation]
	 *	 where
	 *	   simid is a uniqueID for a simulator
	 *	   actor
	 *	   transaction
	 *	   validation - name of a validation to be performed
	 * @param endpoint
	 */
	void parseEndpoint(String endpoint) throws SoapFaultException {
		String[] uriParts = endpoint.split("\\/");
		String toolkitServletName = (uriParts.length < 2) ? "" : uriParts[1];
		int simIndex;

		for (simIndex=0; simIndex<uriParts.length; simIndex++) {
			if ("sim".equals(uriParts[simIndex]))
				break;
		}
		if (simIndex >= uriParts.length) {
			throw new EndpointUnavailableException("Endpoint was " + endpoint);
		}

		List<String> transIds = new ArrayList<String>();
		transIds.add("pnr");
		transIds.add("xcqr");

		try {
			simid = uriParts[simIndex + 1];
			actor = uriParts[simIndex + 2];
			transaction = uriParts[simIndex + 3];
		}
		catch (Exception e) {
			throw new EndpointUnavailableException("Endpoint was " + endpoint);
		}

		try {
			validation = null;
			validation = uriParts[simIndex+4];
		} catch (Exception e) {
			// ignore - null value will signal no validation
		}

		transactionType = ATFactory.findTransactionByShortName(transaction); 
		if (transactionType == null) {
			throw new EndpointUnavailableException("Endpoint was " + endpoint + " - Do not understand the transaction requested - " + transaction);
		}

	}

	void logRequest()
			throws FileNotFoundException, IOException, HttpHeaderParseException, ParseException {
		Map<String, String> headers = new HashMap<String, String>();
		String contentType;
		HttpHeader contentTypeHeader;
		String bodyCharset;
		StringBuffer buf = new StringBuffer();

		buf.append(request.getMethod() + " " + request.getRequestURI() + " " + request.getProtocol() + "\r\n");
		for (@SuppressWarnings("unchecked")
		Enumeration<String> en=request.getHeaderNames(); en.hasMoreElements(); ) {
			String name = en.nextElement();
			String value = request.getHeader(name);
			if (name.equals("Transfer-Encoding"))
				continue;  // log will not include transfer encoding so don't include this
			headers.put(name.toLowerCase(), value);
			buf.append(name).append(": ").append(value).append("\r\n");
		}
		//		bodyCharset = request.getCharacterEncoding();
		String ctype = headers.get("content-type");
		if (ctype == null || ctype.equals(""))
			throw new IOException("Content-Type header not found");
		contentTypeHeader = new HttpHeader("content-type: " + ctype);
		bodyCharset = contentTypeHeader.getParam("charset");
		contentType = contentTypeHeader.getValue();

		if (bodyCharset == null || bodyCharset.equals(""))
			bodyCharset = "UTF-8";

		buf.append("\r\n");


		db.putRequestHeaderFile(buf.toString().getBytes());

		db.putRequestBodyFile(Io.getBytesFromInputStream(request.getInputStream()));

	}

}
