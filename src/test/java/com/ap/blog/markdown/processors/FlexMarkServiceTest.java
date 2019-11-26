package com.ap.blog.markdown.processors;

import com.ap.blog.model.Blog;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ExtendWith(MockitoExtension.class)
class FlexMarkServiceTest {

    private FlexMarkService subject = new FlexMarkService();

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