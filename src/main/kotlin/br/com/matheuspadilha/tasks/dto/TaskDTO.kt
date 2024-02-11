package br.com.matheuspadilha.tasks.dto

import br.com.matheuspadilha.tasks.enums.TaskState
import br.com.matheuspadilha.tasks.model.Address
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import java.io.Serializable
import java.time.LocalDate

@JsonInclude(Include.NON_NULL)
data class TaskDTO (
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val priority: Int,
    val state: TaskState? = null,
    val address: Address? = null,
    val created: LocalDate? = null
) : Serializable
