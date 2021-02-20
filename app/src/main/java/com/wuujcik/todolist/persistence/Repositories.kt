package com.wuujcik.todolist.persistence

import android.content.Context

class Repositories(val app: Context) {

    val todoRepository: TodoRepository

    init {
        val locationDao = TodoDatabase.getDatabase(app).todoDao()
        todoRepository = TodoRepository(locationDao)
    }
}