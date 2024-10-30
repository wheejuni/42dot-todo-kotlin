package ai.dot42.todo.service

import ai.dot42.todo.repository.Todo
import ai.dot42.todo.repository.TodoRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.mockk.*

class TodoServiceTest() : StringSpec({


    val todoRepository = mockk<TodoRepository>()
    val todoService: TodoService = TodoServiceImpl(todoRepository)
    afterEach {
        clearMocks(todoRepository)
    }

    val todo = Todo(
        id = 1,
        title = "Test Todo",
        description = "This is a test todo"
    )

    "Create Todo" {
        every { todoRepository.save(any()) } returns todo
        val createdTodo = todoService.createTodo(todo.title, todo.description)

        createdTodo shouldBe todo
    }

    "Get Todo List" {
        every { todoRepository.getAll() } returns listOf(todo, todo)
        val todoList = todoService.getTodoList()

        todoList shouldContainExactly listOf(todo.id, todo.id)
    }

    "Update Todo" {
        val updatedTodo = Todo(
            id = 1,
            title = "Updated Test Todo",
            description = "This is an updated test todo"
        )

        every { todoRepository.save(any()) } returns updatedTodo
        val result = todoService.updateTodo(updatedTodo)

        result shouldBe updatedTodo
    }

    "Delete Todo" {
        every { todoRepository.save(any()) } returns todo
        todoService.createTodo(todo.title, todo.description)

        every { todoRepository.deleteById(any()) } just Runs
        todoService.deleteTodoById(todo.id!!)

        every { todoRepository.getAll() } returns emptyList()
        val todoList = todoService.getTodoList()

        todoList shouldNotContain todo
    }
})


