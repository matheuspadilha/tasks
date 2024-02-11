package br.com.matheuspadilha.tasks.event

import br.com.matheuspadilha.tasks.model.Task
import br.com.matheuspadilha.tasks.service.TaskService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.function.Consumer

@Component
class TaskNotificationConsumer(
    private val service: TaskService
) {

    @Bean
    fun taskEventConsumer(): Consumer<Task> = Consumer { listen(it) }

    private fun listen(task: Task) {
        LOGGER.info("Starting the task topic reading process")
        processTaskEvent(task)
        LOGGER.info("finishing the task topic reading process")
    }

    fun processTaskEvent(task: Task) =
        Mono.just(task)
            .doOnNext { it: Task -> LOGGER.info("Receiving task to finish. ID: {}", it.id) }
            .flatMap { service.done(it) }
            .block()

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)
    }
}