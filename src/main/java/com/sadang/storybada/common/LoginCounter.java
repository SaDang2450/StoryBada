package com.sadang.storybada.common;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoginCounter implements HttpSessionListener {

    private static final AtomicInteger sessionCounter = new AtomicInteger();

    public static void increment() {
        sessionCounter.incrementAndGet();
    }

    public static void decrement() {
        sessionCounter.decrementAndGet();
    }

    public static int getSessionCounter() {
        return sessionCounter.get();
    }
}
