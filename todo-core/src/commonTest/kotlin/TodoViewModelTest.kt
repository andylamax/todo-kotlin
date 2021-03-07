import expect.expect
import expect.toBe
import todo.TodoViewModel
import todo.TodoViewModel.Intent
import todo.TodoViewModel.State
import tz.co.asoft.asyncTest
import viewmodel.test
import kotlin.test.Test

class TodoViewModelTest {
    private val vm = TodoViewModel()

    @Test
    fun should_create_a_todo_without_failure() = asyncTest {
        vm.test(Intent.Create("Test Todo"))
        expect(vm.state.value).toBe<State.TodoList>()
    }
}