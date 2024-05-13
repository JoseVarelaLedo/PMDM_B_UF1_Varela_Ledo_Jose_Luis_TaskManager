package jose.uf1.taskmanager

import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    var description: String,
    val creationDate: Long = System.currentTimeMillis()
)