package nl.utwente.di.OVSoftware;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class OVAccountTest {
	
	private OVAccount acc = new OVAccount("a", "123");
	
	@BeforeEach
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
