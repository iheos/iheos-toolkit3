package gov.nist.toolkit.wsseTool.parsing;

import gov.nist.toolkit.wsseTool.api.config.Context;
import gov.nist.toolkit.wsseTool.api.exceptions.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class MessageFactory {

	private static final Logger log = LoggerFactory.getLogger(MessageFactory.class);
	
	public static Message getMessage(Element wsseHeader, Context context) throws ValidationException{
		validateContext(context);
		return buildMessage(wsseHeader, context);
	}
	
	private static void validateContext(Context context) throws ValidationException{
			if(context == null) throw new ValidationException("No context found.");
			if(context.getParams().get("homeCommunityId") == null){ log.warn("no homeCommunityId found in context. Some validations will not be performed.");}
			if(context.getParams().get("To") == null){ log.warn("no ws-addressing \"To\" info found in context. Some validations will not be performed.");}
	}
	
	private static Message buildMessage(Element wsseHeader, Context context) throws ValidationException{
		Message message = new Message(wsseHeader, context);
		
		try{
			//add gpath and opensaml representation to the context
			message.setGroovyHeader(MessageParser.parseToGPath(wsseHeader));
			message.setOpensamlHeader(MessageParser.parseToOpenSaml(wsseHeader));
		}
		catch(Exception e){
			throw new ValidationException("an error occured during validation.", e);
		}

		return message;
	}

}
