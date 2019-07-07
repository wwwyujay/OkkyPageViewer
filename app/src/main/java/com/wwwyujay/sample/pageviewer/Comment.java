package com.wwwyujay.sample.pageviewer;

public class Comment {
    
    private String writer;   // Writer of the comment
    private String createdAt;   // When the comment was created
    private String detail;  // Content of the comment
    private int level;  // How deep is the comment (If the comment is a reply to another comment, then the level of the comments get 1 point higher.)

    public Comment(String writer, String createdAt, String detail) {
        this.writer = writer;
        this.createdAt = createdAt;
        this.detail = detail;
    }

    public Comment(String writer, String createdAt, String detail, int level) {
        this.writer = writer;
        this.createdAt = createdAt;
        this.detail = detail;
        this.level = level;
    }

    /* Getters and Setters */
    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
