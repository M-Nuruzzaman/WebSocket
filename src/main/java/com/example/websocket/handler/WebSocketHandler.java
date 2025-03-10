package com.example.websocket.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    // List to hold all the WebSocket sessions
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Adding New Session: " + session.getId());
        sessions.add(session); // Add the session to the list when it's established
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message; // Cast the message to TextMessage
            System.out.println("Received message: " + textMessage.getPayload());
            broadcastMessage("Server: " + textMessage.getPayload()); // Broadcast the message to all clients
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Handle any errors that occur during the WebSocket communication
        System.out.println("Error with WebSocket session: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session); // Remove the session from the list when it's closed
    }

    @Override
    public boolean supportsPartialMessages() {
        return false; // You can implement partial message support if needed
    }

    // Method to broadcast a message to all connected clients
    public void broadcastMessage(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message)); // Send the message to each session
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
