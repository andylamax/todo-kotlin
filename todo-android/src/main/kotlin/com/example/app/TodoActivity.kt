package com.example.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import live.Watcher
import todo.Todo
import todo.TodoViewModel
import todo.TodoViewModel.Intent
import todo.TodoViewModel.State
import viewmodel.ViewModel

private val viewModel: ViewModel<Intent, State> = TodoViewModel()

class TodoActivity : AppCompatActivity() {
    private var watcher: Watcher<State>? = null

    private val message by lazy { findViewById<TextView>(R.id.message) }
    private val inputField by lazy { findViewById<TextInputEditText>(R.id.inputField) }
    private val container by lazy { findViewById<LinearLayout>(R.id.todoList) }
    private val addButton by lazy { findViewById<Button>(R.id.addButton) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_layout)
        addButton.setOnClickListener {
            viewModel.post(Intent.Create(inputField.text.toString()))
        }
    }

    override fun onResume() {
        super.onResume()
        watcher = viewModel.state.watch {
            runOnUiThread {
                when (it) {
                    is State.Loading -> show(msg = it.msg)
                    is State.Success -> show(msg = it.msg)
                    is State.TodoList -> show(it.todos)
                    is State.BlankList -> show(emptyList())
                    is State.Error -> show(msg = it.cause.message.toString())
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        watcher?.stop()
    }

    fun hideAll() {
        message.text = ""
        inputField.setText("")
    }

    fun show(msg: String) {
        hideAll()
        message.text = msg
    }

    fun Todo.view(): View {
        val view = LayoutInflater.from(this@TodoActivity).inflate(R.layout.todo_view, container, false)
        val textView = view.findViewById<TextView>(R.id.details)
        textView.text = details
        view.findViewById<CheckBox>(R.id.checkBox).apply {
            isChecked = completed
            setOnCheckedChangeListener { _, isChecked ->
                val i = if (isChecked) Intent.Complete(this@view.uid) else Intent.UnComplete(this@view.uid)
                viewModel.post(i)
            }
        }
        return view
    }

    fun show(todos: List<Todo>) {
        hideAll()
        container.removeAllViews()
        for (todo in todos) container.addView(todo.view())
    }
}
