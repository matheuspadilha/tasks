package br.com.matheuspadilha.tasks.error;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ValidationError extends StandardError {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(OffsetDateTime dateTime, Integer status, String error, String path) {
        super(dateTime, status, error, path);
    }

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }

}