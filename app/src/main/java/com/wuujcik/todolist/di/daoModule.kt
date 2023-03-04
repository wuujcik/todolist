@file:Suppress("RemoveExplicitTypeArguments")

package com.wuujcik.todolist.di

import com.wuujcik.todolist.persistence.RoomDatabase
import com.wuujcik.todolist.persistence.TodoDao
import org.koin.dsl.module

/**
 * Module holding database access objects.
 */
val daoModule = module {
    single<TodoDao> { get<RoomDatabase>().todoDao() }
}
