package com.example.sbmobile;

public class Topic {
    private String topicId;
    private String title;
    private String description;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private String timestamp; // Fecha y hora del post
    private String username;  // Nombre del usuario que realiza el post

    // Constructor vacío necesario para Firebase
    public Topic() {
    }

    // Constructor con todos los campos
    public Topic(String title, String description, String imageUrl, double latitude, double longitude, String timestamp, String username) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.username = username;
    }

    // Getters y setters
    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
