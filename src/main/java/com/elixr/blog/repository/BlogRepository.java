package com.elixr.blog.repository;

import com.elixr.blog.model.Blog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlogRepository extends CrudRepository<Blog, UUID> {

}
