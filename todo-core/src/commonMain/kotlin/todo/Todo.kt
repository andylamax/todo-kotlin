package todo

data class Todo(
    val uid: Int? = null,
    val details: String,
    val completed: Boolean = false
)