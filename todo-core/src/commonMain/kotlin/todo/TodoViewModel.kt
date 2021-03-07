package todo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import todo.TodoViewModel.Intent
import todo.TodoViewModel.State
import viewmodel.IntentBus
import viewmodel.ViewModel

class TodoViewModel(
    private val service: TodoService = TodoService("test")
) : ViewModel<Intent, State>(State.Loading("Preparing todos")) {
    sealed class State {
        data class Loading(val msg: String) : State()
        data class Success(val msg: String) : State()
        data class TodoList(val todos: List<Todo>) : State()
        object BlankList : State()
        data class Error(val cause: Throwable, val origin: Intent) : State()
    }

    sealed class Intent {
        object LoadTodos : Intent()
        data class Create(val details: String) : Intent()
        data class Edit(val todo: Todo) : Intent()
        data class Complete(val uid: Int?) : Intent()
        data class UnComplete(val uid: Int?) : Intent()
    }

    override fun CoroutineScope.execute(i: Intent) = when (i) {
        is Intent.LoadTodos -> loadTodos(i)
        is Intent.Create -> create(i)
        is Intent.Edit -> edit(i)
        is Intent.Complete -> setCompleteStatus(i, i.uid, completed = true)
        is Intent.UnComplete -> setCompleteStatus(i, i.uid, completed = false)
    }

    private fun CoroutineScope.setCompleteStatus(i: Intent, uid: Int?, completed: Boolean) = launch {
        state.value = State.Loading("Setting completed=$completed")
        flow<State> {
            val id = uid ?: throw Exception("uid is null")
            emit(State.Loading("Searching for todo with uid=$uid"))
            val todo = service.load(id) ?: throw Exception("Todo(uid=$id) not found")
            emit(State.Loading("Marking Todo(uid=$uid) as completed=$completed"))
            val td = service.edit(todo.copy(completed = completed))
            emit(State.Loading("Success"))
        }.catch {
            emit(State.Error(Exception("Failed to update todo", it), i))
            delay(3000)
        }.collect {
            state.value = it
        }
        loadTodos(Intent.LoadTodos)
    }

    private fun CoroutineScope.edit(i: Intent.Edit) = launch {
        state.value = State.Loading("Editing todo, please wait")
        flow<State> {
            val todo = service.edit(i.todo)
            emit(State.Success("Todo(${todo.details}) edited successfully"))
        }.catch {
            emit(State.Error(it, i))
            delay(3000)
        }.collect {
            state.value = it
        }
        loadTodos(Intent.LoadTodos)
    }

    private fun CoroutineScope.create(i: Intent.Create) = launch {
        state.value = State.Loading("Creating a new todo")
        flow<State> {
            val todo = service.create(i.details)
            emit(State.Success("Todo(${todo.details}) created successfully"))
        }.catch {
            emit(State.Error(it, i))
            delay(3000)
        }.collect {
            state.value = it
        }
        loadTodos(Intent.LoadTodos)
    }

    private fun CoroutineScope.loadTodos(i: Intent.LoadTodos) = launch {
        state.value = State.Loading("Fetching all todos")
        flow {
            val todos = service.all()
            val ui = if (todos.isEmpty()) State.BlankList else State.TodoList(todos)
            emit(ui)
        }.catch {
            emit(State.Error(it, i))
        }.collect {
            state.value = it
        }
    }
}