package com.elixr.blog.markdown.processors;

import com.elixr.blog.markdown.MarkdownContent;

public interface MarkdownProcessor {
    String convertToHtml(MarkdownContent content);
}
