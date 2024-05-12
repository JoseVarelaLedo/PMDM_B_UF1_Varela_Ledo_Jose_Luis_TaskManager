package jose.uf1.taskmanager

data class Note(
    val title: String,
    val description: String,
    val creationDate: Long = System.currentTimeMillis()
)