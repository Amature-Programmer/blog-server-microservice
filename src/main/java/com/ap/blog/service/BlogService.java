package com.ap.blog.service;

import com.ap.blog.events.UpdateBlogEvent;
import com.ap.blog.model.Blog;
import com.ap.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final CommonMarkService markdownProcessor;

    public Blog addNewBlog(String title, String content) {
        final Blog blog = Blog.builder().title(title).content(content).build();
        return this.blogRepository.save(blog);
    }

    public Optional<Blog> findBlog(UUID blogId) {
        return this.blogRepository.findById(blogId);
    }

    public Blog updateBlog(UUID blogId, UpdateBlogEvent event) {
        final Optional<Blog> originalBlog = this.blogRepository.findById(blogId);
        if (originalBlog.isPresent()) {
            Blog blog = originalBlog.get();
            blog.setTitle(event.getTitle() != null ? event.getTitle() : blog.getTitle());
            blog.setContent(event.getContent() != null ? event.getContent() : blog.getContent());
            return this.blogRepository.save(blog);
        }
        return originalBlog.get();
    }

    public Object convertTo(FetchType fetchType, Blog blog) {
        switch (fetchType) {
            case JSON:
                return blog;
            case PDF:
                break;
            case HTML:
                return this.markdownProcessor.convertToHtml(blog);
            case MARKDOWN:
                return blog.getMarkdownContent();
        }
        throw new UnsupportedOperationException("Cannot convert blog to the following format: " + fetchType.toString());
    }
}
