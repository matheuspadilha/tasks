package br.com.matheuspadilha.tasks.event

import br.com.matheuspadilha.tasks.model.Task
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class TaskNotificationProducer(
    private val template: KafkaTemplate<Any, Any>
) {

    fun send(task: Task): Mono<Task> =
        Mono.just(task)
            .map { template.send(TASK_NOTIFICATION_V1_PRODUCER, it) }
            .map { task }
            .doOnNext { LOGGER.info("Task notification send successfully") }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)
        private const val TASK_NOTIFICATION_V1_PRODUCER = "taskEventProducer-out-0"
    }
}