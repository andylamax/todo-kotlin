package todo

import kotlinx.css.em
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.TextDecorationLine
import kotlinx.css.properties.textDecoration
import kotlinx.css.textDecoration
import live.Watcher
import react.RBuilder
import react.RProps
import react.setState
import styled.css
import styled.styledBr
import styled.styledP
import todo.TodoViewModel.Intent
import todo.TodoViewModel.State
import tz.co.asoft.*
import viewmodel.VComponent

@JsExport
class TodoApp private constructor() : VComponent<RProps, Intent, State, TodoViewModel>() {
    override val viewModel by lazy { TodoViewModel() }
    private var watch1: Watcher<State>? = null
    override fun componentDidMount() {
        super.componentDidMount()
        watch1 = viewModel.state.watch { setState { ui = it } }
        post(Intent.LoadTodos)
    }

    override fun componentWillUnmount() {
        super.componentWillUnmount()
        watch1?.stop()
    }

    private fun RBuilder.Todo(todo: Todo) = Grid("4fr 1fr") {
        styledP {
            css {
                if (todo.completed) textDecoration(TextDecorationLine.lineThrough)
            }
            +todo.details
        }
        ContainedButton(if (todo.completed) "UnCheck" else "Check") {
            if (todo.completed) {
                post(Intent.UnComplete(todo.uid))
            } else {
                post(Intent.Complete(todo.uid))
            }
        }
    }

    private fun RBuilder.Form() = Form {
        Grid("4fr 1fr") {
            TextInput(name = "details")
            ContainedButton("Submit", FaPaperPlane)
        }
    } onSubmit {
        val details by text()
        viewModel.post(Intent.Create(details))
    }

    override fun RBuilder.render(ui: State) = Surface(margin = 3.em) {
        Form()
        for (i in 1..3) styledBr { }
        console.log(watcher)
        when (ui) {
            is State.Loading -> LoadingBox(ui.msg)
            is State.Success -> SuccessBox(ui.msg)
            is State.TodoList -> Grid { for (todo in ui.todos) Todo(todo) }
            is State.BlankList -> Grid { styledP { +"No Todos Yet" } }
            is State.Error -> ErrorBox(ui.cause)
        }
    }
}

fun RBuilder.TodoApp() = child(TodoApp::class) {}