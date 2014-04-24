package gov.nist.hit.ds.simSupport.engine

import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.simSupport.loader.PropertyResourceFactory
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException

/**
 * The property file based format for a SimChain looks like:
 element1.class=gov.nist.hit.ds.simSupport.loader.ByParamLogLoader
 element1.name=Log Loader
 element1.description=Load incoming HTTP header and body from logs
 element1.source=src/test/resources/simple

 element2.class=gov.nist.hit.ds.httpSoapValidator.validators.HttpMsgParser
 element2.name=HTTP Message Parser/Validator

 element3.class=gov.nist.hit.ds.httpSoapValidator.validators.SimpleSoapEnvironmentValidator
 element3.name=Verify HTTP environment describes SIMPLE SOAP
 element3.description=Verify not multipart and correct content-type

 element4.class=gov.nist.hit.ds.xmlValidator.XmlParser
 element4.name=XML Parser

 element5.class=gov.nist.hit.ds.httpSoapValidator.validators.SoapParser
 element5.name=SOAP Parser
 element5.description=Parses XML into SOAP Header and SOAP Body

 element6.class=gov.nist.hit.ds.httpSoapValidator.validators.SoapHeaderValidator
 element6.name=SOAP Header Validator
 element6.expectedWsAction=urn:ihe:iti:2007:RegisterDocumentSet-b
 *
 * .class indicates the Component implementation
 * .name and .description are documentation
 * Others, like .source or .expectedWsAction are parameters to the
 * Component.
 */
class SimChainFactory {
    SimChain simChain
    Event event

    /**
     * Build in a test environment:
     *   new SimChainFactory(event)
     *   addComponent(), addComponent(), addComponent()
     *   getSimChain()
     * @return
     */
    SimChainFactory(Event event) { this.event = event; simChain = new SimChain() }

    void addComponent(String className, Map<String, String> parameters) {
        SimComponentFactory factory = new SimComponentFactory(className, parameters)
        SimComponent component = factory.component
        assert component
        SimStep simStep = new SimStep()
        simStep.init(event)
        simStep.simComponent = component
        simChain.addStep(simStep)
    }

    /**
     * Build from Property file configuration:
     *   new SimChainFactory(event)
     *   loadFromProperyBasedResource(resourceReference)
     * @param chainDefResource
     * @return SimChain
     */
     void loadFromPropertyBasedResource(String chainDefResource) {
        List<SimStep> simSteps = new ArrayList<SimStep>();

        try {
            PropertyResourceFactory propFactory = new PropertyResourceFactory(chainDefResource)
            Properties properties = propFactory.getProperties()
            SimComponentPropFormatParser parser = new SimComponentPropFormatParser(properties)
            for (Properties parmMap : parser.getComponentProperties()) {
                SimComponentFactory componentFactory = new SimComponentFactory(parmMap.getProperty("class"), parmMap)
                SimComponent component = componentFactory.load()

                def step = new SimStep()
                step.simComponent = component
                simSteps.add(step)
            }
            simChain.addSteps(simSteps)
        } catch (Exception e) {
            throw new ToolkitRuntimeException("SimChainFactory failed.", e)
        }
    }
}