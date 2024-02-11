package br.com.matheuspadilha.tasks.repository

import br.com.matheuspadilha.tasks.model.Task
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : ReactiveMongoRepository<Task, String>