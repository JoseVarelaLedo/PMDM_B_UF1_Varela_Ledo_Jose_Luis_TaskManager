package jose.uf1.taskmanager

data class Task(
    val title: String,
    val description: String,
    val deadline: Long,
    val creationDate: Long = System.currentTimeMillis()
)
