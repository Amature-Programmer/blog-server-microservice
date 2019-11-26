package com.ap.blog.events.blogs;

public enum FetchType {
    JSON("json"),
    HTML("html"),
    PDF("pdf"),
    MARKDOWN("md");

    private final String type;

    FetchType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
