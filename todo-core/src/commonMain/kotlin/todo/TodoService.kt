package todo

import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.random.nextLong

class TodoService(val url: String) {
    private val todos = mutableMapOf<Int, Todo>()
    private val delayTime get() = Random.nextLong(500..2000L)
    suspend fun create(details: String): Todo {
        delay(delayTime)
        val uid = todos.size
        val todo = Todo(
            uid = todos.size,
            details = details
        )
        todos[uid] = todo
        return todo
    }

    suspend fun load(uid: Int): Todo? {
        delay(delayTime)
        return todos[uid]
    }

    suspend fun edit(todo: Todo): Todo {
        delay(delayTime)
        val uid = todo.uid ?: throw Exception("Provided todo has null id")
        todos[uid] = todo
        return todo
    }

    suspend fun delete(uid: Int): Todo? {
        delay(delayTime)
        return todos.remove(uid)
    }

    suspend fun all(): List<Todo> {
        delay(delayTime)
        return todos.values.toList()
    }
}