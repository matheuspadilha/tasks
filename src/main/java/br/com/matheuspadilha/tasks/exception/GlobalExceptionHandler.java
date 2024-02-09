package br.com.matheuspadilha.tasks.exception;

import br.com.matheuspadilha.tasks.error.ValidationError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ValidationError>> handleMethodArgumentNotValid(
            WebExchangeBindException ex, ServerWebExchange exchange) {

        ValidationError error = new ValidationError(
                OffsetDateTime.now(),
                ex.getStatusCode().value(),
                "Error Validation",
                exchange.getRequest().getURI().toString()
        );

        ex.getBindingResult().getFieldErrors().forEach(
                fieldError -> error.addError(fieldError.getField(), fieldError.getDefaultMessage())
        );

        return Mono.just(ResponseEntity.status(ex.getStatusCode()).body(error));
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ValidationError>> handleNotFound(NotFoundException ex, ServerWebExchange exchange) {
        ValidationError error = new ValidationError(
                OffsetDateTime.now(),
                ex.getStatusCode().value(),
                "Not Found",
                exchange.getRequest().getURI().toString()
        );

        error.addError(null, ex.getMessage());

        return Mono.just(ResponseEntity.status(ex.getStatusCode()).body(error));
    }

}
