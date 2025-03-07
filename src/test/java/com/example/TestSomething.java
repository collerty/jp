package test.java.com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestSomething {

    @BeforeEach
    public void setUp() {
        // setup code here
    }

    @Test
    public void testPass() {
        assertTrue(true);
    }

    @Test
    public void testFail() {
        fail();
    }
}
