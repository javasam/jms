package com.example.jmsclient.service;

import com.example.jmsclient.dto.MessageDTO;

public interface JmsSender {
    String sendAndReceiveMessage(String requestQueue, String responseQueue, MessageDTO messageDTO);
}
