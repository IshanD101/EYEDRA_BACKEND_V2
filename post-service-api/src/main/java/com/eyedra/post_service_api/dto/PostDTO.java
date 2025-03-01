package com.eyedra.post_service_api.dto;

public class PostDTO {
    private String title;
    private String content;
    private String author;
    private String userId;
    private String imagePath; // Added field for image path


    // Getters and Setters
    public String getImagePath() { // Getter for image path
        return imagePath;
    }

    public void setImagePath(String imagePath) { // Setter for image path
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
