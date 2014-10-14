package gov.nist.hit.ds.simSupport.simChain
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.simSupport.simEngine.SimComponentFactory
import gov.nist.hit.ds.simSupport.simEngine.SimStep
import gov.nist.hit.ds.simSupport.loader.PropertyResourceFactory
import gov.nist.hit.ds.simSupport.loader.SimComponentPropFormatParser
import gov.nist.hit.ds.simSupport.validationEngine.ValComponent
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import org.apache.log4j.Logger

/**
 * The property file based format for a SimChain looks like:
 element1.class=gov.nist.hit.ds.simSupport.loader.ByParamLogLoader
 element1.displayName=Log Loader
 element1.description=Load incoming HTTP header and body from logs
 element1.source=src/test/resources/simple

 element2.class=gov.nist.hit.ds.httpSoap.validators.HttpMsgParser
 element2.displayName=HTTP Message Parser/Validator

 element3.class=gov.nist.hit.ds.httpSoap.validators.SimpleSoapEnvironmentValidator
 element3.displayName=Verify HTTP environment describes SIMPLE SOAP
 element3.description=Verify not multipart and correct content-type

 element4.class=gov.nist.hit.ds.xmlValidator.XmlParser
 element4.displayName=XML Parser

 element5.class=gov.nist.hit.ds.httpSoap.validators.SoapParser
 element5.displayName=SOAP Parser
 element5.description=Parses XML into SOAP Header and SOAP Body

 element6.class=gov.nist.hit.ds.httpSoap.validators.SoapHeaderValidator
 element6.displayName=SOAP Header Validator
 element6.expectedWsAction=urn:ihe:iti:2007:RegisterDocumentSet-b
 *
 * .class indicates the Component implementation
 * .displayName and .description are documentation
 * Others, like .source or .expectedWsAction are parameters to the
 * Component.
 */
class SimChainFactory {
    SimChain simChain
    Event event
    private static Logger logger = Logger.getLogger(SimChainFactory)

    /**
     * Build in a test environment:
     *   new SimChainFactory(event)
     *   addComponent(), addComponent(), addComponent()
     *   getSimChain()
     * @return
     */
    SimChainFactory(Event event) { this.event = event; simChain = new SimChain() }

    def addComponent(String className, Map<String, String> parameters) {
        SimStep simStep = new SimStep()
        simStep.init(event)
        simChain.addStep(simStep)
        ValComponent component = loadComponent(className, parameters, simStep)
        simStep.simComponent = component
    }

    ValComponent loadComponent(String className, Map<String, String> parameters, SimStep simStep) {
        try {
            SimComponentFactory factory = new SimComponentFactory(className, parameters)
            return factory.component
        }
        catch (Exception e) {
            throw new SoapFaultException(simStep.event, FaultCode.Receiver, e.message)
        }
    }

    /**
     * Build from Property file configuration:
     *   new SimChainFactory(event)
     *   loadFromProperyBasedResource(resourceReference)
     * @param chainDefResource
     * @return SimChain
     */
     def loadFromPropertyBasedResource(String chainDefResource) {
        try {
            PropertyResourceFactory propFactory = new PropertyResourceFactory(chainDefResource)
            Properties properties = propFactory.getProperties()
            SimComponentPropFormatParser parser = new SimComponentPropFormatParser(properties)
            for (Properties parmMap : parser.getComponentProperties()) {
                String className = parmMap.getProperty("class")
                addComponent(className, withoutStandardProperties(parmMap))
            }
        } catch (SoapFaultException e) {
            throw e
        } catch (Exception e) {
            throw new ToolkitRuntimeException("SimChainFactory failed: ${e.getMessage()}", e)
        }
    }

    static checkFromResource(String chainDefResource) throws Exception {
            def factory = new SimChainFactory(null)
            factory.loadFromPropertyBasedResource(chainDefResource)
    }

    Properties withoutStandardProperties(Properties input) {
        Properties out = new Properties()
        input.each { key, value ->
            if (key.endsWith('.class')) return
            if (key.endsWith('.displayName')) return
            if (key.endsWith('.description')) return
            out.setProperty(key, value)
        }
        return out
    }
}