package com.ap.blog.controller;

import com.ap.blog.events.NewBlogEvent;
import com.ap.blog.events.UpdateBlogEvent;
import com.ap.blog.model.Blog;
import com.ap.blog.service.BlogService;
import com.ap.blog.service.RenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final RenderService renderService;

    @PostMapping("/blog")
    ResponseEntity postNewBlog(
            @RequestBody @Valid @NotNull NewBlogEvent blogEvent
    ) {
        return ResponseEntity.ok(this.blogService.addNewBlog(
                blogEvent.getTitle(), blogEvent.getContent()
        ));
    }

    @GetMapping("/blog/{id}")
    ResponseEntity getBlog(@PathVariable @NotNull String id) {
        final UUID uuid = UUID.fromString(id);
        final Optional<Blog> optionalBlog = this.blogService.findBlog(uuid);
        if (optionalBlog.isPresent()) {
            return ResponseEntity.ok(optionalBlog.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/blog/{id}/render")
    ResponseEntity renderBlog(@PathVariable @NotNull String id) {
        final UUID uuid = UUID.fromString(id);
        final Optional<Blog> optionalBlog = this.blogService.findBlog(uuid);
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            String html = this.renderService.renderBlog(blog);
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
