package nl.utwente.di.OVSoftware;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    private Employee employee;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        employee = new Employee(1337,"Hans","A");
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void getId() {
        assertEquals(1337,employee.getId());
    }

    @org.junit.jupiter.api.Test
    void getName() {
        assertEquals("Hans",employee.getName());
    }

    @org.junit.jupiter.api.Test
    void getStatus() {
        assertEquals("A", employee.getStatus());
    }

    @org.junit.jupiter.api.Test
    void addPayrates() {
        //TODO
    }

    @org.junit.jupiter.api.Test
    void getPayrates() {
        //TODO
    }

    @org.junit.jupiter.api.Test
    void toStringTest() {
        assertEquals("1337 Hans A",employee.toString());
    }
}