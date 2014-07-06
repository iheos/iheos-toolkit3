package gov.nist.hit.ds.actorTransaction;

import gov.nist.hit.ds.actorTransaction.exceptions.InvalidTransactionTypeDefinitionException;
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
				if (s.equalsIgnoreCase(t.name)) return t;
				if (s.equalsIgnoreCase(t.shortName)) return t;
				if (s.equalsIgnoreCase(t.code)) return t;
				if (s.equalsIgnoreCase(t.asyncCode)) return t;
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

	private static TransactionType loadTransactionType(String typeName)  {
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

		check(ttype);

		transactionTypeMap.put(typeName, ttype);
		return ttype;
	}

	private static void check(TransactionType tt)  {
		String val;

		val = tt.id;
		if (val == null || val.equals(""))
			throw new InvalidTransactionTypeDefinitionException("id not defined");
		val = tt.name;
		if (val == null || val.equals(""))
			throw new InvalidTransactionTypeDefinitionException("name not defined");
		val = tt.shortName;
		if (val == null || val.equals(""))
			throw new InvalidTransactionTypeDefinitionException("shortName not defined");
		val = tt.code;
		if (val == null || val.equals(""))
			throw new InvalidTransactionTypeDefinitionException("code not defined");
		val = tt.asyncCode;
		if (val == null || val.equals(""))
			throw new InvalidTransactionTypeDefinitionException("asyncCode not defined");
		val = tt.requestAction;
		if (val == null || val.equals(""))
			throw new InvalidTransactionTypeDefinitionException("requestAction not defined");
		val = tt.responseAction;
		if (val == null || val.equals(""))
			throw new InvalidTransactionTypeDefinitionException("responseAction not defined");
	}
}
