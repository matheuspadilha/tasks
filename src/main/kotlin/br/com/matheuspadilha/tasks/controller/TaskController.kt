package br.com.matheuspadilha.tasks.controller

import br.com.matheuspadilha.tasks.converter.TaskConverter
import br.com.matheuspadilha.tasks.dto.TaskDTO
import br.com.matheuspadilha.tasks.dto.TaskInsertDTO
import br.com.matheuspadilha.tasks.dto.TaskUpdateDTO
import br.com.matheuspadilha.tasks.enums.TaskState
import br.com.matheuspadilha.tasks.model.Task
import br.com.matheuspadilha.tasks.service.TaskService
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/task")
class TaskController(
    private val service: TaskService,
    private val convert: TaskConverter
) {

    @GetMapping("/paginated")
    fun findPaginated(
        @RequestParam(required = false) id: String?,
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) description: String?,
        @RequestParam(required = false, defaultValue = "0") priority: Int?,
        @RequestParam(required = false) taskState: TaskState?,
        @RequestParam(value = "page", defaultValue = "0") page: Int?,
        @RequestParam(value = "size", defaultValue = "10") size: Int?
    ): Mono<Page<TaskDTO>> =
        service.findPaginated(
            convert.convert(
                id,
                title,
                description,
                priority!!,
                taskState
            ),
            page!!,
            size!!
        )
            .map { tasks -> tasks.map { convert.convert(it) } }

    @PostMapping
    fun create(@RequestBody dto: @Valid TaskInsertDTO): Mono<TaskDTO> =
        service.insert(convert.convert(dto))
            .doOnNext { it: Task -> LOGGER.info("Saved task with id {}", it.id) }
            .map { task: Task -> convert.convert(task) }

    @PostMapping("/refresh/created")
    fun refreshCreated(): Flux<TaskDTO> =
        service.refreshCreated()
            .map { task: Task -> convert.convert(task) }

    @PostMapping("/done")
    fun done(@RequestBody ids: List<String>): Mono<List<TaskDTO>> =
        service.doneMany(ids)
            .map { taskList: List<Task> -> convert.convertList(taskList) }

    @PutMapping
    fun updateTask(@RequestBody dto: @Valid TaskUpdateDTO): Mono<TaskDTO> =
        service.update(convert.convert(dto))
            .doOnNext { it: Task -> LOGGER.info("Update task with id {}", it.id) }
            .map { task: Task -> convert.convert(task) }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: String): Mono<Void> =
        Mono.just(id)
            .doOnNext { it: String -> LOGGER.info("Deleting task with id {}", it) }
            .flatMap { service.deleteById(id) }

    @PostMapping("/start")
    fun start(@RequestParam id: String, @RequestParam zipcode: String): Mono<TaskDTO> =
        service.start(id, zipcode)
            .map { task: Task -> convert.convert(task) }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)
    }
}