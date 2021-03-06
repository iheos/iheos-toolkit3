package gov.nist.toolkit.wsseTool.validation

import gov.nist.toolkit.wsseTool.validation.Validation
import gov.nist.toolkit.wsseTool.context.SecurityContextImpl;
import gov.nist.toolkit.wsseTool.parsing.groovyXML.GroovyHeader;
import gov.nist.toolkit.wsseTool.time.TimeUtil
import groovy.util.slurpersupport.GPathResult

import org.slf4j.Logger
import org.slf4j.LoggerFactory



class TimestampVal {

	private static final Logger log = LoggerFactory.getLogger(TimestampVal.class)
	
	private GroovyHeader header
	private SecurityContextImpl context

	public TimestampVal(SecurityContextImpl context){
		this.context = context
		this.header = context.groovyHeader
	}

	@Validation(id="1020", rtm=["45","50","53"])
	public void ordering() {
		def children = header.map.timestamp.children()
		
		if('Created' != children[0].name()) log.error("timestamp first child should be Created")
		if('Expires' != children[1].name()) log.error("timestamp first child should be Expires")
		
		assert 'Created' == children[0].name()
		assert 'Expires' == children[1].name()
	}

	@Validation(id="1021" , rtm=["51", "54"])
	public void created() {
		String d = header.map.timestamp.Created[0].text()

		if( !TimeUtil.isDateInUTCFormat(d) )log.error("timestamp creation date not in UTC format : {}", d)
		assert TimeUtil.isDateInUTCFormat(d)
	}

	@Validation(id="1022" , rtm=["52", "54"])
	public void expires() {
		String d = header.map.timestamp.Expires[0].text()

		if( !TimeUtil.isDateInUTCFormat(d) )log.error("timestamp expiration date not in UTC format : {}", d)
		assert TimeUtil.isDateInUTCFormat(d)

		log.info(ValDescriptor.NOT_IMPLEMENTED)
		log.info(ValDescriptor.LATER_THAN_CAPTURED_TIME)
	}

	@Validation(id="1023", rtm=["168"])
	public void uniqueId() {
		def timestamp = header.map.timestamp;
		
		String prefix = header.namespaces.getPrefix("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
		
		GString id = "${prefix}:Id"
		if( header.map.timestamp.@"${prefix}:Id" == "" )log.error("timestamp does not have a valid Id attribute");
		assert header.map.timestamp.@"${prefix}:Id" != ""
		
		log.info("Check if {} is unique in the header", id);
		if(! CommonVal.uniqueId(header,header.map.timestamp.@"${prefix}:Id".text())) log.error("timestamp id is not unique in the document");
		assert CommonVal.uniqueId(header,header.map.timestamp.@"${prefix}:Id".text())
	}
}
