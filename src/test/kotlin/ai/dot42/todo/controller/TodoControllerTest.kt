package ai.dot42.todo.controller

import ai.dot42.todo.domain.CommonResponse
import ai.dot42.todo.repository.Todo
import ai.dot42.todo.service.TodoService
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class TodoControllerTest : FunSpec() {
    val todoService = mockk<TodoService>()
    private val todoController = TodoController(todoService)

    private val mockMvc = MockMvcBuilders
        .standaloneSetup(todoController)
        .build()

    private val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    init {
        test("get todos should return todo list") {
            val expectedTodoIdList = listOf(1, 2, 3)
            every {
                todoService.getTodoList()
            } returns expectedTodoIdList

            mockMvc.perform(
                MockMvcRequestBuilders.get("{fix it restful uri}")
            )
                .andExpect(status().isOk)
                .andExpect{ result ->
                    val responseString = result.response.contentAsString
                    val response = objectMapper.readValue<CommonResponse<List<Int>>>(responseString)
                    response.returnCode shouldBe "SUCCESS"
                    response.data shouldNotBe null
                    response.data!!.size shouldBe expectedTodoIdList.size
                }
        }


        test("get todo with id should return todo detail") {
            val idToFind = 1
            val expectedTodo = Todo(1, "title", "description")

            every {
                todoService.getTodo(any())
            } returns expectedTodo


            mockMvc.perform(
                MockMvcRequestBuilders.get("{fix it restful uri}", idToFind)
            )
                .andExpect(status().isOk)
                .andExpect{ result ->
                    val responseString = result.response.contentAsString
                    val response = objectMapper.readValue<CommonResponse<Todo>>(responseString)
                    response.returnCode shouldBe "SUCCESS"
                    response.data shouldNotBe null
                    response.data?.let{
                        it shouldBe expectedTodo
                    }
                }
        }

        test("create todo should return created todo") {
            val request = TodoCreateRequest("new title", "new description")
            val createdTodo = Todo(1, "new title", "new description")
            every { todoService.createTodo(any(), any()) } returns createdTodo

            mockMvc.perform(
                MockMvcRequestBuilders.request("적당한 http method를 넣어주세요", "{fix it restful uri}")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isOk)
                .andExpect { result ->
                    val responseString = result.response.contentAsString
                    val response = objectMapper.readValue<CommonResponse<Todo>>(responseString)
                    response.returnCode shouldBe "SUCCESS"
                    response.data shouldNotBe null
                    response.data?.let {
                        it shouldBe createdTodo
                    }
                }
        }

        test("update todo should return updated todo") {
            val idToUpdate = 1
            val request = TodoUpdateRequest(idToUpdate, "updated title", "updated description")
            val updatedTodo = Todo(idToUpdate, request.title, request.description)
            every { todoService.updateTodo(updatedTodo) } returns updatedTodo

            mockMvc.perform(
                MockMvcRequestBuilders.request("적당한 HTTP METHOD를 넣어주세요", "{fix it restful uri}")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(updatedTodo))
            )
                .andExpect(status().isOk)
                .andExpect { result ->
                    val responseString = result.response.contentAsString
                    val response = objectMapper.readValue<CommonResponse<Todo>>(responseString)
                    response.returnCode shouldBe "SUCCESS"
                    response.data shouldNotBe null
                    response.data?.let {
                        it shouldBe updatedTodo
                    }
                }
        }

        test("delete todo should return success") {
            val idToDelete = 1
            every { todoService.deleteTodoById(idToDelete) } returns Unit

            mockMvc.perform(
                MockMvcRequestBuilders.request("적당한 HTTP METHOD를 넣어주세요","{fix it restful uri}", idToDelete)
            )
                .andExpect(status().isOk)
                .andExpect { result ->
                    val responseString = result.response.contentAsString
                    val response = objectMapper.readValue<CommonResponse<Unit>>(responseString)
                    response.returnCode shouldBe "SUCCESS"
                }
        }

    }
}
