package com.wuujcik.todolist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wuujcik.todolist.R
import com.wuujcik.todolist.model.TodoProvider
import javax.inject.Inject

class MainViewModel @Inject constructor(private val todoProvider: TodoProvider) : ViewModel() {

    var themeId = R.style.Theme_Todo

    fun changeTheme(resourceId: Int) {
        themeId = resourceId
    }

    fun refreshFromFirebase() {
        todoProvider.refreshFromFirebase(viewModelScope)
    }
}
