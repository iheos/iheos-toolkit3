package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import groovy.util.logging.Log4j

@Log4j
public class ActorTypeFactory {
	static Map<String, ActorType> actorTypeMap = new HashMap<String, ActorType>();

    static Set<String> getActorTypeNames() {
        return actorTypeMap.keySet();
    }

    public static ActorType find(String actorTypeName)  {
		// must not be capitalized
		if (Character.isUpperCase(actorTypeName.charAt(0)))
			actorTypeName = Character.toLowerCase(actorTypeName.charAt(0)) + actorTypeName.substring(1);
		ActorType actorType = actorTypeMap.get(actorTypeName);
		if (actorType != null)
			return actorType;
		return null
	}

    public static TransactionType find(String actorTypeName, String transactionTypeName) {
        ActorType actorType = find(actorTypeName)
        if (!actorType) {
            log.error("Actor type <${actorTypeName}> not found")
            log.error("Actors defined are <${getActorTypeNames()}>")
            throw new ToolkitRuntimeException("Actor type <${actorTypeName}> not found")
        }
        return actorType.find(transactionTypeName)
    }

	static ActorType loadActorType(String typeName)  {
		InputStream inputStream = ActorTypeFactory.class.getClassLoader().getResourceAsStream(typeName + "Actor.properties");
		if (inputStream == null)
			return null;
		Properties props;
		try {
			props = new Properties();
			props.load(inputStream);
		} catch (Exception e) {
            return null
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
        if (actorType.shortName) actorTypeMap.put(actorType.shortName, actorType)
		return actorType;
	}

    def static clear() { actorTypeMap.clear() }

}
