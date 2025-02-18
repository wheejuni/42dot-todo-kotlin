package ai.dot42.todo.repository

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.atomic.AtomicInteger

class TodoRepositoryImpl : TodoRepository {
    private val autogenId = AtomicInteger(1)
    private val todos: ConcurrentMap<Int, Todo> = ConcurrentHashMap()

    override fun getAll(): List<Todo> {
        return todos.values.toList()
    }

    override fun countAll(): Int {
        return todos.size
    }

    override fun findByTitleContaining(s: String): List<Todo> {
        return todos.values.filter { todo -> todo.title.contains(s) }
    }

    override fun save(todo: Todo): Todo {
        val id = todo.id ?: autogenId.getAndIncrement()
        val newTodo = todo.copy(id = id)
        todos[id] = newTodo
        return newTodo
    }

    override fun saveAll(todos: List<Todo>) {
        todos.forEach { todo -> this.save(todo) }
    }

    override fun deleteById(i: Int) {
        todos.remove(i)
    }

    override fun deleteAll() {
        todos.clear()
    }
}
