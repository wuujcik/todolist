package com.wuujcik.todolist.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData


class TodoRepository(private val todoDao: TodoDao) {

    val getAll: LiveData<List<Todo>> = todoDao.getAll()

    val getAlltimestamps: LiveData<List<Long>> = todoDao.getAllTimestampts()

    @WorkerThread
    fun insert(item: Todo) {
        todoDao.insert(item)
    }

    @WorkerThread
    fun update(item:Todo) {
        todoDao.update(item)
    }

    @WorkerThread
    fun delete(itemTimestamp: Long) {
        todoDao.delete(itemTimestamp)
    }

    @WorkerThread
    fun deleteAll() {
        todoDao.deleteAll()
    }

    companion object {
        const val TAG = "LocationRepository"
    }
}
