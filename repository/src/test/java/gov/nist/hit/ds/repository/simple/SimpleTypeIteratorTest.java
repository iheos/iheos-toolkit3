package gov.nist.hit.ds.repository.simple;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.Type;
import gov.nist.hit.ds.repository.api.TypeIterator;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleTypeIterator;
import org.junit.Test;

public class SimpleTypeIteratorTest {
	
	
	@Test
	public void simpleTypeIteratorTest() throws RepositoryException {
		
		System.out.println(" types path: " + Configuration.getRepositoryTypesDir(Configuration.getRepositorySrc(Access.RW_EXTERNAL)));
		
		TypeIterator it = new SimpleTypeIterator(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		assertTrue("initially the iterator should have hasNextType() return true", it.hasNextType());
		
		Type nextType = it.nextType();
		
		assertNotNull("nextType() should return an object, not null", nextType);
		
		Type nextType2 = it.nextType();
		
		assertFalse("Two returned types should not be equal", nextType.isEqual(nextType2));
	}
	 
	@Test
	public void managerIteratorTest() throws RepositoryException {
		TypeIterator ti = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL)).getRepositoryTypes();
		assertTrue("initially the iterator should have hasNextType() return true", ti.hasNextType());
		
		Type nextType = ti.nextType();
		
		assertNotNull("nextType() should return an object, not null", nextType);
		
		Type nextType2 = ti.nextType();
		
		assertFalse("Two returned types should not be equal", nextType.isEqual(nextType2));
	}
	
	
}
