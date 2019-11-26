package com.ap.blog.service;

import com.ap.blog.events.UpdateBlogEvent;
import com.ap.blog.model.Blog;
import com.ap.blog.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

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

}
