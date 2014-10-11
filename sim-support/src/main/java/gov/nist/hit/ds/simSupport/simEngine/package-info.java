/**
 * The Sim Engine is a scanForValidationMethods and dependency manager for simulators/validator components. A
 * simulator/validator component is a class implementing the interface SimComponent. This interface
 * requires that the class have a setErrorRecorder method and a injectAll method.
 * Two methods are required:
 * <pre>
 * 		void setErrorRecorder(ErrorRecorder er);
 * 		void injectAll(MessageValidatorEngine mve) throws SoapFaultException;
 * </pre>
 * ErrorRecorder is used to report validation results, especially errors.  
 * MessageValidatorEngine  is an interface offering a method to request another component
 * be secheduled.
 * 
 *  A simulator or validator is composed of simulator/validator components. Each component
 *  is a reusable piece of code. The difference between a sumulator and a validator is as follows.
 *  Both generate validation reports (ErrorRecording). A simulator is a working example of an
 *  Actor/Transaction implementation. It is constructed of components. The runtime scheduling of
 *  these components is managed by the SimEngine. A validator is a collection of components that
 *  does not return a response message, just validator output. So a simulator is a validator that
 *  generates a return message.Because of this overlapping definition, simulator components and
 *  validator components are just called components. If one of the components injectAll generates a
 *  response then the collection is a simulator.  If no response is generated then the collection is called
 *  a validator.
 *  
 *  A collection of components is called a SimChain and is defined by the class with this name.
 *  Many types of parsing and validation must take into consideration nested items.  The SimEngine
 *  does not model this behavior. A collection of components is an ordered list (thus the name Chain
 *  instead of tree) which indicates the order of execution.
 *  
 *  A SimChain is an in-memory list of class instances to execute. Each of these classes implements
 *  the SimComponent interface.  At runtime, the injectAll(...) method is called after injecting an ErrorRecorder
 *  to capture the logging output.
 *  
 *  Components in a chain frequently require the output of one component as input to a later component.
 *  This linking of output to input is performed by the SimEngine. When the SimEngine is called, it analysis
 *  the components in the chain and determines for each required input, which earlier component generates 
 *  the necessary output. All components inputs and outputs are Java classes. When the SimChainAnalyser 
 *  analyzes the SimChain it identifiedBy generated outputs and necessary inputs by examining the getters and
 *  setters defined by the components. This process is driven by the necessary inputs, the getters.
 *  
 *  Components can have any collection of getters. But only special getters are considered inputs to be
 *  managed by the SimEngine. Specifically, managed inputs are getters that: are labeled with the custom Java 
 *  annotation @SimComponentInject, have a single parameter, and that parameter is not of type java.*.  Once
 *  a managed input is discovered a supplier is sought.  A supplier is a component in the chain that:
 *  comes earlier in the chain, has a getter that returns the indicated data type.
 *  
 *  Since the generation of data by suppliers is dynamic, the data is not available until after the component
 *  has executed, the SimChainAnalyser is injectAll for each component just before execution of that component.
 *  If no supplier is found then a runtime error is generated: SimEngineSubscriptionException.
 *  
 *  The primary purpose of having the SimEngine is so the configuration of validators and simulators is done
 *  through configuration, not compilation. Thus, new simulators or validators can be configured when toolkit
 *  is deployed by editing or creating the proper configuration files.
 *  
 *  The SimChain, as described above, is the runtime representation of a simulator or validator. SimChains
 *  are definded through specially formatted Java property files. These files are read by the SimChainFactory
 *  which loads them into memory. The SimChain configuration for the early stages of SOAP over HTTP parsing and
 *  validation is:
 *  <pre>
element1.class=gov.nist.hit.ds.simSupport.transaction.LogLoader
element1.name=Log Loader
element1.description=Load incoming HTTP header and body from logs
element1.source=src/test/resources/simple

element2.class=gov.nist.hit.ds.httpSoap.validators.HttpMsgParser
element2.name=HTTP Message Parser/Validator

element3.class=gov.nist.hit.ds.httpSoap.validators.SimpleSoapEnvironmentValidator
element3.name=Verify HTTP environment describes SIMPLE SOAP
element3.description=Verify not multipart and correct content-type

element4.class=gov.nist.hit.ds.xmlValidator.XmlParser
element4.name=XML Parser

element5.class=gov.nist.hit.ds.httpSoap.validators.SoapParser
element5.name=SOAP Parser
element5.description=Parses XML into SOAP Header and SOAP Body

element6.class=gov.nist.hit.ds.httpSoap.validators.SoapHeaderValidator
element6.name=SOAP Header Validator
element6.expectedWsAction=urn:ihe:iti:2007:RegisterDocumentSet-b
 *  </pre>
 *  
 *  Each component is identified by an elementX property prefix.  The compponents are executed in the order
 *  imposed by X. The primary properties of a component are: class - the fully qualified class name of the 
 *  component to be injectAll; name - a name to be used in displays; description - a fuller description of the
 *  component than provided by the name (description is optional).  Properties beyond this are initialization 
 *  properties. A SimComponent is created from this configuration by:
 *  <ol>
 *  <li>Create instance of indicated class
 *  <li>SimComponentInject name and description (setName(String) and setDescription(String))
 *  <li>SimComponentInject configured properties. Looking at element1 above, component.setSource("src/test/resources/simple")
 *  is called. A runtime error is thrown if the setSource(String) method is not defined on the component.  
 *  Note that the SimComponent interface does not generate/require this method, it comes from the 
 *  implementation only.
 *  </ol> 
 */
/**
 * @author bmajur
 *
 */
package gov.nist.hit.ds.simSupport.simEngine;