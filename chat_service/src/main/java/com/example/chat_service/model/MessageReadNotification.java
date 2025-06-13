package com.example.chat_service.model;

public class MessageReadNotification {
    private String userId;
    private String seen;

    public MessageReadNotification(String userId, String seen){
        this.userId = userId;
        this.seen = seen;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

}
