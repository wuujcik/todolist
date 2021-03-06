package com.wuujcik.todolist.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.wuujcik.todolist.persistence.*
import com.wuujcik.todolist.model.TodoProvider
import javax.inject.Inject


class ListViewModel @Inject constructor(private val todoProvider: TodoProvider) : ViewModel() {


    val allTodos: LiveData<PagedList<Todo>> = todoProvider.getAll.toLiveData(30)


    fun deleteTodo(item: Todo) {
        todoProvider.deleteItem(item)
    }

    fun invalidateTodos() {
        allTodos.value?.dataSource?.invalidate()
    }

    fun attachDatabaseReadListeners() {
        todoProvider.attachDatabaseReadListeners()
    }

    fun detachDatabaseReadListener() {
        todoProvider.detachDatabaseReadListener()
    }
}
