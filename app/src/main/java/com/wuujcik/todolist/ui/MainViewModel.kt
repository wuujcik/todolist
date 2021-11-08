package com.wuujcik.todolist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wuujcik.todolist.R
import com.wuujcik.todolist.model.MealProvider
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mealProvider: MealProvider) : ViewModel() {

    fun refreshFromFirebase() {
        mealProvider.refreshFromFirebase(viewModelScope)
    }
}
