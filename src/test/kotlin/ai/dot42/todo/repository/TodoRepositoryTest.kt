package ai.dot42.todo.repository

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotHaveSingleElement
import io.kotest.matchers.shouldBe

class TodoRepositoryTest(
    private val sut: TodoRepository = TodoRepositoryImpl()
) : StringSpec() {

    init {
        afterEach {
            sut.deleteAll()
        }
        "save todo item" {
            val todo = Todo()

            val result = sut.save(todo)

            result shouldBe todo
            sut.countAll() shouldBe 1
        }

        "retrieve todo item" {
            val todo = Todo()
            sut.save(todo)

            val result = sut.getAll()

            result shouldBe listOf(todo)
            result.size shouldBe 1
        }

        "filter todo item" {
            val todos = (1..3).map { index ->
                Todo(
                    title = "todo $index",
                    description = "description $index"
                )
            }

            sut.saveAll(todos)

            val result = sut.findByTitleContaining("1")

            result shouldBe listOf(todos[0])
            result.size shouldBe 1
        }

        "delete todo item" {
            val todos = (1..3).map { index ->
                Todo(
                    id = index,
                    title = "todo $index"
                )
            }

            sut.saveAll(todos)
            sut.deleteById(1)

            val result = sut.getAll()

            result.size shouldBe 2
            result shouldNotHaveSingleElement todos[0]
        }
    }
}