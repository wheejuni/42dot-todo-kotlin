package ai.dot42.todo.repository

data class Todo(
    val id: Int? = null,
    val title: String = "Test Todo",
    val description: String = "This is a test todo"
)
