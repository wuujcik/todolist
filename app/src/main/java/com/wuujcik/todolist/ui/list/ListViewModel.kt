package com.wuujcik.todolist.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.wuujcik.todolist.persistence.*
import com.wuujcik.todolist.model.TodoProvider
import kotlinx.coroutines.launch
import javax.inject.Inject


class ListViewModel @Inject constructor(private val todoProvider: TodoProvider) : ViewModel() {


    val allTodos: LiveData<PagedList<Todo>> = todoProvider.getAll.toLiveData(30)


    fun deleteTodo(item: Todo) {
        viewModelScope.launch {
            todoProvider.deleteItem(item, viewModelScope)
        }
    }

    fun invalidateTodos() {
        allTodos.value?.dataSource?.invalidate()
    }

    fun attachDatabaseReadListeners() {
        viewModelScope.launch {
            todoProvider.attachDatabaseReadListeners(viewModelScope)
        }
    }

    fun detachDatabaseReadListener() {
        viewModelScope.launch {
            todoProvider.detachDatabaseReadListener()
        }
    }

    fun createQuickItem(item: Todo) {
        todoProvider.addItem(item, viewModelScope)
    }
}
