package com.example.chat_service.model;

public class ChatMessage {
    private String text;
    private String isSentByCurrentUser;
    private String userId;

    public ChatMessage(String text, String userId){
        this.text = text;
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIsSentByCurrentUser() {
        return isSentByCurrentUser;
    }

    public void setIsSentByCurrentUser(String isSentByCurrentUser) {
        this.isSentByCurrentUser = isSentByCurrentUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "text='" + text + '\'' +
                ", isSentByCurrentUser='" + isSentByCurrentUser + '\'' +
                ", user ID='" + userId + '\'' +
                '}';
    }
}
