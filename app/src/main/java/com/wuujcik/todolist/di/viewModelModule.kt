package com.wuujcik.todolist.di

import com.wuujcik.todolist.ui.MainViewModel
import com.wuujcik.todolist.ui.details.DetailsViewModel
import com.wuujcik.todolist.ui.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Module containing view models.
 */
val viewModelModule = module {
    viewModel {
        ListViewModel(
            todoProvider = get(),
        )
    }

    viewModel {
        DetailsViewModel(
            todoProvider = get(),
        )
    }

    viewModel {
        MainViewModel(
            todoProvider = get(),
        )
    }
}
