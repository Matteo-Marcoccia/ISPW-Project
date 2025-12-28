package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GreetingServiceTest {

    @Test
    void testGreetingStandard() {
        GreetingService service = new GreetingService();
        String greeting = service.getGreeting();
        assertNotNull(greeting);
        assertTrue(greeting.contains("Hello World!"));
    }

    @Test
    void testGreetingWithNull() {
        GreetingService service = new GreetingService();
        String greeting = service.getGreeting(null);
        assertEquals("Hello World! Running on Java unknown.", greeting);
    }

    @Test
    void testGreetingWithCustomValue() {
        GreetingService service = new GreetingService();
        String greeting = service.getGreeting("21");
        assertEquals("Hello World! Running on Java 21.", greeting);
    }
}