package br.com.matheuspadilha.tasks.error

import java.time.OffsetDateTime

class ValidationError(
    dateTime: OffsetDateTime,
    status: Int,
    error: String,
    path: String
) :
    StandardError(
        dateTime,
        status,
        error,
        path
    ) {

    private val errors: MutableList<FieldMessage> = ArrayList()

    fun addError(fieldName: String?, message: String) {
        errors.add(FieldMessage(fieldName, message))
    }
}