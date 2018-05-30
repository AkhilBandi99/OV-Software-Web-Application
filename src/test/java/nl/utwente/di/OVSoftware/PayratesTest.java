package nl.utwente.di.OVSoftware;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PayratesTest {
    private Payrates payrates;

    @BeforeEach
    void setUp() {
        payrates = new Payrates(1337,523.014,"01.01.2018","01.01.2019");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getId() {
        assertEquals(1337,payrates.getId());
    }

    @Test
    void getCost() {
        assertEquals(523.014,payrates.getCost());
    }

    @Test
    void getStartDate() {
        assertEquals("01.01.2018",payrates.getStartDate());
    }

    @Test
    void getEndDate() {
        assertEquals("01.01.2019",payrates.getEndDate());
    }
}