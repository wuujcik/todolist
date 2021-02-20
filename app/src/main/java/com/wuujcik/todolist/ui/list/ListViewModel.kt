package com.wuujcik.todolist.ui.list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wuujcik.todolist.model.TodoProvider
import com.wuujcik.todolist.persistence.Todo
import com.wuujcik.todolist.persistence.Repositories


class ListViewModel (val app: Application) : ViewModel() {

    private val todoProvider: TodoProvider
        get() {
            return TodoProvider(app)
        }

    val allTodos: LiveData<List<Todo>> = Repositories(app).todoRepository.getAll

    fun deleteTodo(item: Todo){
        todoProvider.deleteItem(item.timestamp)
        todoProvider.deleteItemInFirebase(item)
    }

    fun attachDatabaseReadListeners(){
        todoProvider.attachDatabaseReadListeners()
    }
    fun detachDatabaseReadListener(){
        todoProvider.detachDatabaseReadListener()
    }
}
