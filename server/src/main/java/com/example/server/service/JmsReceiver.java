package com.example.server.service;

public interface JmsReceiver {
    void receiveAndSend(String requestQueue, String responseQueue);
}
