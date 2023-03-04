package com.wuujcik.todolist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wuujcik.todolist.model.TodoProvider

class MainViewModel(private val todoProvider: TodoProvider) : ViewModel() {

    fun refreshFromFirebase() {
        todoProvider.refreshFromFirebase(viewModelScope)
    }
}
