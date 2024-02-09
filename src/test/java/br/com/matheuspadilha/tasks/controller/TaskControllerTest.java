package br.com.matheuspadilha.tasks.controller;

import br.com.matheuspadilha.tasks.converter.TaskConverter;
import br.com.matheuspadilha.tasks.dto.TaskDTO;
import br.com.matheuspadilha.tasks.dto.TaskInsertDTO;
import br.com.matheuspadilha.tasks.model.Task;
import br.com.matheuspadilha.tasks.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskControllerTest {

    @InjectMocks
    private TaskController controller;
    @Mock
    private TaskService service;
    @Mock
    private TaskConverter converter;

    @Test
    void controller_mustReturnOk_whenSaveSuccessfully() {
        when(converter.convert(any(Task.class))).thenReturn(new TaskDTO());
        when(service.insert(any())).thenReturn(Mono.just(new Task()));

        WebTestClient client = WebTestClient.bindToController(controller).build();

        client.post()
                .uri("/task")
                .bodyValue(new TaskInsertDTO())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDTO.class);
    }

//    @Test
//    void controller_mustReturnOk_whenGetPaginatedSuccessfully() {
//        when(service.findPaginated(any(), anyInt(), anyInt())).thenReturn(Mono.just(Page.empty()));
//
//        WebTestClient client = WebTestClient.bindToController(controller).build();
//
//        client.get()
//                .uri("/task/paginated")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(TaskDTO.class)
//                .hasSize(0);
//    }

    @Test
    void controller_mustReturnNoContent_whenDeleteSuccessfully() {
        String taskId = "any_id";

        when(service.deleteById(any())).thenReturn(Mono.empty());

        WebTestClient client = WebTestClient.bindToController(controller).build();

        client.delete()
                .uri("/task/" + taskId)
                .exchange()
                .expectStatus().isNoContent();
    }
}
