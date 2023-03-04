package com.wuujcik.todolist.di

import com.wuujcik.todolist.model.TodoProvider
import org.koin.dsl.module


/**
 * Module containing providers.
 */
val providerModule = module {
    single {
        TodoProvider(
            todoDao = get(),
            firebaseDb = get(),
        )
    }
}
