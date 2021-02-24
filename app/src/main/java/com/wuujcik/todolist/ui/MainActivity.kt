package com.wuujcik.todolist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wuujcik.todolist.R
import com.wuujcik.todolist.TodoListApp
import com.wuujcik.todolist.di.MainActivityComponent

class MainActivity : AppCompatActivity() {

    lateinit var mainActivityComponent: MainActivityComponent
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        mainActivityComponent = (application as TodoListApp).appComponent.mainActivityComponent().create()
        mainActivityComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (database == null) {
            database?.setPersistenceEnabled(true)
            database = Firebase.database
        }
    }
}
