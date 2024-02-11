package br.com.matheuspadilha.tasks.exception

import br.com.matheuspadilha.tasks.error.ValidationError
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.function.Consumer

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleMethodArgumentNotValid(
        ex: WebExchangeBindException, exchange: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        val error = ValidationError(
            OffsetDateTime.now(),
            ex.statusCode.value(),
            "Error Validation",
            exchange.request.uri.toString()
        )

        ex.bindingResult.fieldErrors.forEach(
            Consumer { fieldError: FieldError ->
                error.addError(
                    fieldError.field,
                    fieldError.defaultMessage!!
                )
            }
        )

        return Mono.just(ResponseEntity.status(ex.statusCode).body(error))
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(ex: NotFoundException, exchange: ServerWebExchange): Mono<ValidationError> {
        val error = ValidationError(
            OffsetDateTime.now(),
            ex.statusCode.value(),
            "Not Found",
            exchange.request.uri.toString()
        )

        error.addError(null, ex.message)

        return Mono.just(error)
    }
}