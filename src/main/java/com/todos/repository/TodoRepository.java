package com.todos.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.todos.domain.Todo;

/**
 * Repository class which is implemented by Spring.
 * 
 * @author igp
 */

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {
    public List<Todo> findByUser(String user);
}