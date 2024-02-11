package br.com.matheuspadilha.tasks.error

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import java.io.Serializable
import java.time.OffsetDateTime

@JsonInclude(Include.NON_NULL)
open class StandardError : Serializable {
    var dateTime: OffsetDateTime
        private set

    var status: Int
        private set

    var error: String
        private set

    var message: String? = null
        private set

    var path: String
        private set

    var map: MutableMap<String, Any>? = null
        private set

    constructor(dateTime: OffsetDateTime, status: Int, error: String, message: String, path: String) {
        this.dateTime = dateTime
        this.status = status
        this.error = error
        this.message = message
        this.path = path
    }

    constructor(dateTime: OffsetDateTime, status: Int, error: String, path: String) {
        this.dateTime = dateTime
        this.status = status
        this.error = error
        this.path = path
    }
}