package br.com.matheuspadilha.tasks.dto;

import br.com.matheuspadilha.tasks.enums.TaskState;
import br.com.matheuspadilha.tasks.model.Address;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class TaskDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String description;
    private Integer priority;
    private TaskState state;
    private Address address;
    private LocalDate created;

}
