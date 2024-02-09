package br.com.matheuspadilha.tasks.controller;

import br.com.matheuspadilha.tasks.converter.TaskConverter;
import br.com.matheuspadilha.tasks.dto.TaskDTO;
import br.com.matheuspadilha.tasks.dto.TaskInsertDTO;
import br.com.matheuspadilha.tasks.dto.TaskUpdateDTO;
import br.com.matheuspadilha.tasks.enums.TaskState;
import br.com.matheuspadilha.tasks.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private final TaskService service;
    private final TaskConverter convert;

    @GetMapping("/paginated")
    public Mono<Page<TaskDTO>> getPaginated(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "0") Integer priority,
            @RequestParam(required = false) TaskState taskState,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return service.findPaginated(convert.convert(id, title, description, priority, taskState), pageNumber, pageSize)
        .map(it -> it.map(convert::convert));
    }

    @PostMapping
    public Mono<TaskDTO> create(@RequestBody @Valid TaskInsertDTO dto) {
        return service.insert(convert.convert(dto))
                .doOnNext( it -> LOGGER.info("Saved task with id {}", it.getId()) )
                .map(convert::convert);
    }

    @PostMapping("/refresh/created")
    public Flux<TaskDTO> refreshCreated() {
        return service.refreshCreated()
                .map(convert::convert);
    }

    @PostMapping("/done")
    public Mono<List<TaskDTO>> done(@RequestBody List<String> ids) {
        return service.doneMany(ids)
                .map(convert::convertList);
    }

    @PutMapping
    public Mono<TaskDTO> updateTask(@RequestBody @Valid TaskUpdateDTO dto) {
        return service.update(convert.convert(dto))
                .doOnNext( it -> LOGGER.info("Update task with id {}", it.getId()) )
                .map(convert::convert);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return Mono.just(id)
                .doOnNext( it -> LOGGER.info("Deleting task with id {}", it) )
                .flatMap(service::deleteById);
    }

    @PostMapping("/start")
    public Mono<TaskDTO> start(@RequestParam String id, @RequestParam String zipcode) {
        return service.start(id, zipcode)
                .map(convert::convert);
    }
}
