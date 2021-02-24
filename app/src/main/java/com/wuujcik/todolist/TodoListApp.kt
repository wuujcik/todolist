package com.wuujcik.todolist

import android.app.Application
import com.wuujcik.todolist.di.AppComponent
import com.wuujcik.todolist.di.DaggerAppComponent

open class TodoListApp: Application() {

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
