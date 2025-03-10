package com.example.websocket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.websocket.handler.WebSocketHandler;

@RestController
public class MessageController {

    @Autowired
    private WebSocketHandler webSocketHandler;

    @GetMapping("/sendMessage")
    public String sendMessageToClient(String message) {
        // Logic to send message to frontend
        // You can create a method in WebSocketHandler to send message to all or specific sessions
        webSocketHandler.broadcastMessage(message);
        return "Message sent!";
    }
}
