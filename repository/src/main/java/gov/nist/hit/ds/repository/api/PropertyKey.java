/**
 * 
 */
package gov.nist.hit.ds.repository.api;

/**
 * @author Sunil.Bhaskarla
 *
 */

public enum PropertyKey {
       UNIDENTIFIED(){},
       
       ASSET_ID(){
           @Override
           public String toString() {
               return "id";
           }
       },
       ASSET_TYPE(){
           @Override
           public String toString() {
               return "type";
           }
       },	        
       CREATED_DATE(){
           @Override
           public String toString() {
               return "createdDate";
           }
       },
       DESCRIPTION() {	        	
           @Override
           public String toString() {
               return "description";
           }	        	
       },
       DISPLAY_NAME() {	        	
           @Override
           public String toString() {
               return "displayName";
           }	        	
       },
       DISPLAY_ORDER(){
           @Override
           public String toString() {
               return "displayOrder";
           }
       },	        
       EXPIRATION_DATE(){
           @Override
           public String toString() {
               return "expirationDate";
           }
       },
       MIME_TYPE(){
           @Override
           public String toString() {
               return "mimeType";
           }
       },
       UPDATED_DATE() {
       	@Override
           public String toString() {
               return "modifiedDate";
           }
       },
       PARENT_ID(){
           @Override
           public String toString() {
               return "parentId";
           }
       },
       REPOSITORY_ID(){
           @Override
           public String toString() {
               return "repositoryId";
           }
       },
       STATUS(){
           @Override
           public String toString() {
               return "status";
           }
       };

   }
