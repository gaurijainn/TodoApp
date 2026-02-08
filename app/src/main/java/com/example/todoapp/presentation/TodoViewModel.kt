package com.example.todoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.todoapp.data.TodoDataRepository
import com.example.todoapp.data.TodoDatabase
import com.example.todoapp.domain.Todo
import com.example.todoapp.domain.addTodoUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class TodoViewModel(application: Application): AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        context = application,
        klass = TodoDatabase::class.java,
        name = "my_todo_db"
    ).build()

    private val todoRepository = TodoDataRepository(dao = db.todoDao())
    private val addUseCase = addTodoUseCase(todoRepository)

    open val todos = todoRepository.getTodos().stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    fun addTodo(title: String) {
        viewModelScope.launch {
            addUseCase.execute(title)
        }
    }

    fun toggleTodoDone(todo: Todo){
        viewModelScope.launch {
            todoRepository.updateTodo(todo.copy(isDone = !todo.isDone))
        }
    }

    fun editTodo(todo: Todo, newTitle: String) {
        viewModelScope.launch {
            if(newTitle.isNotBlank()) {
                todoRepository.updateTodo(todo.copy(title = newTitle))
            }
        }
    }

    fun deleteTodo(todo: Todo){
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }
    }
}