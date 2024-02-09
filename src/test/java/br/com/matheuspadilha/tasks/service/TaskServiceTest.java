package br.com.matheuspadilha.tasks.service;

import br.com.matheuspadilha.tasks.model.Task;
import br.com.matheuspadilha.tasks.repository.TaskCustomRepository;
import br.com.matheuspadilha.tasks.repository.TaskRepository;
import br.com.matheuspadilha.tasks.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceTest {

    @InjectMocks
    private TaskService service;
    @Mock
    private TaskRepository repository;
    @Mock
    private TaskCustomRepository taskCustomRepository;

    @Test
    void service_mustReturneTask_whenInsertSuccessfully() {
        Task task = TestUtils.buildValidTask();

        when(repository.save(any())).thenReturn(Mono.just(task));

        StepVerifier.create(service.insert(task))
                .then( () -> verify(repository, times(1)).save(any()))
                .expectNext(task)
                .expectComplete()
                .verify();
    }

    @Test
    void service_mustReturnTaskPage_whenFindPaginated() {
        Task task = TestUtils.buildValidTask();

        when(taskCustomRepository.findPaginated(any(), anyInt(), anyInt())).thenReturn(Mono.just(Page.empty()));

        Mono<Page<Task>> result = service.findPaginated(task, 0, 10);

        assertNotNull(result);
        verify(taskCustomRepository, times(1)).findPaginated(any(), anyInt(), anyInt());
    }

    @Test
    void service_mustReturnVoid_whenDeleteTaskSuccessfully() {
        when(repository.deleteById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteById("1213"))
                .then( () -> verify(repository, times(1)).deleteById(anyString()) )
                .verifyComplete();
    }
}