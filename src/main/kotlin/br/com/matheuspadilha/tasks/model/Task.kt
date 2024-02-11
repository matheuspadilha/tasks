package br.com.matheuspadilha.tasks.model

import br.com.matheuspadilha.tasks.enums.TaskState
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.Objects.isNull
import java.util.Objects.nonNull

@Document(collation = "{ 'locale': 'en', 'strength': 2 }")
data class Task(
    @Id
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val priority: Int = 0,
    var state: TaskState? = null,
    var address: Address? = null,
    var created: LocalDate? = null
) {

    constructor(title: String, description: String, priority: Int) : this(null, title, description, priority)

    fun insert(): Task = this.copy(state = TaskState.INSERT, created = LocalDate.now())

    fun update(oldTask: Task): Task = this.copy(state = oldTask.state!!)

    fun updateAddress(address: Address): Task = this.copy(address = address)

    fun createdNow(): Task = this.copy(created = LocalDate.now())

    fun start(): Task = this.copy(state = TaskState.DOING)

    fun done(): Task = this.copy(state = TaskState.DONE)

    fun createdIsEmpty(): Boolean = isNull(this.created)

    fun isValidPriority(): Boolean = this.priority > 0

    fun isValidState(): Boolean = nonNull(this.state)
}
