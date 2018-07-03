package nl.utwente.di.OVSoftware;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

class DatabaseTest {
	private Employee employee;
	private Payrates payrates;
	private Payrates invalidpayrate;
	private List<Payrates> list = new ArrayList<Payrates>();
	
	@BeforeEach
	void setup() throws ParseException {
		employee = new Employee(9999, "AkBa", "A");
		payrates = new Payrates(employee.getId(), 90, "01-01-2018", "31-12-2018");
		invalidpayrate = new Payrates(employee.getId(), 90, "01-01-2018", "31-12-2017");
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

    @Test
    void getAllPayrates() {
        assertNotNull(Database.getAllPayrates(Database.mainDatabase));
    }
    
    @Test
    void getPayratesSpecificEmployee() {
        assertNotNull(Database.getPayratesSpecificEmployee(4, Database.mainDatabase));
    }

    @Test
    void allEmployees() {
        assertNotNull(Database.getEmployees(Database.mainDatabase));
    }
    
    @Test
    void getGoogleAccounts() {
    	assertNotNull(Database.getGoogleAccounts());
    }
    
    @Test
    void googleAccountAccepted() {
    	assertTrue(Database.googleAccountAccepted("s.s.a.bandi@student.utwente.nl"));
    	assertFalse(Database.googleAccountAccepted("invalid@gmail.com"));
    }
    
    @Test
    void getOVAccounts() {
    	assertNotNull(Database.getOVAccounts());
    }
    
    @Test
    void OVAccountAccepted() {
    	assertTrue(Database.OVAccountAccepted("a", "a"));
    	assertFalse(Database.OVAccountAccepted("invalid", "invalid"));
    }
    
    @Test
    void createGoogleAccount() throws ClassNotFoundException, SQLException {
    	Database.createGoogleAccount("newEmail@gmail.com");
    	assertTrue(Database.googleAccountAccepted("newEmail@gmail.com"));
    }
    
    @Test
    void createOVAccount() throws ClassNotFoundException, SQLException {
    	Database.createOVAccount("new", "new");
    	assertTrue(Database.OVAccountAccepted("new", "new"));
    }
    
    @Test
    void deleteGoogleAccount() throws ClassNotFoundException, SQLException {
    	Database.deleteGoogleAccount("newEmail@gmail.com");
    	assertFalse(Database.googleAccountAccepted("newEmail@gmail.com"));
    }
    
    @Test
    void deleteOVAccount() throws ClassNotFoundException, SQLException {
    	Database.deleteOVAccount("new");
    	assertFalse(Database.OVAccountAccepted("new", "new"));
    }
    
    @Test
    void addPayrates() throws ParseException {
    	list.add(invalidpayrate);
    	Database.editPayrates(employee.getId(), list, Database.mainDatabase);
    	thrown.expect(SQLException.class);
    	list.remove(invalidpayrate);
    	list.add(payrates);
    	Database.editPayrates(employee.getId(), list, Database.mainDatabase);
    	list.remove(payrates);
    }
    
    @Test
    void importPayrates() {
		list.add(invalidpayrate);
		Database.importPayrts(list, Database.mainDatabase);
		thrown.expect(SQLException.class);
		list.remove(invalidpayrate);
    }

    @Test
    void searchFilterSortEmployees() {
        assertNotNull(Database.searchEmployees(4, "MaSt", "A", 10, Database.mainDatabase));
        assertNotNull(Database.searchEmployees(4, "MaSt", "A", -1, Database.mainDatabase));
        assertNotNull(Database.searchEmployees(4, "MaSt", "-1", -1, Database.mainDatabase));
        assertNotNull(Database.searchEmployees(1, "-1", "-1", -1, Database.mainDatabase));
        assertNotNull(Database.searchEmployees(-1, "j", "A", 11, Database.mainDatabase));
        assertNotNull(Database.searchEmployees(-1, "-1", "I", 00, Database.mainDatabase));
        assertNotNull(Database.searchEmployees(-1, "-1", "-1", 00, Database.mainDatabase));
        assertNotNull(Database.searchEmployees(-1, "MaSt", "-1", 00, Database.mainDatabase));
    }

}