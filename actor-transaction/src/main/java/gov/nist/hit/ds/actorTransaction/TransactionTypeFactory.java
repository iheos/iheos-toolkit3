package gov.nist.hit.ds.actorTransaction;

import gov.nist.hit.ds.actorTransaction.exceptions.TransactionTypeNotDefinedException;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class TransactionTypeFactory {
	static Map<String, TransactionType> transactionTypeMap = new HashMap<String, TransactionType>();

	static Set<String> getTransactionTypeNames() {
		return transactionTypeMap.keySet();
	}

	static public TransactionType find(String s) {
		if (transactionTypeMap.isEmpty())
			throw new ToolkitRuntimeException("TransactionTypeFactory not initialized");
		if (s != null) {
			for (TransactionType t : transactionTypeMap.values()) {
                if (t.identifiedBy(s)) return t;
			}
		}
		throw new ToolkitRuntimeException("TransactionType for label <" + s + "> not found");
	}

	protected static TransactionType getTransactionType(String transactionTypeName)  {
		TransactionType tt = transactionTypeMap.get(transactionTypeName);
		if (tt != null)
			return tt;
		return loadTransactionType(transactionTypeName);
	}

	static TransactionType loadTransactionType(String typeName)  {
		InputStream in = TransactionTypeFactory.class.getClassLoader().getResourceAsStream(typeName + "Transaction.properties");
		if (in == null)
			throw new TransactionTypeNotDefinedException("Resource File <" + typeName + "Transaction.properties> not found");
		Properties props;
		try {
			props = new Properties();
			props.load(in);
		} catch (Exception e) {
			throw new TransactionTypeNotDefinedException("type = " + typeName, e);
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
