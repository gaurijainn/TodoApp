package com.example.todoapp.domain

class addTodoUseCase(private val todoRepository: TodoRepository) {
    suspend fun execute(title: String){
        todoRepository.addTodo(Todo(title = title, isDone = false))
    }
}