package com.wuujcik.todolist.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.wuujcik.todolist.persistence.*
import com.wuujcik.todolist.model.MealProvider
import kotlinx.coroutines.launch
import javax.inject.Inject


class ListViewModel @Inject constructor(private val mealProvider: MealProvider) : ViewModel() {


    val allMeals: LiveData<PagedList<Meal>> = mealProvider.getAll.toLiveData(30)


    fun deleteMeal(item: Meal) {
        viewModelScope.launch {
            mealProvider.deleteItem(item, viewModelScope)
        }
    }

    fun invalidateMeals() {
        allMeals.value?.dataSource?.invalidate()
    }

    fun attachDatabaseReadListeners() {
        viewModelScope.launch {
            mealProvider.attachDatabaseReadListeners(viewModelScope)
        }
    }

    fun detachDatabaseReadListener() {
        viewModelScope.launch {
            mealProvider.detachDatabaseReadListener()
        }
    }
}
