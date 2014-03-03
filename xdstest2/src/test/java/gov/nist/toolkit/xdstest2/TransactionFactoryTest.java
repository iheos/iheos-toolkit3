package gov.nist.toolkit.xdstest2;

import gov.nist.toolkit.testengine.engine.StepContext;
import gov.nist.toolkit.testengine.engine.TransactionFactory;
import gov.nist.toolkit.testengine.transactions.BasicTransaction;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Created by bmajur on 3/3/14.
 */
public class TransactionFactoryTest {

    String[] interestingTransactionNames = {
            "IGQTransaction",
            "MPQTransaction",
            "MuTransaction",
            "ProvideAndRegisterTransaction",
            "RegisterTransaction",
            "RetrieveTransaction",
            "StoredQueryTransaction",
            "XDRProvideAndRegisterTransaction"
    };

    @Test
    public void runTest() {
        StepContext stepContext = new StepContext(null);
        TransactionFactory factory = new TransactionFactory(stepContext);
        for (String transactionName : interestingTransactionNames) {
            try {
                BasicTransaction trans = factory.build(transactionName, null, null);
            } catch (RuntimeException e) {
                fail(e.getMessage());
            }
        }
    }

}
