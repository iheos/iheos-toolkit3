package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j

@Log4j
public class TransactionTypeFactory {
	static Map<String, TransactionType> transactionTypeMap = new HashMap<String, TransactionType>();

	static Set<String> getTransactionTypeNames() {
		return transactionTypeMap.keySet();
	}

	static public TransactionType find(String transactionTypeName) {
		if (transactionTypeName != null) {
			for (TransactionType t : transactionTypeMap.values()) {
                if (t.identifiedBy(transactionTypeName)) return t;
			}
		}
		throw new ToolkitRuntimeException("TransactionType for label <" + transactionTypeName + "> not found");
	}

	protected static TransactionType getTransactionType(String transactionTypeName)  {
		TransactionType tt = transactionTypeMap.get(transactionTypeName);
		if (tt != null)
			return tt;
		return loadTransactionType(transactionTypeName);
	}

	static TransactionType loadTransactionType(String typeName)  {
        log.debug("loading ${typeName.trim()}Transaction.properties")
        InputStream ins = TransactionTypeFactory.class.getClassLoader().getResourceAsStream(typeName + "Transaction.properties")
        if (ins == null) return null
		Properties props;
		try {
			props = new Properties();
			props.load(ins);
		} catch (Exception e) {
            ins?.close()
            return null
		}

		TransactionType ttype = new TransactionType();
		ttype.id = props.getProperty("id");
		ttype.name = props.getProperty("name");
		ttype.shortName = props.getProperty("shortName");
		ttype.code = props.getProperty("code");
		ttype.asyncCode = props.getProperty("asyncCode");
		ttype.requestAction = props.getProperty("requestAction");
		ttype.responseAction = props.getProperty("responseAction");

		ttype.check();

		transactionTypeMap.put(typeName, ttype);
		return ttype;
	}

}
