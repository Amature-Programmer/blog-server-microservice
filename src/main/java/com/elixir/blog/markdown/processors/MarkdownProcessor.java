package com.elixir.blog.markdown.processors;

import com.elixir.blog.markdown.MarkdownContent;

public interface MarkdownProcessor {
    String convertToHtml(MarkdownContent content);
}
