package com.example;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class HelloWorldTest {

    @BeforeAll
    static void initJfx() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            latch.countDown();
        }
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("JavaFX Platform failed to start");
        }
    }

    @Test
    void testGreeting() {
        HelloWorld helloWorld = new HelloWorld();
        String greeting = helloWorld.getGreeting();
        
        assertNotNull(greeting);
        assertTrue(greeting.contains("Hello, JavaFX"));
    }

    @Test
    void testStart() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        // Usiamo AtomicReference per portare fuori le eccezioni o gli oggetti dal thread JavaFX
        AtomicReference<Throwable> error = new AtomicReference<>();
        AtomicReference<Stage> stageRef = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                HelloWorld helloWorld = new HelloWorld();
                Stage stage = new Stage();
                helloWorld.start(stage);
                stageRef.set(stage);
            } catch (Throwable t) {
                error.set(t);
            } finally {
                latch.countDown();
            }
        });

        // Aspettiamo che il thread JavaFX finisca
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Timeout waiting for start()");

        // Verifichiamo se ci sono stati errori
        if (error.get() != null) {
            fail("Exception in start(): " + error.get().getMessage());
        }

        // Ora possiamo fare asserzioni sull'oggetto Stage modificato
        // Nota: alcune proprietà di JavaFX potrebbero richiedere di essere lette nel thread FX,
        // ma verificare che l'oggetto non sia nullo va bene qui.
        Stage stage = stageRef.get();
        assertNotNull(stage, "Stage should not be null");
        
        // Per leggere la scena in sicurezza, meglio farlo nel thread FX, ma proviamo qui:
        Platform.runLater(() -> {
             assertNotNull(stage.getScene(), "Scene should be set");
        });
    }
}