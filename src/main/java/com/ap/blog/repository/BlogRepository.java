package com.ap.blog.repository;

import com.ap.blog.model.Blog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlogRepository extends CrudRepository<Blog, UUID> {

}
