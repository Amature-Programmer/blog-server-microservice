package com.elixr.blog.controller;

import com.elixr.blog.events.blogs.NewBlogEvent;
import com.elixr.blog.events.blogs.UpdateBlogEvent;
import com.elixr.blog.model.Blog;
import com.elixr.blog.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@AutoConfigureMockMvc
class BlogControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BlogService mockService;

    @InjectMocks
    private BlogController subject;

    @Test
    void contextLoads() {
        assertNotNull(subject);
        log.info("Blog Controller context loads");
    }

    @Test
    void postNewBlog() throws Exception {
        final String title = "Hello World";
        final String content = "# Hello World";

        final NewBlogEvent blogEvent = NewBlogEvent.builder().title(title)
                .content(content).build();

        MvcResult result = this.mvc.perform(
                post("/blog")
                        .content(objectMapper.writeValueAsString(blogEvent))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk()).andReturn();

        final Blog blog = objectMapper.readValue(result.getResponse().getContentAsString(), Blog.class);

        assertAll(
                () -> assertNotNull(blog),
                () -> assertEquals(blog.getMarkdownContent(), content),
                () -> assertNotNull(blog.getId()),
                () -> assertEquals(blog.getTitle(), title)
        );
    }

    @Test
    void postNewBlogFailsWhenNoTitle() throws Exception {
        final String content = "# Hello World";

        final NewBlogEvent blogEvent = NewBlogEvent.builder()
                .content(content).build();

        MvcResult result = this.mvc.perform(
                post("/blog")
                        .content(objectMapper.writeValueAsString(blogEvent))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is4xxClientError()).andReturn();
    }


    @Test
    void getBlogFailsForRandomBlog() throws Exception {
        final UUID uuid = UUID.randomUUID();
        this.mvc.perform(get("/blog/" + uuid.toString())).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getBlogAsDefaultJSON() throws Exception {
        final Blog blog = addBlog();

        MvcResult result = this.mvc.perform(
                get("/blog/" + blog.getId().toString()).accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().is2xxSuccessful()).andReturn();

        Blog bg = objectMapper.readValue(result.getResponse().getContentAsString(), Blog.class);
        assertAll(
                () -> assertNotNull(bg),
                () -> assertEquals(bg.getMarkdownContent(), blog.getContent()),
                () -> assertNotNull(bg.getId()),
                () -> assertEquals(bg.getTitle(), blog.getTitle())
        );
    }

    @Test
    void getBlogAsHtml() throws Exception {
        final Blog blog = addBlog();

        MvcResult result = this.mvc.perform(
                get("/blog/" + blog.getId().toString() + "?type=html").accept(MediaType.TEXT_HTML)
        ).andDo(print())
                .andExpect(status().is2xxSuccessful()).andReturn();
        assertThat(result.getResponse().getContentAsString())
                .containsSequence("<h1>")
                .containsSequence("Hello World")
                .containsSequence("</h1>");
    }

    @Test
    void getBlogAsMarkdown() throws Exception {
        final Blog blog = addBlog();

        MvcResult result = this.mvc.perform(
                get("/blog/" + blog.getId().toString() + "?type=md").accept(MediaType.TEXT_HTML)
        ).andDo(print())
                .andExpect(status().is2xxSuccessful()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("# Hello World");
    }

    @Test
    void renderBlog() throws Exception {
        final Blog blog = addBlog();

        MvcResult result = this.mvc.perform(
                get("/blog/" + blog.getId().toString() + "/render").accept(MediaType.TEXT_HTML_VALUE)
        ).andDo(print())
                .andExpect(status().is2xxSuccessful()).andReturn();

        assertThat(result.getResponse().getContentAsString())
                .containsSequence("<h1>")
                .containsSequence("Hello World")
                .containsSequence("</h1>");
    }

    @Test
    void updateExistingBlog() throws Exception {
        final Blog blog = addBlog();

        UpdateBlogEvent updateBlogEvent = new UpdateBlogEvent();
        updateBlogEvent.setTitle("First Blog!");

        MvcResult result = this.mvc.perform(
                patch("/blog/" + blog.getId().toString())
                        .content(objectMapper.writeValueAsString(updateBlogEvent))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn();

        Blog bg = objectMapper.readValue(result.getResponse().getContentAsString(), Blog.class);
        assertAll(
                () -> assertNotNull(bg),
                () -> assertEquals(bg.getMarkdownContent(), blog.getContent()),
                () -> assertNotNull(bg.getId()),
                () -> assertEquals(bg.getTitle(), updateBlogEvent.getTitle())
        );
    }

    @Test
    void postBlogFromFile() throws Exception {
        final String title = "Hello World";
        final String content = "# Hello World";

        MockMultipartFile multipartFile = new MockMultipartFile("content", "test.md",
                MediaType.TEXT_MARKDOWN_VALUE, content.getBytes());
        MvcResult result = this.mvc.perform(
                multipart("/blog/upload").file(multipartFile).param("title", title)
        ).andExpect(status().isOk()).andDo(print()).andReturn();

        Blog bg = objectMapper.readValue(result.getResponse().getContentAsString(), Blog.class);
        assertAll(
                () -> assertNotNull(bg),
                () -> assertEquals(bg.getMarkdownContent(), content),
                () -> assertNotNull(bg.getId()),
                () -> assertEquals(bg.getTitle(), title)
        );
    }

    private Blog addBlog() {
        final String title = "Hello World";
        final String content = "# Hello World";
        return this.mockService.addNewBlog(title, content);
    }

}