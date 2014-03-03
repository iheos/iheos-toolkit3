package gov.nist.toolkit.testengine.engine;

import gov.nist.toolkit.testengine.transactions.BasicTransaction;
import gov.nist.toolkit.testengine.transactions.RetrieveTransaction;
import org.apache.axiom.om.OMElement;

/**
 * Created by bmajur on 3/3/14.
 */
public class TransactionFactory {
    StepContext stepContext;
    boolean isXca = false;
    boolean useIg = false;

    public TransactionFactory(StepContext stepContext) { this.stepContext = stepContext; }

    public BasicTransaction build(String transactionName, OMElement instruction, OMElement instruction_output) {
        Class<?> clazz;
        String trClass = "gov.nist.toolkit.testengine.transactions." + transactionName;

        // Build transaction instance
        try {
            clazz = getClass().getClassLoader().loadClass(trClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot create transaction <" + transactionName + "> from class <" + trClass + "> - does not exist.", e);
        }
        BasicTransaction trans;
        try {
            Object obj = clazz.newInstance();
            if (!(obj instanceof BasicTransaction)) {
                throw new RuntimeException("Cannot create transaction <" + transactionName + "> from class <" + trClass + "> - it must be a subclass of BasicTransaction.");
            }
            trans = (BasicTransaction) obj;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create transaction <" + transactionName + "> from class <" + trClass + "> - unknown problem.", e);
        }

        // Initialize transaction instance
        trans.initialize(stepContext, instruction, instruction_output);

        // Handle a couple of special cases
        if (transactionName.equals("XCRTransaction")) {
            isXca = true;
        }
        if (transactionName.equals("IGRTransaction")) {
            isXca = true;
            useIg = true;
        }
        if (trans instanceof RetrieveTransaction) {
            ((RetrieveTransaction) trans).setIsXca(isXca);
            ((RetrieveTransaction) trans).setUseIG(useIg);
        }

        return trans;
    }

}
