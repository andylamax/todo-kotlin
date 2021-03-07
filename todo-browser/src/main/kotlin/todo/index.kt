package todo

import kotlinx.browser.document
import tz.co.asoft.setContent

fun main() = document.getElementById("root").setContent {
    TodoApp()
}