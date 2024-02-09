package br.com.matheuspadilha.tasks.converter;

import br.com.matheuspadilha.tasks.dto.TaskDTO;
import br.com.matheuspadilha.tasks.dto.TaskInsertDTO;
import br.com.matheuspadilha.tasks.dto.TaskUpdateDTO;
import br.com.matheuspadilha.tasks.enums.TaskState;
import br.com.matheuspadilha.tasks.model.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TaskConverter {

    public TaskDTO convert(Task task) {
        return Optional.ofNullable(task)
                .map(item -> {
                    TaskDTO dto = new TaskDTO();
                    dto.setId(item.getId());
                    dto.setTitle(item.getTitle());
                    dto.setDescription(item.getDescription());
                    dto.setPriority(item.getPriority());
                    dto.setState(item.getState());
                    dto.setAddress(item.getAddress());
                    dto.setCreated(item.getCreated());

                    return dto;
                })
                .orElse(null);
    }

    public Task convert(TaskDTO dto) {
        return Optional.ofNullable(dto)
                .map(item -> Task.builder()
                        .withId(item.getId())
                        .withTitle(item.getTitle())
                        .withDscription(item.getDescription())
                        .withPriority(item.getPriority())
                        .withState(item.getState())
                        .build()
                )
                .orElse(null);
    }

    public Task convert(TaskInsertDTO dto) {
        return Optional.ofNullable(dto)
                .map(item -> Task.builder()
                        .withTitle(item.getTitle())
                        .withDscription(item.getTitle())
                        .withPriority(item.getPriority())
                        .build()
                )
                .orElse(null);
    }

    public Task convert(TaskUpdateDTO dto) {
        return Optional.ofNullable(dto)
                .map(item -> Task.builder()
                        .withId(item.getId())
                        .withTitle(item.getTitle())
                        .withDscription(item.getTitle())
                        .withPriority(item.getPriority())
                        .build()
                )
                .orElse(null);
    }

    public Task convert(
            String id,
            String title,
            String description,
            int priority,
            TaskState taskState
    ) {
        return Task.builder()
                .withId(id)
                .withTitle(title)
                .withDscription(description)
                .withPriority(priority)
                .withState(taskState)
                .build();
    }

    public List<TaskDTO> convertList(List<Task> taskList) {
        return Optional.ofNullable(taskList)
                .map(array -> array.stream()
                        .map(this::convert)
                        .toList()
                )
                .orElse(new ArrayList<>());
    }
}
