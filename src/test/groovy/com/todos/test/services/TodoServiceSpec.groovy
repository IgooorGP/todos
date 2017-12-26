package com.todos.test.services

import spock.lang.Specification
import com.todos.repository.TodoRepository
import com.todos.services.TodoService
import com.todos.domain.Todo
import com.todos.test.helpers.TestHelper
import com.todos.errors.EntityNotFoundException

class TodoServiceSpec extends Specification {
    
    // fields
    TodoRepository todoRepo = Mock(TodoRepository)
    TodoService todoService = new TodoService(todoRepo: todoRepo)
    
    // feature methods
    /**
     * CREATE service
     */
    def "creating a todo with mongo repository"() {
        when: "todoCRUD invokes todoService.create method"
            Todo createdTodo = todoService.create(TestHelper.getDummyTodo())
        
        then: "should invoke todoRepo.save method" 
            1 * todoRepo.save(_) >> TestHelper.getDummyTodo()
            
        and: "assert created todo"
            TestHelper.assertTodo(createdTodo, TestHelper.getDummyTodo())
    }
        
    def "getting a todo with mongo repository" () {
        // fake id
        String todoId = "1"
                
        when: "todoCRUD invokes todoService.findById"
            Todo foundTodo = todoService.findById(todoId)
            
        then: "should invoke todoRepo.findOne"
            1 * todoRepo.findOne(_) >> TestHelper.getDummyTodo()            
            
        and: "assert found todo"
           TestHelper.assertTodo(foundTodo, TestHelper.getDummyTodo())                  
    }
    
    def "getting a todo with mongo repository WITHOUT any results" () {
        String todoId = "1"
        
        when: "todoCRUD invokes todoService.findById and NO todo is found"
            Todo foundTodo = todoService.findById(todoId)
            
        then: "todoRepo returns null and EntityNotFoundException is thrown " +
              "to send a customized user-friendly JSON message handled by Spring"
             1 * todoRepo.findOne(_) >> null // query for one todo returns NULL when nothing is found
             
             thrown EntityNotFoundException
    }
    
    def "getting ALL todos with mongo repository" () {
        // number of dummy todos returned
        def NUMBER_OF_RETURNED_TODOS = 3
                
        when: "todoCRUD invokes todoService.findById"
            List<Todo> todoList = todoService.findAll()
            
        then: "should invoke todoRepo.findOne and return 3 dummy Todos"
            1 * todoRepo.findAll() >> TestHelper.getDummyTodo(NUMBER_OF_RETURNED_TODOS)            
            
        and: "assert found todo"
           TestHelper.assertTodo(todoList, TestHelper.getDummyTodo(NUMBER_OF_RETURNED_TODOS))                  
    }
    
    def "getting ALL todos with mongo repository WITHOUT any results" () {
        when: "todoCRUD invokes todoService.findById and NO todos are found"
            List<Todo> foundTodo = todoService.findAll()
            
        then: "todoRepo returns null and EntityNotFoundException is thrown " +
              "to send a customized user-friendly JSON message handled by Spring"
              
            1 * todoRepo.findAll() >> new ArrayList<Todo>() // query for all todos returns an EMPTY 
            
            thrown EntityNotFoundException
    }
    
    def "deleting one todo with mongo repository" () {
        String todoId = "1"
        
        when: "todoCRUD invokes todoService.delete"
            todoService.delete(todoId) // void method
            
        then: "todoRepo should try to find the todo first and then delete it"
            1 * todoRepo.findOne(_) >> TestHelper.getDummyTodo()
            1 * todoRepo.delete(_) 
    }
    
    def "deleting one todo with mongo repository that does NOT exist"() {
        String todoId = "1"
        
        when: "todoCRUD invokes todoService.delete"
            todoService.delete(todoId) // void method
            
        then: "todoRepo should try to find the todo but fails and throws EntityNotFoundException"
            1 * todoRepo.findOne(_) >> null
             
            thrown EntityNotFoundException
    }
    
    def "updating one todo with mongo repository" () {
        when: "todoCRUD invokes todoService.update with a todo from a POST payload"
            Todo updatedTodo = todoService.update(TestHelper.getDummyTodo())
            
        then: "todoRepo should try to find the todo first and then update it with save method (upsert)"
            1 * todoRepo.findOne(_) >> TestHelper.getDummyTodo()
            1 * todoRepo.save(_) 
        
        and: "assert updated todo"
            TestHelper.assertTodo(updatedTodo, TestHelper.getDummyTodo())              
    }
    
    def "updating one todo with mongo repository that does NOT exist"() {
        when: "todoCRUD invokes todoService.update with a todo from a POST payload"
            Todo updatedTodo = todoService.update(TestHelper.getDummyTodo())
            
        then: "todoRepo should try to find the todo but fails and throws EntityNotFoundException"
            1 * todoRepo.findOne(_) >> null
            
            thrown EntityNotFoundException        
    }
    
}
