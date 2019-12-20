package com.elixir.blog.repository;

import com.elixir.blog.model.Blog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlogRepository extends CrudRepository<Blog, UUID> {

}
