package ai.dot42.todo.repository

interface TodoRepository {
    fun getAll(): List<Todo>
    fun countAll(): Int
    fun findByTitleContaining(s: String): List<Todo>

    fun save(todo: Todo): Todo
    fun saveAll(todos: List<Todo>)

    fun deleteById(i: Int)
    fun deleteAll()
}
