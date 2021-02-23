package com.wuujcik.todolist.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.wuujcik.todolist.model.TodoProvider
import com.wuujcik.todolist.persistence.*


class ListViewModel (val app: Application) : AndroidViewModel(app) {

    private val todoProvider: TodoProvider
        get() {
            return TodoProvider(app)
        }


    val allTodos: LiveData<PagedList<Todo>> = todoProvider.getAll.toLiveData(30)


    fun deleteTodo(item: Todo){
        todoProvider.deleteItem(item.timestamp)
        todoProvider.deleteItemInFirebase(item)
    }


    fun invalidateTodos(){
        allTodos.value?.dataSource?.invalidate()
    }


    fun attachDatabaseReadListeners(){
        todoProvider.attachDatabaseReadListeners()
    }


    fun detachDatabaseReadListener(){
        todoProvider.detachDatabaseReadListener()
    }
}
