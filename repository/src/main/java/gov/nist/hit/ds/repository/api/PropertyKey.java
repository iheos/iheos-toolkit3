/**
 * 
 */
package gov.nist.hit.ds.repository.api;

import gov.nist.hit.ds.repository.simple.search.client.PnIdentifier;

/**
 * This enumeration contains some of the frequently used property keys, as used in {@link Properties#getProperty(java.io.Serializable)}, which is common to all types repositories or assets.
 * @author Sunil.Bhaskarla
 *
 */

public enum PropertyKey {	
       UNIDENTIFIED(false){},
       
       ASSET_ID(false){
           @Override
           public String toString() {
               return "id";
           }
       },
       ASSET_NAME(false) {
           @Override
           public String toString() {
               return "name";
           }
       },
       ASSET_TYPE(false){
           @Override
           public String toString() {
               return "type";
           }
       },
       COLOR(false) {
            @Override
            public String toString() {
                return "color";
            }
        },
       CREATED_DATE(false){
           @Override
           public String toString() {
               return "createdDate";
           }
       },
       DESCRIPTION(false) {	        	
           @Override
           public String toString() {
               return "description";
           }	        	
       },
       DISPLAY_NAME(false) {	        	
           @Override
           public String toString() {
               return "displayName";
           }	        	
       },
       DISPLAY_ORDER(false){
           @Override
           public String toString() {
               return "displayOrder";
           }
       },	        
       EXPIRATION_DATE(false){
           @Override
           public String toString() {
               return "expirationDate";
           }
       },
       HASH(true) {
           @Override
           public String toString() {
               return "hash";
           }    	   
       },
       INDEX_SESSION(true) {
    	   @Override
           public String toString() {
               return "indexSession";
           }
       },
        LAST_ACCESS_TIME(true) {
            @Override
            public String toString() {
                return "lastAccessTime";
            }
        },
        LOCATION(true) {
           @Override
           public String toString() {
               return "location";
           }
       },
       MIME_TYPE(false){
           @Override
           public String toString() {
               return "mimeType";
           }
       },
       MODIFIED_DATE(false) {
          	@Override
              public String toString() {
                  return "modifiedDate";
              }
       },
       PARENT_ID(false){
           @Override
           public String toString() {
               return "parentId";
           }
       },
       REPOSITORY_ID(false){
           @Override
           public String toString() {
               return "repositoryId";
           }
       },
       REPOSITORY_ACCESS(true) {
           @Override
           public String toString() {
               return "reposAcs";
           }
       },
       REPOSITORY_TYPE(true) {
           @Override
           public String toString() {
               return "repositoryType";
           }
       },
       STATUS(false){
           @Override
           public String toString() {
               return "status";
           }
       },
       UPDATED_DATE(false) {
          	@Override
            public String toString() {
                return "updatedDate";
            }
        };

    	private boolean internalUseOnly;

		public boolean isInternalUseOnly() {
			return internalUseOnly;
		}
		
		public static PropertyKey getPropertyKey(String Pn) {
			if (Pn!=null)
				for (PropertyKey p : PropertyKey.values()) {
					if (PnIdentifier.stripQuotes(Pn).equals(p.toString())) {
						return p;
					}
				}
			return null;
		}
		
		public String getPropertyName() {
			if (isInternalUseOnly()) {
				return this.toString();
			} else {
				return PnIdentifier.getQuotedIdentifer(this.toString());
			}
		}

		public void setInternalUseOnly(boolean internalUseOnly) {
			this.internalUseOnly = internalUseOnly;
		}
		
		private PropertyKey(boolean isInternal) {
			setInternalUseOnly(isInternal);
		}		
    	
   }
