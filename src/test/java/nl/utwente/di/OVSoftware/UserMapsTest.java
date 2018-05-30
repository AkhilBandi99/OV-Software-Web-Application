package nl.utwente.di.OVSoftware;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapsTest {

    private UserMaps UserMaps;

    @BeforeEach
    void setUp() {
        UserMaps = new UserMaps();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void newUser() {
        assertFalse(UserMaps.findUser("b","b"));
        UserMaps.newUser("b","b");
        assertFalse(UserMaps.findUser("b","b"));
    }

    @Test
    void findUser() {
        assertTrue(UserMaps.findUser("a", "a"));
        assertFalse(UserMaps.findUser("b","b"));
    }
}