package gov.nist.hit.ds.actorTransaction;

import gov.nist.hit.ds.actorTransaction.client.ActorType;
import gov.nist.hit.ds.actorTransaction.client.TransactionType;
import gov.nist.hit.ds.actorTransaction.exceptions.InvalidActorTransactionTypeDefinition;

import java.util.HashMap;
import java.util.Map;

public class ActorTypeFactory {
	static Map<String, ActorType> actorTypeMap = new HashMap<String, ActorType>();

	/**
	 * Within toolkit, each TransactionType maps to a unique ActorType
	 * (as receiver of the transaction). To make this work, transaction
	 * names are customized to make this mapping unique.  This goes 
	 * beyond the definition in the TF.
	 * 
	 * @param transactionType
	 * @return
	 */
	public static ActorType getActorType(TransactionType transactionType) {
		for(ActorType actorType : actorTypeMap.values()) {
			if (actorType.hasTransaction(transactionType))
					return actorType;
		}
		return null;
	}

	public static ActorType find(String actorTypeName) throws InvalidActorTransactionTypeDefinition {
		// must not be capitalized
		if (Character.isUpperCase(actorTypeName.charAt(0)))
			actorTypeName = Character.toLowerCase(actorTypeName.charAt(0)) + actorTypeName.substring(1);
		ActorType tt = actorTypeMap.get(actorTypeName);
		if (tt != null)
			return tt;
//		return loadActorType(actorTypeName);
        return null;
	}

//	private static ActorType loadActorType(String typeName) throws InvalidActorTransactionTypeDefinition {
//		InputStream in = ActorTypeFactory.class.getClassLoader().getResourceAsStream(typeName + "Actor.properties");
//		if (in == null)
//			throw new ActorTypeNotDefinedException("Resource File <" + typeName + "Actor.properties> not found");
//		Properties props;
//		try {
//			props = new Properties();
//			props.load(in);
//		} catch (Exception e) {
//			throw new ActorTypeNotDefinedException("type = " + typeName, e);
//		}
//
//		ActorType ttype = new ActorType();
//		ttype.name = props.getProperty("name");
//		ttype.shortName = props.getProperty("shortName");
//		ttype.actorSimFactoryClassName = props.getProperty("actorSimFactoryClass");
//		List<TransactionType> transactionTypes = new ArrayList<TransactionType>();
//		ttype.transactionTypes = transactionTypes;
//		for (Object okey : props.keySet()) {
//			String key = (String) okey;
//			if (key.startsWith("transactionType")) {
//				TransactionType tt = TransactionTypeFactory.getTransactionType(props.getProperty(key));
//				if (!transactionTypes.contains(tt))
//					transactionTypes.add(tt);
//			}
//		}
//
//		check(ttype);
//
//		actorTypeMap.put(typeName, ttype);
//		return ttype;
//	}

//	private static void check(ActorType at) throws InvalidActorTypeDefinitionException {
//		String val;
//
//		val = at.name;
//		if (val == null || val.equals(""))
//			throw new InvalidActorTypeDefinitionException("name not defined");
//		val = at.shortName;
//		if (val == null || val.equals(""))
//			throw new InvalidActorTypeDefinitionException("shortName not defined");
//		val = at.actorSimFactoryClassName;
//		if (val == null || val.equals(""))
//			throw new InvalidActorTypeDefinitionException("actorSimFactoryClass not defined");
//		if (at.transactionTypes.size() == 0)
//			throw new InvalidActorTypeDefinitionException("must define at least one transaction");
//	}
}
