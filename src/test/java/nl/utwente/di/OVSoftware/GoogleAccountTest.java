package nl.utwente.di.OVSoftware;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

public class GoogleAccountTest {
	
	private GoogleAccount acc = new GoogleAccount("group8@student.utwente.nl");
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testUsernameAndPassword() {
		assertNotNull(acc.getEmail());
		assertTrue(acc.getEmail().equals("group8@student.utwente.nl"));
	}
	
}
