package com.example.sbmobile;

public class Comment {
    private String userId;
    private String userEmail;
    private String message;
    private long timestamp;

    public Comment() {
    }

    public Comment(String userId, String userEmail, String message, long timestamp) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

