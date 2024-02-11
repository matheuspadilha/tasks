package br.com.matheuspadilha.tasks

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks

@SpringBootApplication
class TasksApplication

fun main(args: Array<String>) {
	Hooks.enableAutomaticContextPropagation()

	runApplication<TasksApplication>(*args)
}
