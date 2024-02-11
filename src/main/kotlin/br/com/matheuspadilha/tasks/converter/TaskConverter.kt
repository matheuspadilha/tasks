package br.com.matheuspadilha.tasks.converter

import br.com.matheuspadilha.tasks.dto.TaskDTO
import br.com.matheuspadilha.tasks.dto.TaskInsertDTO
import br.com.matheuspadilha.tasks.dto.TaskUpdateDTO
import br.com.matheuspadilha.tasks.enums.TaskState
import br.com.matheuspadilha.tasks.model.Task
import org.springframework.stereotype.Component
import java.util.*

@Component
class TaskConverter {

    fun convert(task: Task): TaskDTO =
        Optional.ofNullable(task)
            .map { it: Task ->
                TaskDTO(
                    it.id,
                    it.title,
                    it.description,
                    it.priority,
                    it.state,
                    it.address,
                    it.created
                )
            }
            .orElse(null)

    fun convert(dto: TaskDTO): Task =
        Optional.ofNullable(dto)
            .map { it: TaskDTO ->
                Task(
                    it.id,
                    it.title,
                    it.description,
                    it.priority,
                    it.state,
                    it.address,
                    it.created
                )
            }
            .orElse(null)

    fun convert(dto: TaskInsertDTO): Task =
        Optional.ofNullable(dto)
            .map { it: TaskInsertDTO ->
                Task(
                    it.title,
                    it.description,
                    it.priority
                )
            }
            .orElse(null)

    fun convert(dto: TaskUpdateDTO): Task =
        Optional.ofNullable(dto)
            .map { it: TaskUpdateDTO ->
                Task(
                    it.id,
                    it.title,
                    it.description,
                    it.priority
                )
            }
            .orElse(null)

    fun convert(
        id: String?,
        title: String?,
        description: String?,
        priority: Int,
        taskState: TaskState?
    ): Task =
        Task(
            id,
            title,
            description,
            priority,
            taskState
        )

    fun convertList(taskList: List<Task>): List<TaskDTO> =
        Optional.ofNullable(taskList)
            .map { items: List<Task> ->
                items.stream()
                    .map { task: Task -> this.convert(task) }
                    .toList()
            }
            .orElse(ArrayList())

}