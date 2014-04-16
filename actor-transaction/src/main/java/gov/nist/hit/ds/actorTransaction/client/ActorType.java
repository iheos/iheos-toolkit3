package gov.nist.hit.ds.actorTransaction.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.List;

public class ActorType implements IsSerializable, Serializable {
		
		private static final long serialVersionUID = 1L;
		String name;   
		List<String> altNames;
		String shortName;
		List<TransactionType> transactionTypes; // TransactionTypes this actor can receive
		boolean showInConfig;
		String actorsFileLabel;
		String actorSimFactoryClassName;  // must be subclass of ActorFactory

    public void setName(String name) { this.name = name; };
    public void setShortName(String shortName) { this.shortName = shortName; }
    public void setActorSimFactoryClassName(String className) { this.actorSimFactoryClassName = className; }
    public void setTransactionTypes(List<TransactionType> types) { this.transactionTypes = types; }
    public List<TransactionType> getTransactionTypes() { return transactionTypes; }
		
		public ActorType() {}

		ActorType(String name, List<String> altNames, String shortName, String actorSimFactoryClassName,
                  List<TransactionType> tt, boolean showInConfig, String actorsFileLabel) {
			this.name = name;
			this.altNames = altNames;
			this.shortName = shortName;
			this.transactionTypes = tt;   // This actor receives
			this.showInConfig = showInConfig;
			this.actorsFileLabel = actorsFileLabel;
			this.actorSimFactoryClassName = actorSimFactoryClassName;
		}
		
		public String getActorSimFactoryClassName() {
			return actorSimFactoryClassName;
		}
		
		public boolean showInConfig() {
			return showInConfig;
		}
						
//		public boolean isRepositoryActor() {
//			return this.equals(REPOSITORY); 
//		}
//		
//		public boolean isRGActor() {
//			return this.equals(RESPONDING_GATEWAY);
//		}
//		
//		public boolean isIGActor() {
//			return this.equals(INITIATING_GATEWAY);
//		}
//		
//		public boolean isGW() {
//			return isRGActor() || isIGActor();
//		}
//		
//		public String getActorsFileLabel() {
//			return actorsFileLabel;
//		}
//
//		static public List<String> getActorNames() {
//			List<String> names = new ArrayList<String>();
//			
//			for (ActorType a : values())
//				names.add(a.name);
//			
//			return names;
//		}
		
//		/**
//		 * Within toolkit, each TransactionType maps to a unique ActorType
//		 * (as receiver of the transaction). To make this work, transaction
//		 * names are customized to make this mapping unique.  This goes
//		 * beyond the definition in the TF.
//		 * @param tt
//		 * @return
//		 */
		// now ActorTypeFactory.getActorType(TransactionType tt);
//		static public ActorType getActorType(TransactionType tt) {
//			if (tt == null)
//				return null;
//			for (ActorType at : values()) {
//				if (at.hasTransaction(tt))
//					return at;
//			}
//			return null;
//		}
		
		// now ActorTypeFactory.getActorType(name);
//		static public ActorType findActor(String name) {
//			if (name == null)
//				return null;
//			
//			for (ActorType actor : values()) {
//				if (actor.name.equals(name)) return actor;
//				if (actor.shortName.equals(name)) return actor;
//				if (actor.altNames.contains(name)) return actor;
//			}
//			return null;
//		}
			
		public String toString() {
			return "ActorType " + name;
		}
		
		public String getName() {
			return name;
		}
		
		public String getShortName() {
			return shortName;
		}
		
		public List<TransactionType> getTransactions() {
			return transactionTypes;
		}
		
		public boolean hasTransaction(TransactionType transType) {
			for (TransactionType transType2 : transactionTypes) {
				if (transType2.equals(transType))
					return true;
			}
			return false;
		}
		
		
		public boolean equals(ActorType at) {
			try {
				return name.equals(at.name);
			} catch (Exception e) {}
			return false;
		}
	}