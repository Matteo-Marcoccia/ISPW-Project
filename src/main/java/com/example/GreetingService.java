package com.example;

public class GreetingService {
    
    public String getGreeting() {
        return getGreeting(System.getProperty("java.version"));
    }

    public String getGreeting(String javaVersion) {
        if (javaVersion == null) javaVersion = "unknown";
        return "Hello World! Running on Java " + javaVersion + ".";
    }
}