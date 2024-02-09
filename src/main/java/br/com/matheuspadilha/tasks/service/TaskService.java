package br.com.matheuspadilha.tasks.service;

import br.com.matheuspadilha.tasks.event.TaskNotificationProducer;
import br.com.matheuspadilha.tasks.exception.NotFoundException;
import br.com.matheuspadilha.tasks.model.Address;
import br.com.matheuspadilha.tasks.model.Task;
import br.com.matheuspadilha.tasks.repository.TaskCustomRepository;
import br.com.matheuspadilha.tasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository repository;
    private final TaskCustomRepository taskCustomRepository;
    private final AddressService addressService;
    private final TaskNotificationProducer producer;

    public Mono<Task> insert(Task task) {
        return Mono.just(task)
                .map(Task::insert)
                .flatMap(this::save)
                .doOnError(it -> LOGGER.error("Error during save task. Title: {}", task.getTitle(), it));
    }

    public Mono<Page<Task>> findPaginated(Task task, Integer page, Integer size) {
        return taskCustomRepository.findPaginated(task, page, size);
    }

    public Mono<Task> update(Task task) {
        return repository.findById(task.getId())
                .map(task::update)
                .flatMap(repository::save)
                .switchIfEmpty(Mono.error(new NotFoundException("Task Not Found!")))
                .doOnError(it -> LOGGER.error("Error during update task. ID: {}, Message: {}", task.getId(), it.getMessage()));
    }

    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    public Mono<Task> start(String id, String zipcode) {
        return repository.findById(id)
                .zipWhen(it -> addressService.getAddress(zipcode))
                .flatMap(it -> updateAddress(it.getT1(), it.getT2()))
                .map(Task::start)
                .flatMap(repository::save)
                .flatMap(producer::sendNotification)
                .switchIfEmpty(Mono.error(new NotFoundException("Task Not Found!")))
                .doOnError(it -> LOGGER.error("Error on start task. ID: {}", id, it));
    }

    public Mono<Task> done(Task task) {
        return Mono.just(task)
                .doOnNext(it -> LOGGER.info("Finishing task. ID: {}", it.getId()))
                .map(Task::done)
                .flatMap(repository::save);
    }

    public Mono<List<Task>> doneMany(List<String> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> repository.findById(id)
                        .map(Task::done)
                        .flatMap(repository::save)
                        .doOnNext(it -> LOGGER.info("Done task. ID: {}", it.getId()))
                )
                .collectList();
    }

    public Flux<Task> refreshCreated() {
        return repository.findAll()
                .filter(Task::createdIsEmpty)
                .map(Task::createdNow)
                .flatMap(repository::save);
    }

    private Mono<Task> updateAddress(Task task, Address address) {
        return Mono.just(task)
                .map(it -> task.updateAddress(address));
    }

    private Mono<Task> save(Task task){
        return Mono.just(task)
                .doOnNext(it -> LOGGER.info("Saving task with title {}", it.getTitle()))
                .flatMap(repository::save);
    }

}
