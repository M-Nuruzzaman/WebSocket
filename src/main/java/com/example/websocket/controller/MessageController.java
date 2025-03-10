package com.example.websocket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.websocket.handler.WebSocketHandler;

import java.io.IOException;

@RestController
public class MessageController {

    @Autowired
    private WebSocketHandler webSocketHandler;

    // Endpoint to send message to all clients
    @GetMapping("/sendMessage")
    public String sendMessageToAll(@RequestParam String message) {
        // Logic to send message to all clients
        webSocketHandler.broadcastMessage(message);
        return "Message sent to all clients!";
    }

    // Endpoint to send message to a specific client by userId
    @GetMapping("/sendMessageToUser")
    public String sendMessageToUser(@RequestParam String userId, @RequestParam String message) throws IOException {
        // Logic to send a message to a specific user
        webSocketHandler.sendMessageToUser(userId, message);
        return "Message sent to user with ID: " + userId;
    }
}
