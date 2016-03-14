package com.srgzotov.hellokotlin

import java.util.*

data class ToDo(val name: String, val description: String, var completed: Boolean = false)

fun ArrayList<ToDo>.markCompleted(name: String) {
    find{it.name == name}?.completed = true
}
