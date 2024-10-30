package ai.dot42.todo.domain

data class CommonResponse<T>(
    val returnCode: String,
    val data: T?
)