package br.com.matheuspadilha.tasks.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.io.Serializable

data class TaskInsertDTO(
    @NotBlank(message = "{blank.title}")
    @Size(min = 3, max = 20, message = "{size.title}")
    val title: String,

    @NotBlank(message = "{blank.description}")
    @Size(min = 10, max = 50, message = "{size.description}")
    val description: String,

    @Min(value = 1, message = "{min.priority}")
    val priority: Int,
) : Serializable
