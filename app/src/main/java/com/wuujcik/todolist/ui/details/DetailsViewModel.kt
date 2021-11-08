package com.wuujcik.todolist.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wuujcik.todolist.model.MealProvider
import com.wuujcik.todolist.persistence.Meal
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private val mealProvider: MealProvider) : ViewModel() {

    fun createItem(item: Meal) {
            mealProvider.addItem(item, viewModelScope)
    }

    fun updateItem(item: Meal) {
        viewModelScope.launch {
            mealProvider.updateItem(item, viewModelScope)
        }
    }
}
