package com.wuujcik.todolist.ui

import androidx.lifecycle.ViewModel
import com.wuujcik.todolist.R

class MainViewModel : ViewModel() {

    var themeId = R.style.Theme_Todo

    fun changeTheme(resourceId: Int) {
        themeId = resourceId
    }
}
