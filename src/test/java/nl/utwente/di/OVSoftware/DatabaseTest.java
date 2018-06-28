package nl.utwente.di.OVSoftware;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    @Test
    void getAllPayrates() {
        assertNotNull(Database.getAllPayrates(Database.mainDatabase));
    }

    @Test
    void allEmployees() {
        assertNotNull(Database.getEmployees(Database.mainDatabase));
    }

    @Test
    void getPayratesSpecificEmployee() {
        assertNotNull(Database.getPayratesSpecificEmployee(4, Database.mainDatabase));
    }

    @Test
    void searchEmployees() {
        assertNotNull(Database.searchEmployees(4,"MaSt", "A", Database.mainDatabase));
    }

}