package gov.nist.hit.ds.actorTransaction;

import gov.nist.hit.ds.actorTransaction.exceptions.ActorTypeNotDefinedException;
import gov.nist.hit.ds.actorTransaction.exceptions.InvalidActorTransactionTypeDefinition;

import java.io.InputStream;
import java.util.*;

public class ActorTypeFactory {
	static Map<String, ActorType> actorTypeMap = new HashMap<String, ActorType>();

	public static ActorType find(String actorTypeName) throws InvalidActorTransactionTypeDefinition {
		// must not be capitalized
		if (Character.isUpperCase(actorTypeName.charAt(0)))
			actorTypeName = Character.toLowerCase(actorTypeName.charAt(0)) + actorTypeName.substring(1);
		ActorType actorType = actorTypeMap.get(actorTypeName);
		if (actorType != null)
			return actorType;
		return loadActorType(actorTypeName);
	}

	static ActorType loadActorType(String typeName) throws InvalidActorTransactionTypeDefinition {
		InputStream inputStream = ActorTypeFactory.class.getClassLoader().getResourceAsStream(typeName + "Actor.properties");
		if (inputStream == null)
			throw new ActorTypeNotDefinedException("Resource File <" + typeName + "Actor.properties> not found");
		Properties props;
		try {
			props = new Properties();
			props.load(inputStream);
		} catch (Exception e) {
			throw new ActorTypeNotDefinedException("type = " + typeName, e);
		} finally {
            inputStream.close()
        }

		ActorType actorType = new ActorType();
		actorType.name = props.getProperty("name");
		actorType.shortName = props.getProperty("shortName");
		actorType.actorSimFactoryClassName = props.getProperty("actorSimFactoryClass");
		List<TransactionType> transactionTypes = new ArrayList<TransactionType>();
		actorType.transactionTypes = transactionTypes;
		for (Object okey : props.keySet()) {
			String key = (String) okey;
			if (key.startsWith("transactionType")) {
				TransactionType tt = TransactionTypeFactory.getTransactionType(props.getProperty(key));
				if (!transactionTypes.contains(tt))
					transactionTypes.add(tt);
			}
		}

		actorType.check();

		actorTypeMap.put(typeName, actorType);
		return actorType;
	}

    def static clear() { actorTypeMap.clear() }

}
