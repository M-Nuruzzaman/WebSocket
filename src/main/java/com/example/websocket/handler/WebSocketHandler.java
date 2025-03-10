package com.example.websocket.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    // Map to store the user ID and the corresponding WebSocket session
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // This method is invoked when a new WebSocket connection is established
        System.out.println("New connection established, sessionId: " + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();

        // Check if the message is a user registration message
        if (payload.startsWith("REGISTER:")) {
            String userId = payload.substring("REGISTER:".length());
            userSessions.put(userId, session); // Store the userId and session in the map
            System.out.println("User registered with ID: " + userId);
        } else {
            // Handle other messages
            broadcastMessage(payload);
            System.out.println("Received message: " + payload);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Handle any errors that occur during the WebSocket communication
        System.out.println("Error with WebSocket session: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // Clean up the map when the connection is closed
        userSessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
        System.out.println("Connection closed, sessionId: " + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false; // You can implement partial message support if needed
    }

//     Method to broadcast a message to all connected clients
    public void broadcastMessage(String message) {
        for (WebSocketSession session : userSessions.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message)); // Send the message to each session
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to send a message to a specific user by their userId
    public void sendMessageToUser(String userId, String message) throws IOException {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message)); // Send message to the specific user
        } else {
            System.out.println("User session not found or closed for userId: " + userId);
        }
    }

}
