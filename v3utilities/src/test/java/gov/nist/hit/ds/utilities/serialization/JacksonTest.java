package gov.nist.hit.ds.utilities.serialization;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class JacksonTest {

	@Test
	public void jacksonTest() {
		User user = new User();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	 
		try {
	 
			// convert user object to json string, and save to a file
			mapper.writeValue(baos, user);
	 
			// display to console
			System.out.println(mapper.writeValueAsString(user));
	 
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			fail();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		try {
			
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			 
			// read from file, convert it to user class
			User user2 = mapper.readValue(bais, User.class);
	 
			// display to console
			System.out.println(user2);
	 
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			fail();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

	}
}
