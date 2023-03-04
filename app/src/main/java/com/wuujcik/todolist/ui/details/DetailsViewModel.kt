package com.wuujcik.todolist.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wuujcik.todolist.model.TodoProvider
import com.wuujcik.todolist.persistence.Todo
import kotlinx.coroutines.launch

class DetailsViewModel (private val todoProvider: TodoProvider) : ViewModel() {

    fun createItem(item: Todo) {
            todoProvider.addItem(item, viewModelScope)
    }

    fun updateItem(item: Todo) {
        viewModelScope.launch {
            todoProvider.updateItem(item, viewModelScope)
        }
    }
}
