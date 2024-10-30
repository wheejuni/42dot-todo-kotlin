package ai.dot42.todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Dot42TodoKotlinApplication

fun main(args: Array<String>) {
    runApplication<Dot42TodoKotlinApplication>(*args)
}
