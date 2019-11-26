package com.ap.blog.markdown.processors;

import com.ap.blog.markdown.MarkdownContent;

public interface MarkdownProcessor {
    String convertToHtml(MarkdownContent content);
}
