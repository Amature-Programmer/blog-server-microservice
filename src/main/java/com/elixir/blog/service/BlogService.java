package com.elixir.blog.service;

import com.elixir.blog.events.blogs.FetchType;
import com.elixir.blog.events.blogs.UpdateBlogEvent;
import com.elixir.blog.markdown.processors.CommonMarkService;
import com.elixir.blog.model.Blog;
import com.elixir.blog.repository.BlogRepository;
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
            blog.setContent(event.getContent() != null ? event.getContent() : blog.getMarkdownContent());
            return this.blogRepository.save(blog);
        }
        return null;
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
