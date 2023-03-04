package com.wuujcik.todolist

import android.app.Application
import com.wuujcik.todolist.di.daoModule
import com.wuujcik.todolist.di.databaseModule
import com.wuujcik.todolist.di.providerModule
import com.wuujcik.todolist.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TodoListApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TodoListApp)
            modules(
                viewModelModule,
                providerModule,
                databaseModule,
                daoModule,
            )
        }
    }
}
