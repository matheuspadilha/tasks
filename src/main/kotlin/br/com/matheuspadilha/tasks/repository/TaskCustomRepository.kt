package br.com.matheuspadilha.tasks.repository

import br.com.matheuspadilha.tasks.model.Task
import org.springframework.data.domain.*
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2

@Repository
class TaskCustomRepository(
    private val mongoOperations: ReactiveMongoOperations
) {

    fun findPaginated(task: Task, page: Int, size: Int): Mono<Page<Task>> =
        queryExample(task)
            .zipWith(pageable(page, size))
            .flatMap { it: Tuple2<Example<Task>, Pageable> -> execute(task, it.t1, it.t2) }

    private fun execute(task: Task, example: Example<Task>, pageable: Pageable): Mono<Page<Task>> =
        query(example, pageable, task)
            .flatMap { query: Query -> mongoOperations.find(query, Task::class.java).collectList() }
            .flatMap { tasks: List<Task> -> paginate(tasks, pageable, example) }

    private fun paginate(tasks: List<Task>, pageable: Pageable, example: Example<Task>): Mono<Page<Task>> =
        Mono.just(tasks)
            .flatMap { _: List<Task> ->
                mongoOperations.count(
                    Query.query(Criteria.byExample(example)),
                    Task::class.java
                )
            }
            .map { counter: Long -> PageableExecutionUtils.getPage(tasks, pageable) { counter } }

    private fun pageable(page: Int, size: Int): Mono<Pageable> =
        Mono.just(PageRequest.of(page, size, Sort.by("title").ascending()))

    private fun queryExample(task: Task): Mono<Example<Task>> =
    Mono.just(task)
            .map { _: Task? -> ExampleMatcher.matching().withIgnorePaths("priority", "state") }
            .map { matcher: ExampleMatcher -> Example.of(task, matcher) }

    private fun query(example: Example<Task>, pageable: Pageable, task: Task): Mono<Query> =
        Mono.just(example)
            .map { it: Example<Task> -> Query.query(Criteria.byExample(it)).with(pageable) }
            .flatMap { it: Query -> validatePriority(it, task) }
            .flatMap { it: Query -> validateState(it, task) }

    private fun validatePriority(query: Query, task: Task): Mono<Query> =
        Mono.just(task)
            .filter { obj: Task -> obj.isValidPriority() }
            .map { it: Task ->
                query.addCriteria(
                    Criteria.where("priority").`is`(it.priority)
                )
            }
            .defaultIfEmpty(query)

    private fun validateState(query: Query, task: Task): Mono<Query> =
        Mono.just(task)
            .filter { obj: Task -> obj.isValidState() }
            .map { it: Task ->
                query.addCriteria(
                    Criteria.where("state").`is`(it.state!!)
                )
            }
            .defaultIfEmpty(query)
}