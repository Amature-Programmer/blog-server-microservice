package com.elixir.blog.controller;

import com.elixir.blog.events.blogs.FetchType;
import com.elixir.blog.events.blogs.NewBlogEvent;
import com.elixir.blog.events.blogs.UpdateBlogEvent;
import com.elixir.blog.markdown.processors.CommonMarkService;
import com.elixir.blog.model.Blog;
import com.elixir.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final CommonMarkService commonMarkService;

    @PostMapping("/blog")
    ResponseEntity postNewBlog(
            @RequestBody @Valid @NotNull NewBlogEvent blogEvent
    ) {
        return ResponseEntity.ok(this.blogService.addNewBlog(
                blogEvent.getTitle(), blogEvent.getContent()
        ));
    }

    @PostMapping(path = "/blog/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity postNewBlogFromFile(
            @RequestParam("content") MultipartFile file,
            @RequestParam("title") @NotNull String title
    ) throws IOException {
        String content = new String(file.getBytes());
        return ResponseEntity.ok(this.blogService.addNewBlog(
                title, content
        ));
    }

    @GetMapping("/blog/{id}")
    ResponseEntity getBlog(
            @PathVariable @NotNull String id,
            @RequestParam(required = false, name = "type", defaultValue = "JSON") FetchType fetchType
    ) {
        final UUID uuid = UUID.fromString(id);
        final Optional<Blog> optionalBlog = this.blogService.findBlog(uuid);
        if (optionalBlog.isPresent()) {
            final Object content = this.blogService.convertTo(fetchType, optionalBlog.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(fetchType.getMediaType())
                    .body(content);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * @see BlogController {@link BlogController#getBlog(String, FetchType)}
     * @deprecated
     */
    @GetMapping("/blog/{id}/render")
    ResponseEntity renderBlog(@PathVariable @NotNull String id) {
        final UUID uuid = UUID.fromString(id);
        final Optional<Blog> optionalBlog = this.blogService.findBlog(uuid);
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            String html = this.commonMarkService.convertToHtml(blog);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/blog/{id}")
    ResponseEntity renderBlog(@PathVariable @NotNull String id, @RequestBody @NotNull UpdateBlogEvent updateBlogEvent) {
        final UUID uuid = UUID.fromString(id);
        Blog blog = this.blogService.updateBlog(uuid, updateBlogEvent);
        return ResponseEntity.ok(blog);
    }

}
