package nl.utwente.di.OVSoftware;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class userMapsTest {

    private  userMaps userMaps;

    @BeforeEach
    void setUp() {
        userMaps = new userMaps();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void newUser() {
        assertFalse(userMaps.findUser("b","b"));
        userMaps.newUser("b","b");
        assertFalse(userMaps.findUser("b","b"));
    }

    @Test
    void findUser() {
        assertTrue(userMaps.findUser("a", "a"));
        assertFalse(userMaps.findUser("b","b"));
    }
}