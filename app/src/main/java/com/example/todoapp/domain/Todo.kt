package com.example.todoapp.domain

data class Todo(
    val id: Int = 0,
    val title: String,
    var isDone: Boolean = false
)
