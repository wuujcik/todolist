package com.wuujcik.todolist

import android.app.Application
import com.wuujcik.todolist.di.AppComponent
import com.wuujcik.todolist.di.DaggerAppComponent

open class TodoListApp: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
