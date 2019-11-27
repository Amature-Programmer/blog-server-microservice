package com.elixr.blog.events.blogs;

import org.springframework.http.MediaType;

public enum FetchType {
    JSON("json", MediaType.APPLICATION_JSON),
    HTML("html", MediaType.TEXT_HTML),
    PDF("pdf", MediaType.APPLICATION_PDF),
    MARKDOWN("md", MediaType.TEXT_MARKDOWN);

    private final String type;
    private final MediaType mediaType;

    FetchType(String type, MediaType mediaType) {
        this.type = type;
        this.mediaType = mediaType;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
