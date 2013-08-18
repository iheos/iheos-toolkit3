package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.api.TypeIterator;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleTypeIterator;
import gov.nist.hit.ds.repository.simple.index.MockServletContext;

import java.io.File;

import javax.servlet.ServletContext;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleTypeIteratorTest {
	
	
	@Test
	public void simpleTypeIteratorTest() throws RepositoryException {
		
		System.out.println(" types path: " + Configuration.getRepositoryTypesDir().getPath());
		
		TypeIterator it = new SimpleTypeIterator();
		assertTrue("initially the iterator should have hasNextType() return true", it.hasNextType());
		
		Type nextType = it.nextType();
		
		assertNotNull("nextType() should return an object, not null", nextType);
		
		Type nextType2 = it.nextType();
		
		assertFalse("Two returned types should not be equal", nextType.isEqual(nextType2));
	}
	 
	@Test
	public void managerIteratorTest() throws RepositoryException {
		TypeIterator ti = new RepositoryFactory().getRepositoryTypes();
		assertTrue("initially the iterator should have hasNextType() return true", ti.hasNextType());
		
		Type nextType = ti.nextType();
		
		assertNotNull("nextType() should return an object, not null", nextType);
		
		Type nextType2 = ti.nextType();
		
		assertFalse("Two returned types should not be equal", nextType.isEqual(nextType2));
	}
	
	
}
