package com.ap.blog.markdown.processors;

import com.ap.blog.markdown.MarkdownContent;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class FlexMarkService implements MarkdownProcessor {
    private final MutableDataSet options;
    private final Parser parser;
    private final HtmlRenderer renderer;

    FlexMarkService() {
        this.options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        this.parser = Parser.builder(options).build();
        this.renderer = HtmlRenderer.builder(options).build();
    }

    @Override
    public String convertToHtml(MarkdownContent content) {
        final Node document = this.parser.parse(content.getMarkdownContent());
        return renderer.render(document);
    }
}
