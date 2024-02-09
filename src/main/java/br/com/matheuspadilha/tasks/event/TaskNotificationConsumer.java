package br.com.matheuspadilha.tasks.event;

import br.com.matheuspadilha.tasks.model.Task;
import br.com.matheuspadilha.tasks.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TaskNotificationConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskNotificationConsumer.class);

    private final TaskService service;

    @KafkaListener(topics = "${kafka.task.notification.output}", groupId = "${kafka.task.notification-group.id}")
    public void receiveAndFinishTask(Task task) {
        Mono.just(task)
                .doOnNext( it -> LOGGER.info("Receiving task to finish. ID: {}", it.getId()))
                .flatMap(service::done)
                .block();
    }

}
