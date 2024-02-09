package br.com.matheuspadilha.tasks.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@JsonInclude(Include.NON_NULL)
public class StandardError implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private OffsetDateTime dateTime;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private Map<String, Object> map;

    public StandardError(OffsetDateTime dateTime, Integer status, String error, String message, String path){
        this.dateTime = dateTime;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public StandardError(OffsetDateTime dateTime, Integer status, String error, String path){
        this.dateTime = dateTime;
        this.status = status;
        this.error = error;
        this.path = path;
    }

}