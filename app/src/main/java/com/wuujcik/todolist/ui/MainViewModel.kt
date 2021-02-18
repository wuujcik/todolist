package com.wuujcik.todolist.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wuujcik.todolist.model.Todo

class MainViewModel(val app: Application) : ViewModel() {

//    private val smogDataProvider: SmogDataProvider
//        get() {
//            return SmogDataProvider(app)
//        }
//
//    private val smogDataRepository: SmogDataRepository
//        get() {
//            return Repositories(app).smogDataRepository
//        }

//    val lastUpdated = smogDataRepository.lastUpdated
//    val smogData = smogDataRepository.smogData


    val listOfTodos: LiveData<List<Todo>>? = null

    fun refreshData(completion: (() -> Unit)) {

    }

}

