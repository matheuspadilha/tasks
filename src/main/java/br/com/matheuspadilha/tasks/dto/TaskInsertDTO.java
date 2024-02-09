package br.com.matheuspadilha.tasks.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TaskInsertDTO implements Serializable {

    @NotBlank(message = "{blank.title}")
    @Size(min = 3, max = 20, message = "{size.title}")
    private String title;

    @NotBlank(message = "{blank.description}")
    @Size(min = 10, max = 50, message =  "{size.description}")
    private String description;

    @Min(value = 1, message = "{min.priority}")
    private Integer priority;
}
