package br.com.matheuspadilha.tasks.error

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import java.io.Serializable

@JsonInclude(Include.NON_NULL)
data class FieldMessage(
    val fieldName: String? = null,
    val message: String? = null
) : Serializable