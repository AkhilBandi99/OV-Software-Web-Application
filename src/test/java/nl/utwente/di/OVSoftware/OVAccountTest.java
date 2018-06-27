package nl.utwente.di.OVSoftware;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

public class OVAccountTest {
	
	private OVAccount acc = new OVAccount("a", "123");
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testUsernameAndPassword() {
		assertNotNull(acc.getUsername());
		assertNotNull(acc.getPassword());
		assertTrue(acc.getUsername().equals("a"));
		assertTrue(acc.getPassword().equals("123"));
	}
	
}
