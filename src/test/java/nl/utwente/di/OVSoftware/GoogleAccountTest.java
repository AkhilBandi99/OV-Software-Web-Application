package nl.utwente.di.OVSoftware;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class GoogleAccountTest {
	
	private GoogleAccount acc = new GoogleAccount("group8@student.utwente.nl");
	
	@BeforeEach
	public void setUp() {
		
	}
	
	@Test
	public void testUsernameAndPassword() {
		assertNotNull(acc.getEmail());
		assertTrue(acc.getEmail().equals("group8@student.utwente.nl"));
	}
	
}
