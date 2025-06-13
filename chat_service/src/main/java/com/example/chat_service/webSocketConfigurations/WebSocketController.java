package com.example.chat_service.webSocketConfigurations;

import com.example.chat_service.model.ChatMessage;
import com.example.chat_service.model.MessageReadNotification;
import com.example.chat_service.model.TypingNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // client => admin
    @MessageMapping("/send-message-to-admin")
    public void sendMessageToAdmin(ChatMessage message) {
        ChatMessage messageToSend = new ChatMessage(message.getText(), message.getUserId());
        String jsonMessage = null;
        try {
            jsonMessage = new ObjectMapper().writeValueAsString(messageToSend);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        messagingTemplate.convertAndSend("/topic/admin", jsonMessage);

        System.out.println(message);
    }

    // admin => client specific
    @MessageMapping("/send-message-to-client")
    public void sendMessageToClient(ChatMessage message) {
        ChatMessage messageToSend = new ChatMessage(message.getText(), message.getUserId());
        String jsonMessage = null;
        try {
            jsonMessage = new ObjectMapper().writeValueAsString(messageToSend);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        messagingTemplate.convertAndSend("/topic/client/" + message.getUserId(), jsonMessage);

        System.out.println(message);
    }

    @MessageMapping("/user-typing")
    public void handleUserTyping(TypingNotification notification) {
        messagingTemplate.convertAndSend("/topic/user-typing/" + notification.getUserId(), notification);
        System.out.println("User: " + notification.getUserId() + "is typing: " + notification.getTyping());
    }

    @MessageMapping("/admin-typing")
    public void handleAdminTyping(TypingNotification notification) {
        messagingTemplate.convertAndSend("/topic/admin-typing/" + notification.getUserId(), notification);
        System.out.println("User-Admin: " + notification.getUserId() + "is typing: " + notification.getTyping());
    }

    @MessageMapping("/mark-message-as-read-admin-to-client")
    public void markMessageAsReadAdminToClient(MessageReadNotification notification) {
        messagingTemplate.convertAndSend("/topic/message-read-in-client/" + notification.getUserId(), notification);
        System.out.println("User: " + notification.getUserId() + "a citit mesajul: " + notification.getSeen());
    }

    @MessageMapping("/mark-message-as-read-client-to-admin")
    public void markMessageAsReadToAdmin(MessageReadNotification notification) {
        messagingTemplate.convertAndSend("/topic/message-read-in-admin/" + notification.getUserId(), notification);
        System.out.println("User-Admin: " + notification.getUserId() + "a citit mesajul: " + notification.getSeen());
    }



}
