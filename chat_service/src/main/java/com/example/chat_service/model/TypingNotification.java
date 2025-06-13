package com.example.chat_service.model;

public class TypingNotification {
    private String userId;
    private String typing;

    public TypingNotification(String userId, String typing){
        this.userId = userId;
        this.typing = typing;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTyping() {
        return typing;
    }

    public void setTyping(String typing) {
        this.typing = typing;
    }
}
