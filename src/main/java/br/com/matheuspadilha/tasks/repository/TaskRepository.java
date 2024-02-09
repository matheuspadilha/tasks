package br.com.matheuspadilha.tasks.repository;

import br.com.matheuspadilha.tasks.model.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
}
