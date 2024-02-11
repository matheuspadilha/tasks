package br.com.matheuspadilha.tasks.service

import br.com.matheuspadilha.tasks.event.TaskNotificationProducer
import br.com.matheuspadilha.tasks.exception.NotFoundException
import br.com.matheuspadilha.tasks.model.Address
import br.com.matheuspadilha.tasks.model.Task
import br.com.matheuspadilha.tasks.repository.TaskCustomRepository
import br.com.matheuspadilha.tasks.repository.TaskRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2

@Service
class TaskService(
    private val repository: TaskRepository,
    private val taskCustomRepository: TaskCustomRepository,
    private val addressService: AddressService,
    private val producer: TaskNotificationProducer
) {

    fun insert(task: Task): Mono<Task> =
        Mono.just(task)
            .map { obj: Task -> obj.insert() }
            .flatMap { it: Task -> this.save(it) }
            .doOnError { it: Throwable -> LOGGER.error("Error during save task. Title: {}", task.title, it) }

    fun findPaginated(task: Task, page: Int, size: Int): Mono<Page<Task>> =
        taskCustomRepository.findPaginated(task, page, size)

    fun update(task: Task): Mono<Task> =
        repository.findById(task.id!!)
            .map { oldTask: Task -> task.update(oldTask) }
            .flatMap { entity: Task -> repository.save(entity) }
            .switchIfEmpty(Mono.error(NotFoundException(TASK_NOTFOUND_MESSAGE)))
            .doOnError { it: Throwable ->
                LOGGER.error("Error during update task. ID: {}, Message: {}", task.id, it.message)
            }

    fun deleteById(id: String): Mono<Void> =
        repository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException(TASK_NOTFOUND_MESSAGE)))
            .flatMap { repository.deleteById(id) }

    fun start(id: String, zipcode: String): Mono<Task> =
        repository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException(TASK_NOTFOUND_MESSAGE)))
            .zipWhen { _: Task -> addressService.getAddress(zipcode) }
            .flatMap { it: Tuple2<Task, Address?> -> updateAddress(it.t1, it.t2) }
            .map { it: Task -> it.start() }
            .flatMap { entity: Task -> repository.save(entity) }
            .flatMap { it: Task -> producer.send(it) }
            .doOnError { it: Throwable -> LOGGER.error("Error on start task. ID: {}", id, it) }


    fun done(task: Task): Mono<Task> =
        Mono.just(task)
            .doOnNext { it: Task -> LOGGER.info("Finishing task. ID: {}", it.id) }
            .map { obj: Task -> obj.done() }
            .flatMap { entity: Task -> repository.save(entity) }


    fun doneMany(ids: List<String>): Mono<List<Task>> =
        Flux.fromIterable(ids)
            .flatMap { id: String ->
                repository.findById(id)
                    .map { obj: Task -> obj.done() }
                    .flatMap { entity: Task -> repository.save(entity) }
                    .doOnNext { it: Task -> LOGGER.info("Done task. ID: {}", it.id) }
            }
            .collectList()

    fun refreshCreated(): Flux<Task> =
        repository.findAll()
            .filter { obj: Task -> obj.createdIsEmpty() }
            .map { obj: Task -> obj.createdNow() }
            .flatMap { entity: Task -> repository.save(entity) }

    private fun updateAddress(task: Task, address: Address): Mono<Task> =
        Mono.just(task)
            .map { it: Task -> it.updateAddress(address) }


    private fun save(task: Task): Mono<Task> =
        Mono.just(task)
            .doOnNext { it: Task -> LOGGER.info("Saving task with title {}", it.title) }
            .flatMap { entity: Task -> repository.save(entity) }


    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)
        private const val TASK_NOTFOUND_MESSAGE = "Task Not Found!"
    }
}
