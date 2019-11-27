package com.elixr.blog.markdown.processors;

import com.elixr.blog.markdown.MarkdownContent;
import org.commonmark.Extension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CommonMarkService implements MarkdownProcessor {

    private final Parser parser;
    private final HtmlRenderer renderer;

    CommonMarkService() {
        parser = Parser.builder()
                .extensions(this.getExtensions())
                .build();
        renderer = HtmlRenderer.builder().build();
    }

    private List<Extension> getExtensions() {
        return Arrays.asList();
    }

    @Override
    public String convertToHtml(MarkdownContent content) {
        final Node node = parser.parse(content.getMarkdownContent());
        return renderer.render(node);
    }
}
