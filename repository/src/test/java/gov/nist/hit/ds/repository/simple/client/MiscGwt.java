package gov.nist.hit.ds.repository.simple.client;



import com.google.gwt.junit.client.GWTTestCase;

public class MiscGwt extends GWTTestCase {

    // private DefaultUIManager uiManager;
    
	@Override
	public String getModuleName() {
		return "gov.nist.hit.ds.repository.simple.Simple";
	}
	

    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();

       // uiManager = new DefaultUIManager(true);
    }
    
	public void test1() {
		
		String rawJsonStr = 
				
			//	"[" +				
				"{\"configs\":[{\"actorType\":\"REGISTRY\",\"expires\":1411489856212,\"isExpired\":false,\"elements\":[{\"name\":\"Creation Time\",\"type\":\"TIME\",\"value\":\"Mon Sep 23 12:30:56 EDT 2013\",\"editable\":false},{\"name\":\"Name\",\"type\":\"TEXT\",\"value\":\"Private\",\"editable\":true},{\"name\":\"Update_Metadata_Option\",\"type\":\"BOOLEAN\",\"value\":\"true\",\"editable\":true},{\"name\":\"Extra_Metadata_Supported\",\"type\":\"BOOLEAN\",\"value\":\"true\",\"editable\":true},{\"name\":\"Codes_Environment\",\"type\":\"TEXT\",\"value\":\"C:\\\\Users\\\\skb1\\\\Documents\\\\toolkit\\\\registrySim\\\\target\\\\test-classes\\\\external_cache\\\\environment\\\\EURO2012\\\\codes.xml\",\"editable\":true},{\"name\":\"Register\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/r.b\",\"editable\":true},{\"name\":\"Stored Query\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/sq.b\",\"editable\":true},{\"name\":\"Update\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/update.b\",\"editable\":true},{\"name\":\"Register_TLS\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/r.b\",\"editable\":true},{\"name\":\"Stored Query_TLS\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/sq.b\",\"editable\":true},{\"name\":\"Update_TLS\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/update.b\",\"editable\":true},{\"name\":\"Register_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/r.b\",\"editable\":true},{\"name\":\"Stored Query_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/sq.b\",\"editable\":true},{\"name\":\"Update_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/update.b\",\"editable\":true},{\"name\":\"Register_TLS_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/r.b\",\"editable\":true},{\"name\":\"Stored Query_TLS_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/sq.b\",\"editable\":true},{\"name\":\"Update_TLS_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/update.b\",\"editable\":true}],\"all\":[{\"name\":\"Creation Time\",\"type\":\"TIME\",\"value\":\"Mon Sep 23 12:30:56 EDT 2013\",\"editable\":false},{\"name\":\"Name\",\"type\":\"TEXT\",\"value\":\"Private\",\"editable\":true},{\"name\":\"Update_Metadata_Option\",\"type\":\"BOOLEAN\",\"value\":\"true\",\"editable\":true},{\"name\":\"Extra_Metadata_Supported\",\"type\":\"BOOLEAN\",\"value\":\"true\",\"editable\":true},{\"name\":\"Codes_Environment\",\"type\":\"TEXT\",\"value\":\"C:\\\\Users\\\\skb1\\\\Documents\\\\toolkit\\\\registrySim\\\\target\\\\test-classes\\\\external_cache\\\\environment\\\\EURO2012\\\\codes.xml\",\"editable\":true},{\"name\":\"Register\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/r.b\",\"editable\":true},{\"name\":\"Stored Query\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/sq.b\",\"editable\":true},{\"name\":\"Update\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/update.b\",\"editable\":true},{\"name\":\"Register_TLS\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/r.b\",\"editable\":true},{\"name\":\"Stored Query_TLS\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/sq.b\",\"editable\":true},{\"name\":\"Update_TLS\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/update.b\",\"editable\":true},{\"name\":\"Register_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/r.b\",\"editable\":true},{\"name\":\"Stored Query_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/sq.b\",\"editable\":true},{\"name\":\"Update_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/update.b\",\"editable\":true},{\"name\":\"Register_TLS_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/r.b\",\"editable\":true},{\"name\":\"Stored Query_TLS_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/sq.b\",\"editable\":true},{\"name\":\"Update_TLS_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/update.b\",\"editable\":true}],\"expiration\":1411489856212,\"expired\":false,\"fixed\":[{\"name\":\"Creation Time\",\"type\":\"TIME\",\"value\":\"Mon Sep 23 12:30:56 EDT 2013\",\"editable\":false}],\"editable\":[{\"name\":\"Name\",\"type\":\"TEXT\",\"value\":\"Private\",\"editable\":true},{\"name\":\"Update_Metadata_Option\",\"type\":\"BOOLEAN\",\"value\":\"true\",\"editable\":true},{\"name\":\"Extra_Metadata_Supported\",\"type\":\"BOOLEAN\",\"value\":\"true\",\"editable\":true},{\"name\":\"Codes_Environment\",\"type\":\"TEXT\",\"value\":\"C:\\\\Users\\\\skb1\\\\Documents\\\\toolkit\\\\registrySim\\\\target\\\\test-classes\\\\external_cache\\\\environment\\\\EURO2012\\\\codes.xml\",\"editable\":true},{\"name\":\"Register\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/r.b\",\"editable\":true},{\"name\":\"Stored Query\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/sq.b\",\"editable\":true},{\"name\":\"Update\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/update.b\",\"editable\":true},{\"name\":\"Register_TLS\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/r.b\",\"editable\":true},{\"name\":\"Stored Query_TLS\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/sq.b\",\"editable\":true},{\"name\":\"Update_TLS\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/update.b\",\"editable\":true},{\"name\":\"Register_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/r.b\",\"editable\":true},{\"name\":\"Stored Query_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/sq.b\",\"editable\":true},{\"name\":\"Update_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"http://localhost:8080/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/update.b\",\"editable\":true},{\"name\":\"Register_TLS_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/r.b\",\"editable\":true},{\"name\":\"Stored Query_TLS_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/sq.b\",\"editable\":true},{\"name\":\"Update_TLS_ASYNC\",\"type\":\"ENDPOINT\",\"value\":\"https://localhost:8443/xdstools3/sim/0c11a221-4664-4251-9a39-e4cd667cd63c/reg/update.b\",\"editable\":true}]}],\"simId\":{\"id\":\"0c11a221-4664-4251-9a39-e4cd667cd63c\"},\"id\":{\"id\":\"0c11a221-4664-4251-9a39-e4cd667cd63c\"}}" 
				;
			//	"]";
				
				/*
				"[\r\n" + 
				"  {\r\n" + 
				"    \"symbol\": \"ABC\",\r\n" + 
				"    \"price\": 96.204659543522,\r\n" + 
				"    \"change\": -1.6047997669492\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"symbol\": \"DEF\",\r\n" + 
				"    \"price\": 61.929176899084,\r\n" + 
				"    \"change\": 0.22809544419493\r\n" + 
				"  }\r\n" + 
				"]";
				*/
		
		try {
			System.out.println(rawJsonStr);
			
			// JSONValue jsonValue = JSONParser.parseStrict(rawJsonStr);

	        /*
	         * JSONArray 	
Represents an array of JSONValue objects.
JSONBoolean 	
Represents a JSON boolean value.
JSONNull 	
Represents the JSON null value.
JSONNumber 	
Represents a JSON number.
JSONObject 	
Represents a JSON object.
JSONParser 	
Parses the string representation of a JSON object into a set of JSONValue-derived objects.
JSONString 	
Represents a JSON string.
JSONValue 	
The superclass of all JSON value types.
	         */

					} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		
	}

//	public String processJson(JSONValue jsonValue) {
//
//		String fmtStr = "{";
//		
//        JSONArray jsonArray = jsonValue.isArray();
//        JSONBoolean jsonBoolean = jsonValue.isBoolean();
//        JSONNull jsonNull = jsonValue.isNull();
//        JSONNumber jsonNumber = jsonValue.isNumber();
//        JSONObject jsonObject = jsonValue.isObject();
//        JSONString jsonString = jsonValue.isString();
//        
//    
//        // get first non-null value
//        
//        if (jsonObject!=null) {
//        	for (String s : jsonObject.keySet()) {
//        		JSONValue val =  jsonObject.get(s);
//        		fmtStr += processJson(val);
//        	}
//        }
//        
//        if (jsonArray != null) {
//        	 for (int i=0; i<jsonArray.size(); i++) {
//        		 
//        		 for (JSONValue)
//        		 
//        		 JSONString jsStr = jsonArray.get(i).isString();
//        		 
//        		 System.out.println(jsStr.toString());
//        		 
//        	 }
//
//          } else {
//            fail("parser failed"); 
//          }
//		
//
//	}

}
