package gov.nist.hit.ds.tkInitialization;

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory;
import gov.nist.hit.ds.simSupport.simulator.SimSystemConfig;
import gov.nist.hit.ds.simSupport.utilities.SimSupport;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by bmajur on 10/28/14.
 */
public class InitializationServlet extends HttpServlet {
    static Logger logger = Logger.getLogger(InitializationServlet.class);
    ServletConfig config;
    String simRepositoryName = "Sim";
    ActorTransactionTypeFactory factory;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.config = config;
        init();
    }

    public void init() {
        logger.info("Toolkit initializing...");
//        BasicConfigurator.configure();

        SimSupport.initialize();
        factory = new ActorTransactionTypeFactory();
        factory.clear();
        factory.loadFromResource("actorTransactions.xml");
//        SimSystemConfig.setRepoName(simRepositoryName);
        logger.info("Toolkit initialized");
    }

}

