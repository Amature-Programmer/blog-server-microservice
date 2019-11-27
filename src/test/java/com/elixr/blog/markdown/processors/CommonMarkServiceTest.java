package com.elixr.blog.markdown.processors;

import com.elixr.blog.model.Blog;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class CommonMarkServiceTest {

    private CommonMarkService subject = new CommonMarkService();

    @Test
    void convertToHtml() {
        final String title = "Hello World";
        final String content = "# Hello World";
        final Blog blog = Blog.builder().title(title).content(content).id(UUID.randomUUID()).build();

        String html = subject.convertToHtml(blog);
        log.info(html);

        assertAll(
                () -> assertNotNull(html),
                () -> assertThat(html).containsSequence("<h1>").containsSequence("Hello World").containsSequence("</h1>")
        );
    }
}