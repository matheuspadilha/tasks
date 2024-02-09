package br.com.matheuspadilha.tasks.utils;

import br.com.matheuspadilha.tasks.dto.TaskDTO;
import br.com.matheuspadilha.tasks.enums.TaskState;
import br.com.matheuspadilha.tasks.model.Task;

public class TestUtils {

    public static Task buildValidTask() {
        return Task.builder()
                .withId("123")
                .withTitle("title")
                .withDscription("description")
                .withPriority(1)
                .withState(TaskState.INSERT)
                .build();
    }

    public static TaskDTO buildValidTaskDTO() {
        TaskDTO dto = new TaskDTO();
        dto.setId("123");
        dto.setTitle("title");
        dto.setDescription("description");
        dto.setPriority(1);
        dto.setState(TaskState.INSERT);

        return dto;
    }
}
